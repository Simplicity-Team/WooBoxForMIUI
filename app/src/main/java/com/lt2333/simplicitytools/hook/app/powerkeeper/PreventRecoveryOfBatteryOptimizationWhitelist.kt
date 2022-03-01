package com.lt2333.simplicitytools.hook.app.powerkeeper

import com.lt2333.simplicitytools.util.XSPUtils
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class PreventRecoveryOfBatteryOptimizationWhitelist : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val classIfExists = XposedHelpers.findClassIfExists(
            "com.miui.powerkeeper.statemachine.ForceDozeController",
            lpparam.classLoader
        )
        XposedHelpers.findAndHookMethod(
            classIfExists,
            "restoreWhiteListAppsIfQuitForceIdle",
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    super.beforeHookedMethod(param)
                    if (XSPUtils.getBoolean(
                            "prevent_recovery_of_battery_optimization_white_list",
                            false
                        )
                    ) {
                        param.result = null
                    }
                }
            })
    }
}