package com.lt2333.simplicitytools.hooks.rules.all.android

import android.os.Build
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookAllConstructorBefore
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister

object DisableFlagSecureForAll : HookRegister() {
    override fun init() {
        findMethod("com.android.server.wm.WindowState") {
            name == "isSecureLocked"
        }.hookBefore {
            hasEnable("disable_flag_secure") {
                it.result = false
            }
        }

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
            findMethod("com.android.server.wm.WindowSurfaceController") {
                name == "setSecure"
            }.hookBefore {
                it.args[0] = false
            }
            hookAllConstructorBefore("com.android.server.wm.WindowSurfaceController") {
                var flags = it.args[2] as Int
                val secureFlag = 128
                flags = flags and secureFlag.inv()
                it.args[2] = flags
            }
        }
    }
}