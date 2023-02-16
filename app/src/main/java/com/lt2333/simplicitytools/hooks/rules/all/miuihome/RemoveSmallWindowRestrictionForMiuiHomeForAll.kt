package com.lt2333.simplicitytools.hooks.rules.all.miuihome

import com.github.kyuubiran.ezxhelper.utils.findAllMethods
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister

object RemoveSmallWindowRestrictionForMiuiHomeForAll : HookRegister() {
    override fun init() {

        findAllMethods("com.miui.home.launcher.RecentsAndFSGestureUtils") {
            name == "canTaskEnterSmallWindow"
        }.hookBefore {
            hasEnable("android_remove_small_window_restriction") {
                it.result = true
            }
        }
        findAllMethods("com.miui.home.launcher.RecentsAndFSGestureUtils") {
            name == "canTaskEnterMiniSmallWindow"
        }.hookBefore {
            hasEnable("android_remove_small_window_restriction") {
                it.result = true
            }
        }

    }

}
