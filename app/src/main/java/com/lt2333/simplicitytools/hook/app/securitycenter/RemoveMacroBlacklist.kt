package com.lt2333.simplicitytools.hook.app.securitycenter

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class RemoveMacroBlacklist : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        var letter = 'a'
        for (i in 0..25) {
            val classIfExists = XposedHelpers.findClassIfExists(
                "com.miui.gamebooster.v.$letter" + "0", lpparam.classLoader
            ) ?: continue
            if (classIfExists.declaredMethods.size in 6..12 && classIfExists.fields.isEmpty() && classIfExists.declaredFields.size >= 2) {
                XposedHelpers.findAndHookMethod(classIfExists, "c", String::class.java,
                    object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: MethodHookParam?) {
                            param?.result = false
                        }
                    })
                return
            }
            letter++
        }
    }
}