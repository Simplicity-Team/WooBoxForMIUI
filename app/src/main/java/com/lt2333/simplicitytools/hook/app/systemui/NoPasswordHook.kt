package com.lt2333.simplicitytools.hook.app.systemui

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object NoPasswordHook : HookRegister() {

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