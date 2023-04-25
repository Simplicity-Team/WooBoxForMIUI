package com.lt2333.simplicitytools.hooks.rules.t.powerkeeper

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister

object DoNotClearAppForT : HookRegister() {
    override fun init() {
        findMethod("miui.process.ProcessManager") {
            name == "kill"
        }.hookBefore {
            hasEnable("do_not_clear_app") {
                it.result = false
            }
        }
    }
}