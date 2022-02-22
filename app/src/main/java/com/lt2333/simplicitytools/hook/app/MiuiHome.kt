package com.lt2333.simplicitytools.hook.app

import com.lt2333.simplicitytools.util.XSPUtils
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class MiuiHome : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        XposedBridge.log("成功Hook: " + javaClass.simpleName)
        //始终显示时钟
        try {
            val classIfExists = XposedHelpers.findClassIfExists(
                "com.miui.home.launcher.Workspace",
                lpparam.classLoader
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
        } catch (e: Exception) {
            XposedBridge.log(e)
        }

    }
}