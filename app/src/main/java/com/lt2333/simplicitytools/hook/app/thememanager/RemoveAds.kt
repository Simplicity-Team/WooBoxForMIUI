package com.lt2333.simplicitytools.hook.app.thememanager

import com.lt2333.simplicitytools.util.XSPUtils
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class RemoveAds : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val classIfExists = XposedHelpers.findClassIfExists(
            "com.android.thememanager.basemodule.ad.model.AdInfoResponse", lpparam?.classLoader
        )
        val valueClass: Class<*> = XposedHelpers.findClass(
            "com.android.thememanager.basemodule.ad.model.AdInfo", lpparam?.classLoader
        )
        XposedHelpers.findAndHookMethod(
            classIfExists,
            "isAdValid",
            valueClass,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    if (XSPUtils.getBoolean("remove_thememanager_ads", false)) {
                        param?.result = false
                    }
                }
            })
    }
}