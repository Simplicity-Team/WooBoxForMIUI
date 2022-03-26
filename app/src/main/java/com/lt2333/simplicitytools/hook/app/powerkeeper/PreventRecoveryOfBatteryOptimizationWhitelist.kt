package com.lt2333.simplicitytools.hook.app.powerkeeper

import com.lt2333.simplicitytools.util.XSPUtils
import com.lt2333.simplicitytools.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers

object PreventRecoveryOfBatteryOptimizationWhitelist : HookRegister() {

    override fun init() {
        val classIfExists = XposedHelpers.findClassIfExists(
            "com.miui.powerkeeper.statemachine.ForceDozeController",
            getDefaultClassLoader()
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