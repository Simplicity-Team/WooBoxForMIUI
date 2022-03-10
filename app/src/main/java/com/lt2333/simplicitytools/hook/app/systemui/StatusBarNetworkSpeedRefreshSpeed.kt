package com.lt2333.simplicitytools.hook.app.systemui

import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookBeforeMethod
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

class StatusBarNetworkSpeedRefreshSpeed :IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        "com.android.systemui.statusbar.policy.NetworkSpeedController".hookBeforeMethod(lpparam.classLoader, "postUpdateNetworkSpeedDelay", Long::class.java) {
            hasEnable("status_bar_network_speed_refresh_speed") {
                it.args[0] = 1000L
            }
        }
    }
}