package com.lt2333.simplicitytools.hooks.rules.s.android

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister

object DisableFlagSecureForS : HookRegister() {
    override fun init() {
        findMethod("com.android.server.wm.WindowState") {
            name == "isSecureLocked"
        }.hookBefore {
            hasEnable("disable_flag_secure") {
                it.result = false
            }
        }
    }

}