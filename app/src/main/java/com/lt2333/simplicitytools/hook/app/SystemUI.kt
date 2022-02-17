package com.lt2333.simplicitytools.hook.app

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.provider.Settings
import android.util.AttributeSet
import android.util.Log
import android.widget.TextView
import com.lt2333.simplicitytools.BuildConfig
import com.lt2333.simplicitytools.hook.MainHook
import de.robv.android.xposed.*
import de.robv.android.xposed.XC_MethodHook.MethodHookParam
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam
import java.lang.reflect.Method
import java.text.SimpleDateFormat
import java.util.*

class SystemUI : IXposedHookLoadPackage {

    var prefs = XSharedPreferences(BuildConfig.APPLICATION_ID, "config")

    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        //隐藏无SIM卡图标
        try {
            XposedHelpers.findAndHookMethod(
                "com.android.systemui.statusbar.phone.MiuiStatusBarSignalPolicy",
                lpparam.classLoader,
                "setNoSims",
                Boolean::class.javaPrimitiveType,
                Boolean::class.javaPrimitiveType,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (prefs.hasFileChanged()) {
                            prefs.reload()
                        }
                        if (prefs.getBoolean("hide_no_sim_icon", false)) {
                            param.result = null
                        }
                    }
                })
        } catch (e: Exception) {
            XposedBridge.log(e)
        }
        //隐藏VPN图标
        try {
            XposedHelpers.findAndHookMethod(
                "com.android.systemui.statusbar.phone.StatusBarSignalPolicy", lpparam.classLoader,
                "updateVpn",
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (prefs.hasFileChanged()) {
                            prefs.reload()
                        }
                        if (prefs.getBoolean("hide_vpn_icon", false)) {
                            param.result = null
                        }
                    }
                })
        } catch (e: Exception) {
            XposedBridge.log(e)
        }
        //隐藏飞行模式图标
        try {
            XposedHelpers.findAndHookMethod(
                "com.android.systemui.statusbar.phone.StatusBarSignalPolicy",
                lpparam.classLoader,
                "setIsAirplaneMode",
                XposedHelpers.findClass(
                    "com.android.systemui.statusbar.policy.NetworkController\$IconState",
                    lpparam.classLoader
                ),
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (prefs.hasFileChanged()) {
                            prefs.reload()
                        }
                        if (prefs.getBoolean("hide_airplane_icon", false)) {
                            param.result = null
                        }
                    }
                })
        } catch (e: Exception) {
            XposedBridge.log(e)
        }
        //隐藏WIFI图标
        try {
            XposedHelpers.findAndHookMethod(
                "com.android.systemui.statusbar.phone.MiuiStatusBarSignalPolicy",
                lpparam.classLoader,
                "updateWifiIconWithState",
                XposedHelpers.findClass(
                    "com.android.systemui.statusbar.phone.StatusBarSignalPolicy\$WifiIconState",
                    lpparam.classLoader
                ),
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (prefs.hasFileChanged()) {
                            prefs.reload()
                        }
                        if (prefs.getBoolean("hide_wifi_icon", false)) {
                            param.result = null
                        }
                    }
                })
        } catch (e: Exception) {
            XposedBridge.log(e)
        }
        //隐藏蓝牙图标
        try {
            XposedHelpers.findAndHookMethod(
                "com.android.systemui.statusbar.phone.MiuiPhoneStatusBarPolicy",
                lpparam.classLoader,
                "updateBluetooth",
                String::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (prefs.hasFileChanged()) {
                            prefs.reload()
                        }
                        if (prefs.getBoolean("hide_bluetooth_icon", false)) {
                            param.result = null
                        }
                    }
                })
        } catch (e: Exception) {
            XposedBridge.log(e)
        }
        //隐藏 声音、勿扰 图标
        try {
            XposedHelpers.findAndHookMethod(
                "com.android.systemui.statusbar.phone.MiuiPhoneStatusBarPolicy",
                lpparam.classLoader,
                "updateVolumeZen",
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (prefs.hasFileChanged()) {
                            prefs.reload()
                        }
                        if (prefs.getBoolean("hide_volume_zen_icon", false)) {
                            param.result = null
                        }
                    }
                })
        } catch (e: Exception) {
            XposedBridge.log(e)
        }
        //隐藏 闹钟 图标
        try {
            XposedHelpers.findAndHookMethod(
                "com.android.systemui.statusbar.phone.MiuiPhoneStatusBarPolicy",
                lpparam.classLoader,
                "onMiuiAlarmChanged",
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (prefs.hasFileChanged()) {
                            prefs.reload()
                        }
                        if (prefs.getBoolean("hide_alarm_icon", false)) {
                            param.result = null
                        }
                    }
                })
        } catch (e: Exception) {
            XposedBridge.log(e)
        }
        //隐藏 耳机 图标
        try {
            XposedHelpers.findAndHookMethod(
                "com.android.systemui.statusbar.phone.MiuiPhoneStatusBarPolicy",
                lpparam.classLoader,
                "updateHeadsetPlug",
                Intent::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (prefs.hasFileChanged()) {
                            prefs.reload()
                        }
                        if (prefs.getBoolean("hide_headset_icon", false)) {
                            param.result = null
                        }
                    }
                })
        } catch (e: Exception) {
            XposedBridge.log(e)
        }
        //隐藏 蓝牙电量 图标
        try {
            XposedHelpers.findAndHookMethod(
                "com.android.systemui.statusbar.phone.MiuiPhoneStatusBarPolicy",
                lpparam.classLoader,
                "updateBluetoothHandsfreeBattery",
                Intent::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (prefs.hasFileChanged()) {
                            prefs.reload()
                        }
                        if (prefs.getBoolean("hide_bluetooth_battery_icon", false)) {
                            param.result = null
                        }
                    }
                })
        } catch (e: Exception) {
            XposedBridge.log(e)
        }
        //隐藏卡一卡二
        try {
            XposedHelpers.findAndHookMethod(
                "com.android.systemui.statusbar.phone.StatusBarSignalPolicy", lpparam.classLoader,
                "hasCorrectSubs", MutableList::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (prefs.hasFileChanged()) {
                            prefs.reload()
                        }
                        val list = param.args[0] as MutableList<*>
                        val size = list.size

                        if (size == 2 && prefs.getBoolean("hide_sim_two_icon", false)) {
                            list.removeAt(1)
                        }
                        if (size >= 1 && prefs.getBoolean("hide_sim_one_icon", false)) {
                            list.removeAt(0)
                        }
                    }
                })
        } catch (e: Exception) {
            XposedBridge.log(e)
        }
        //隐藏HD
        try {
            XposedHelpers.findAndHookMethod(
                "com.android.systemui.statusbar.policy.MobileSignalController", lpparam.classLoader,
                "setVolte", Boolean::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (prefs.hasFileChanged()) {
                            prefs.reload()
                        }
                        if (prefs.getBoolean("hide_hd_icon", false)) {
                            param.args[0] = false
                        }
                    }
                })


            XposedHelpers.findAndHookMethod(
                "com.android.systemui.statusbar.policy.MobileSignalController", lpparam.classLoader,
                "updateVoiceIcon",
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (prefs.hasFileChanged()) {
                            prefs.reload()
                        }
                        if (prefs.getBoolean("hide_hd_icon", false)) {
                            param.result = null
                        }
                    }
                })
        } catch (e: Exception) {
            XposedBridge.log(e)
        }

        //隐藏 网速秒刷 图标
        try {
            XposedHelpers.findAndHookMethod(
                "com.android.systemui.statusbar.policy.NetworkSpeedController",
                lpparam.classLoader,
                "postUpdateNetworkSpeedDelay",
                Long::class.java,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (prefs.hasFileChanged()) {
                            prefs.reload()
                        }
                        if (prefs.getBoolean("status_bar_network_speed_refresh_speed", false)) {
                            param.args[0] = 1000L
                        }
                    }
                })
        } catch (e: Exception) {
            XposedBridge.log(e)
        }
        //时钟显秒
        if (prefs.hasFileChanged()) {
            prefs.reload()
        }
        if (prefs.getBoolean("statusbar_time_seconds", false)) {
            var c: Context? = null
            val classIfExists = XposedHelpers.findClassIfExists(
                "com.android.systemui.statusbar.views.MiuiClock",
                lpparam.classLoader
            )
            XposedHelpers.findAndHookConstructor(classIfExists,
                Context::class.java,
                AttributeSet::class.java,
                Integer.TYPE,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        c = param.args[0] as Context
                        val textV = param.thisObject as TextView
                        val d: Method = textV.javaClass.getDeclaredMethod("updateTime")
                        val r = Runnable {
                            d.isAccessible = true
                            d.invoke(textV)
                        }

                        class T : TimerTask() {
                            override fun run() {
                                Handler(textV.context.mainLooper).post(r)
                            }
                        }
                        if (textV.resources.getResourceEntryName(textV.id) == "clock")
                            Timer().scheduleAtFixedRate(
                                T(),
                                1000 - System.currentTimeMillis() % 1000,
                                1000
                            )
                    }
                }
            )
            XposedHelpers.findAndHookMethod(classIfExists, "updateTime",
                object : XC_MethodHook() {
                    @SuppressLint("SetTextI18n", "SimpleDateFormat")
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val textV = param.thisObject as TextView
                        if (textV.resources.getResourceEntryName(textV.id) == "clock") {
                            val t = Settings.System.getString(
                                c!!.contentResolver,
                                Settings.System.TIME_12_24
                            )
                            if (t == "24")
                                textV.text =
                                    SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().time)
                            else
                                textV.text =
                                    textV.text.toString() + SimpleDateFormat(":ss").format(Calendar.getInstance().time)
                        }
                    }
                })

        }


    }

}
