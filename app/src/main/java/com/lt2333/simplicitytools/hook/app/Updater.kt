package com.lt2333.simplicitytools.hook.app

import com.lt2333.simplicitytools.util.XSPUtils
import com.lt2333.simplicitytools.util.hookBeforeMethod
import de.robv.android.xposed.IXposedHookLoadPackage
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
                    classIfExists.hookBeforeMethod("T") {
                        it.result = false
                    }
                    return
                }
                letter++
            }
        }
    }
}