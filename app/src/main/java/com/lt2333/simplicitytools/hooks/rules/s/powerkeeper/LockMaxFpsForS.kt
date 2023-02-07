package com.lt2333.simplicitytools.hooks.rules.s.powerkeeper

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister

object LockMaxFpsForS : HookRegister() {

    override fun init() {
        findMethod("com.miui.powerkeeper.statemachine.DisplayFrameSetting") {
            name == "setScreenEffect" && parameterCount == 3
        }.hookBefore {
            hasEnable("lock_max_fps") {
                it.result = null
            }
        }
    }

}