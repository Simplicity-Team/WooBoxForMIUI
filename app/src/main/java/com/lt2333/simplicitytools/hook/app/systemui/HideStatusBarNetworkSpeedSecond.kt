package com.lt2333.simplicitytools.hook.app.systemui

import com.lt2333.simplicitytools.util.XSPUtils
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class HideStatusBarNetworkSpeedSecond :IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val classIfExists = XposedHelpers.findClassIfExists(
            "com.android.systemui.statusbar.views.NetworkSpeedView",
            lpparam.classLoader
        )
        XposedHelpers.findAndHookMethod(
            classIfExists,
            "setNetworkSpeed",
            String::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    if (XSPUtils.getBoolean("hide_status_bar_network_speed_second", false)) {
                        if (param.args[0] != null) {
                            param.args[0] = (param.args[0] as String)
                                .replace("/", "")
                                .replace("s", "")
                                .replace("'", "")
                        }
                    }
                }
            })
    }
}