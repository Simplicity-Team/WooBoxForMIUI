package com.lt2333.simplicitytools.hook.app.systemui

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object RemoveTheLeftSideOfTheLockScreen : HookRegister() {

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