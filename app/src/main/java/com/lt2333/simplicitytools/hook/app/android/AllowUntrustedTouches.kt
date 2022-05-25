package com.lt2333.simplicitytools.hook.app.android

import android.content.Context
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookReturnConstant
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object AllowUntrustedTouches : HookRegister() {
    override fun init() = hasEnable("allow_untrusted_touches") {
        findMethod("android.hardware.input.InputManager") {
            name == "getBlockUntrustedTouchesMode" && parameterTypes[0] == Context::class.java
        }.hookReturnConstant(0)
    }

}