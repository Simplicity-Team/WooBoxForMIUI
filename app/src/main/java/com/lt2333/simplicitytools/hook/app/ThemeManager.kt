package com.lt2333.simplicitytools.hook.app

import com.lt2333.simplicitytools.hook.app.thememanager.RemoveAds
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class ThemeManager : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        XposedBridge.log("Simplicitytools: 成功 Hook "+javaClass.simpleName)
        //移除主题壁纸的广告
        RemoveAds().handleLoadPackage(lpparam)
    }
}