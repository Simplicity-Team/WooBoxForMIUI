package com.lt2333.simplicitytools.hook.app.miuihome

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object AlwaysDisplayTime : HookRegister() {

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