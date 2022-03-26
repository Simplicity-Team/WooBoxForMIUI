package com.lt2333.simplicitytools.hook.app.miuihome

import com.lt2333.simplicitytools.util.XSPUtils
import com.lt2333.simplicitytools.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers

object AlwaysDisplayTime : HookRegister() {

    override fun init() {
        val classIfExists = XposedHelpers.findClassIfExists(
            "com.miui.home.launcher.Workspace",
            getDefaultClassLoader()
        )
        XposedHelpers.findAndHookMethod(
            classIfExists,
            "isScreenHasClockGadget", Long::class.javaPrimitiveType,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    if (XSPUtils.getBoolean("home_time", false)) {
                        param.result = false
                    }
                }
            })
    }

}