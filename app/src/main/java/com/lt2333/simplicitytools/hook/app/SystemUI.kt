package com.lt2333.simplicitytools.hook.app

import android.content.Intent
import android.util.Log
import com.lt2333.simplicitytools.BuildConfig
import com.lt2333.simplicitytools.hook.MainHook
import de.robv.android.xposed.*
import de.robv.android.xposed.XC_MethodHook.MethodHookParam
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

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

    }
}