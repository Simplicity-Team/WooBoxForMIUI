package com.lt2333.simplicitytools.hook.app.thememanager

import com.lt2333.simplicitytools.util.findClass
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookBeforeMethod
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

class RemoveAds : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        "com.android.thememanager.basemodule.ad.model.AdInfoResponse".hookBeforeMethod(lpparam.classLoader, "isAdValid", "com.android.thememanager.basemodule.ad.model.AdInfo".findClass(lpparam.classLoader)) {
            hasEnable("remove_thememanager_ads") {
                it.result = false
            }
        }
    }
}