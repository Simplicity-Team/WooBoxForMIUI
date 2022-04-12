package com.lt2333.simplicitytools.hook.app.android

import com.lt2333.simplicitytools.util.XSPUtils
import com.lt2333.simplicitytools.util.hookBeforeMethod
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object SystemPropertiesHook : HookRegister() {

    override fun init() {
        "android.os.SystemProperties".hookBeforeMethod(
            getDefaultClassLoader(),
            "getInt",
            String::class.java,
            Int::class.java
        ) {
            when (it.args[0] as String) {
                "ro.config.media_vol_steps" -> it.result = XSPUtils.getInt("media_volume_steps",15)
            }
        }
    }
}