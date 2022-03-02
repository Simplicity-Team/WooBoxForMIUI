package com.lt2333.simplicitytools.hook.app.android

import com.lt2333.simplicitytools.util.XSPUtils
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class RemoveSmallWindowRestrictions2 : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val classIfExists = XposedHelpers.findClassIfExists(
            "com.android.server.wm.Task",
            lpparam.classLoader
        )
        XposedHelpers.findAndHookMethod(
            classIfExists,
            "isResizeable",
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    if (XSPUtils.getBoolean("remove_small_window_restrictions", false)) {
                        param.result = true
                    }
                }
            })
        XposedHelpers.findAndHookMethod(
            classIfExists,
            "supportsMultiWindowInDisplayAreaForFreeForm",
            "com.android.server.wm.TaskDisplayArea",
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    if (XSPUtils.getBoolean("remove_small_window_restrictions", false)) {
                        param.result = true
                    }
                }
            })
    }
}