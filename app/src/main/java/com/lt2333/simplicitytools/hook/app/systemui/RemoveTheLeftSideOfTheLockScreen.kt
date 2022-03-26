package com.lt2333.simplicitytools.hook.app.systemui

import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookBeforeMethod
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object RemoveTheLeftSideOfTheLockScreen: HookRegister() {

    override fun init() {
        "com.android.keyguard.negative.MiuiKeyguardMoveLeftViewContainer".hookBeforeMethod(getDefaultClassLoader(), "inflateLeftView") {
            hasEnable("remove_the_left_side_of_the_lock_screen") {
                it.result = null
            }
        }
    }

}