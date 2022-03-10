package com.lt2333.simplicitytools.hook.app.systemui

import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookBeforeMethod
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

class HideNoSimIcon : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        "com.android.systemui.statusbar.phone.MiuiStatusBarSignalPolicy".hookBeforeMethod(lpparam.classLoader, "setNoSims", Boolean::class.javaPrimitiveType, Boolean::class.javaPrimitiveType) {
            hasEnable("hide_no_sim_icon") {
                it.result = null
            }
        }
    }
}