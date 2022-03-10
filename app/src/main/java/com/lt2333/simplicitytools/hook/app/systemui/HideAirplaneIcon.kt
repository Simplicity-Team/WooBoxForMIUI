package com.lt2333.simplicitytools.hook.app.systemui

import com.lt2333.simplicitytools.util.findClass
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookBeforeMethod
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

class HideAirplaneIcon : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val iconState = "com.android.systemui.statusbar.policy.NetworkController\$IconState".findClass(lpparam.classLoader)
        "com.android.systemui.statusbar.phone.StatusBarSignalPolicy".hookBeforeMethod(lpparam.classLoader, "setIsAirplaneMode", iconState) {
            hasEnable("hide_airplane_icon") {
                it.result = null
            }
        }
    }
}