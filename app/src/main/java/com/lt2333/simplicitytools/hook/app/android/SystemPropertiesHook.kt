package com.lt2333.simplicitytools.hook.app.android

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.util.XSPUtils
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object SystemPropertiesHook : HookRegister() {
    override fun init() {
        val mediaStepsSwitch = XSPUtils.getBoolean("media_volume_steps_switch", false)
        val mediaSteps = XSPUtils.getInt("media_volume_steps", 15)

        findMethod("android.os.SystemProperties") {
            name == "getInt" && returnType == Int::class.java
        }.hookBefore {
            when (it.args[0] as String) {
                "ro.config.media_vol_steps" -> if (mediaStepsSwitch) it.result = mediaSteps
            }
        }
    }
}