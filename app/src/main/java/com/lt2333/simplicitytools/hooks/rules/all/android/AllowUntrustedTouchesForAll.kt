package com.lt2333.simplicitytools.hooks.rules.all.android

import android.content.Context
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookReturnConstant
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister

object AllowUntrustedTouchesForAll : HookRegister() {
    override fun init() = hasEnable("allow_untrusted_touches") {
        findMethod("android.hardware.input.InputManager") {
            name == "getBlockUntrustedTouchesMode" && parameterTypes[0] == Context::class.java
        }.hookReturnConstant(0)
    }

}