package com.lt2333.simplicitytools.hooks.rules.s.systemui

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister

object RemoveTheLeftSideOfTheLockScreenForS : HookRegister() {

    override fun init() {
        findMethod("com.android.keyguard.negative.MiuiKeyguardMoveLeftViewContainer") {
            name == "inflateLeftView"
        }.hookBefore {
            hasEnable("remove_the_left_side_of_the_lock_screen") {
                it.result = null
            }
        }
    }

}