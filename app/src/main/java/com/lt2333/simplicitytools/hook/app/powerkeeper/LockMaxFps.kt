package com.lt2333.simplicitytools.hook.app.powerkeeper

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object LockMaxFps : HookRegister() {

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