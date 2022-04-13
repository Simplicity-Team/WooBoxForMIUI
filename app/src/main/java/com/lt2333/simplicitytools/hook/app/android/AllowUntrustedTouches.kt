package com.lt2333.simplicitytools.hook.app.android

import android.content.Context
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookBeforeMethod
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object AllowUntrustedTouches : HookRegister() {

    override fun init() {
        hasEnable("allow_untrusted_touches") {
            "android.hardware.input.InputManager".hookBeforeMethod(
                getDefaultClassLoader(),
                "getBlockUntrustedTouchesMode",
                Context::class.java
            ) {
                it.result = 0
            }
        }
    }
}