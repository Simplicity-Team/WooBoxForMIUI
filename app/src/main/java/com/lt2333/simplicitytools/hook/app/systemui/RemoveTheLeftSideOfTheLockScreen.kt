package com.lt2333.simplicitytools.hook.app.systemui

import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookBeforeMethod
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

class RemoveTheLeftSideOfTheLockScreen : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        "com.android.keyguard.negative.MiuiKeyguardMoveLeftViewContainer".hookBeforeMethod(lpparam.classLoader, "inflateLeftView") {
            hasEnable("remove_the_left_side_of_the_lock_screen") {
                it.result = null
            }
        }
    }
}