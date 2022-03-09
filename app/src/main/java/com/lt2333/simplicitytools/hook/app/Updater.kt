package com.lt2333.simplicitytools.hook.app

import com.lt2333.simplicitytools.util.XSPUtils
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class Updater : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (XSPUtils.getBoolean("remove_ota_validate", false)) {
            var letter = 'a'
            for (i in 0..25) {
                val classIfExists = XposedHelpers.findClassIfExists(
                    "com.android.updater.common.utils.$letter", lpparam.classLoader
                ) ?: continue
                if (classIfExists.declaredFields.size >= 9 && classIfExists.declaredMethods.size > 60) {
                    XposedHelpers.findAndHookMethod(classIfExists, "T", object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: MethodHookParam) {
                            param.result = false
                        }
                    })
                    return
                }
                letter++
            }
        }
    }
}