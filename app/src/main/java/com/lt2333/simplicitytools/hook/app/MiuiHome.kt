package com.lt2333.simplicitytools.hook.app

import com.lt2333.simplicitytools.BuildConfig
import com.lt2333.simplicitytools.hook.MainHook
import de.robv.android.xposed.*
import de.robv.android.xposed.XC_MethodHook.MethodHookParam
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class MiuiHome : IXposedHookLoadPackage {

    var prefs = XSharedPreferences(BuildConfig.APPLICATION_ID, "config")

    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        //始终显示时钟
        try {
            XposedHelpers.findAndHookMethod(
                "com.miui.home.launcher.Workspace", lpparam.classLoader,
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