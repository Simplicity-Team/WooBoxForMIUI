package com.lt2333.simplicitytools.hooks.rules.all.miuihome

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister

object AlwaysShowStatusBarClockForAll : HookRegister() {
    override fun init() {
        try {
            findMethod("com.miui.home.launcher.Workspace") {
                name == "isScreenHasClockGadget"
            }
        } catch (e: Exception) {
            findMethod("com.miui.home.launcher.Workspace") {
                name == "isScreenHasClockWidget"
            }
        } catch (e: Exception) {
            findMethod("com.miui.home.launcher.Workspace") {
                name == "isClockWidget"
            }
        }.hookBefore {
            hasEnable("home_time") {
                it.result = false
            }
        }
    }

}
