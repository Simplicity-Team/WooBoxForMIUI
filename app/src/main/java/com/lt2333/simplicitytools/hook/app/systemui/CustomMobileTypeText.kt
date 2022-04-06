package com.lt2333.simplicitytools.hook.app.systemui

import com.lt2333.simplicitytools.util.XSPUtils
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookAfterMethod
import com.lt2333.simplicitytools.util.hookBeforeMethod
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object CustomMobileTypeText : HookRegister() {

    override fun init() {
        hasEnable("custom_mobile_type_text_switch") {
            "com.android.systemui.statusbar.policy.MobileSignalController".hookAfterMethod(
                getDefaultClassLoader(),
                "getMobileTypeName",
                Int::class.java
            ) {
                it.result = XSPUtils.getString("custom_mobile_type_text","5G")
            }
        }
    }
}