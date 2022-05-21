package com.lt2333.simplicitytools.hook.app.powerkeeper

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object PreventRecoveryOfBatteryOptimizationWhitelist : HookRegister() {

    override fun init() {
        findMethod(
            "com.miui.powerkeeper.statemachine.ForceDozeController"
        ) {
            name == "restoreWhiteListAppsIfQuitForceIdle"
        }.hookBefore {
            hasEnable("prevent_recovery_of_battery_optimization_white_list") {
                it.result = null
            }
        }
    }

}