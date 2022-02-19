package com.lt2333.simplicitytools.hook.app

import com.lt2333.simplicitytools.BuildConfig
import de.robv.android.xposed.*
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class MiuiHome : IXposedHookLoadPackage {

    var prefs = XSharedPreferences(BuildConfig.APPLICATION_ID, "config")

    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        XposedBridge.log("成功Hook: "+javaClass.simpleName)
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
                        if (prefs.hasFileChanged()) {
                            prefs.reload()
                        }
                        if (prefs.getBoolean("home_time", false)) {
                            param.result = false
                        }
                    }
                })
        } catch (e: Exception) {
            XposedBridge.log(e)
        }

    }
}