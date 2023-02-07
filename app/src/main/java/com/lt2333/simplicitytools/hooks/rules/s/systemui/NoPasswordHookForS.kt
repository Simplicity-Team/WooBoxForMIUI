package com.lt2333.simplicitytools.hooks.rules.s.systemui

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister

object NoPasswordHookForS : HookRegister() {

    override fun init() {
        hasEnable("no_need_to_enter_password_when_power_on") {
            findMethod("com.android.internal.widget.LockPatternUtils\$StrongAuthTracker") { name == "isBiometricAllowedForUser" }.hookBefore {
                it.result = true
            }
            findMethod("com.android.internal.widget.LockPatternUtils") { name == "isBiometricAllowedForUser" }.hookBefore {
                it.result = true
            }
        }

    }
}