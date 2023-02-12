package com.lt2333.simplicitytools.hooks.rules.all.android

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister

object DisableFlagSecureForAll : HookRegister() {
    override fun init() {
        // TODO: 23.1.30及之后的版本无法在部分场景生效，疑似MIUI做了修改 
        findMethod("com.android.server.wm.WindowState") {
            name == "isSecureLocked"
        }.hookBefore {
            hasEnable("disable_flag_secure") {
                it.result = false
            }
        }
    }

}