package com.lt2333.simplicitytools.hooks.rules.s.miuihome

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister

object AlwaysDisplayTimeForS : HookRegister() {

    override fun init() {
        findMethod("com.miui.home.launcher.Workspace") {
            name == "isScreenHasClockGadget" && parameterCount == 1
        }.hookBefore {
            hasEnable("home_time") {
                it.result = false
            }
        }
    }

}