package com.lt2333.simplicitytools.hook.app.android

import com.lt2333.simplicitytools.util.XSPUtils
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class DisableFlagSecure :IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val classIfExists = XposedHelpers.findClassIfExists(
            "com.android.server.wm.WindowState",
            lpparam.classLoader
        )
        XposedHelpers.findAndHookMethod(classIfExists,
            "isSecureLocked",
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    if (XSPUtils.getBoolean("disable_flag_secure",false)) {
                        param.result = false
                    }
                }
            })
    }
}