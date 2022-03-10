package com.lt2333.simplicitytools.hook.app.systemui

import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookBeforeMethod
import com.lt2333.simplicitytools.util.isNonNull
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

class HideStatusBarNetworkSpeedSecond :IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        "com.android.systemui.statusbar.views.NetworkSpeedView".hookBeforeMethod(lpparam.classLoader, "setNetworkSpeed", String::class.java) {
            hasEnable("hide_status_bar_network_speed_second") {
                it.args[0].isNonNull { s ->
                    it.args[0] = (s as String)
                        .replace("/", "")
                        .replace("s", "")
                        .replace("'", "")
                }
            }
        }
    }
}