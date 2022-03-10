package com.lt2333.simplicitytools.hook.app.systemui

import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookBeforeMethod
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

class HideSlaveWifiIcon :IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        "com.android.systemui.statusbar.policy.SlaveWifiSignalController".hookBeforeMethod(lpparam.classLoader, "updateIconState") {
            hasEnable("hide_slave_wifi_icon") {
                it.result = null
            }
        }
    }
}