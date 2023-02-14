package com.lt2333.simplicitytools.hooks.rules.t.systemui

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.lt2333.simplicitytools.utils.XSPUtils
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister

object CustomMobileTypeTextForT : HookRegister() {

    override fun init() = hasEnable("custom_mobile_type_text_switch") {
        findMethod("com.android.systemui.statusbar.connectivity.MobileSignalController") {
            name == "getMobileTypeName" && parameterTypes[0] == Int::class.java
        }.hookAfter {
            it.result = XSPUtils.getString("custom_mobile_type_text", "5G")
        }
    }

}