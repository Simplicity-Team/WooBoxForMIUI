package com.lt2333.simplicitytools.hook.app.powerkeeper

import com.lt2333.simplicitytools.util.XSPUtils
import com.lt2333.simplicitytools.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers

object LockMaxFps : HookRegister() {

    override fun init() {
        val classIfExists = XposedHelpers.findClassIfExists(
            "com.miui.powerkeeper.statemachine.DisplayFrameSetting",
            getDefaultClassLoader()
        )
        XposedHelpers.findAndHookMethod(
            classIfExists,
            "setScreenEffect",
            String::class.java, Int::class.javaPrimitiveType, Int::class.javaPrimitiveType,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    super.beforeHookedMethod(param)
                    if (XSPUtils.getBoolean("lock_max_fps", false)) {
                        param.result = null
                    }
                }
            })
    }

}