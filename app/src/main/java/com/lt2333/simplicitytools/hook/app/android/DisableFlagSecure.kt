package com.lt2333.simplicitytools.hook.app.android

import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookBeforeMethod
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object DisableFlagSecure : HookRegister() {

    override fun init() {
        "com.android.server.wm.WindowState".hookBeforeMethod(getDefaultClassLoader(), "isSecureLocked") {
            hasEnable("disable_flag_secure") {
                it.result = false
            }
        }
    }

}