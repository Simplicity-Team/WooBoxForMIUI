package com.lt2333.simplicitytools.hook.app.systemui

import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookBeforeMethod
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

class HideWifiIcon : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val iconState = "com.android.systemui.statusbar.phone.StatusBarSignalPolicy\$WifiIconState"
        "com.android.systemui.statusbar.phone.MiuiStatusBarSignalPolicy".hookBeforeMethod(lpparam.classLoader, "updateWifiIconWithState", iconState) {
            hasEnable("hide_wifi_icon") {
                it.result = null
            }
        }
    }
}