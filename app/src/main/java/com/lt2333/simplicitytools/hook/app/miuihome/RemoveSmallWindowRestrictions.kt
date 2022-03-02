package com.lt2333.simplicitytools.hook.app.miuihome

import android.content.Context
import com.lt2333.simplicitytools.util.XSPUtils
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class RemoveSmallWindowRestrictions : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val classIfExists = XposedHelpers.findClassIfExists(
            "com.miui.home.launcher.RecentsAndFSGestureUtils",
            lpparam.classLoader
        )
        XposedHelpers.findAndHookMethod(
            classIfExists,
            "isPkgSupportSmallWindow", Context::class.java, String::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    if (XSPUtils.getBoolean("remove_small_window_restrictions", false)) {
                        param.result = true
                    }
                }
            })
        XposedHelpers.findAndHookMethod(
            classIfExists,
            "isTaskSupportSmallWindow", Context::class.java, Int::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    if (XSPUtils.getBoolean("remove_small_window_restrictions", false)) {
                        param.result = true
                    }
                }
            })

    }
}