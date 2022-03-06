package com.lt2333.simplicitytools.hook.app.systemui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.lt2333.simplicitytools.util.XSPUtils
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class HideWifiActivityIcon : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val classIfExists = XposedHelpers.findClassIfExists(
            "com.android.systemui.statusbar.StatusBarWifiView",
            lpparam.classLoader
        )
        XposedHelpers.findAndHookMethod(
            classIfExists,
            "initViewState",
            "com.android.systemui.statusbar.phone.StatusBarSignalPolicy\$WifiIconState",
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    //隐藏WIFI箭头
                    if (XSPUtils.getBoolean("hide_wifi_activity_icon", false)) {
                        (XposedHelpers.getObjectField(
                            param.thisObject,
                            "mWifiActivityView"
                        ) as ImageView).visibility = View.INVISIBLE
                    }

                    //隐藏WIFI标准图标
                    if (XSPUtils.getBoolean("hide_wifi_standard_icon", false)) {
                        (XposedHelpers.getObjectField(
                            param.thisObject,
                            "mWifiStandardView"
                        ) as TextView).visibility = View.INVISIBLE
                    }
                }
            })

        XposedHelpers.findAndHookMethod(
            classIfExists,
            "updateState",
            "com.android.systemui.statusbar.phone.StatusBarSignalPolicy\$WifiIconState",
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    if (XSPUtils.getBoolean("hide_wifi_activity_icon", false)) {
                        (XposedHelpers.getObjectField(
                            param.thisObject,
                            "mWifiActivityView"
                        ) as ImageView).visibility = View.INVISIBLE
                    }
                }
            })



    }
}