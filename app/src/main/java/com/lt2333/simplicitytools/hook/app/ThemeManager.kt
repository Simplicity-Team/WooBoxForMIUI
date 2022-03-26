package com.lt2333.simplicitytools.hook.app

import com.lt2333.simplicitytools.hook.app.thememanager.RemoveAds
import com.lt2333.simplicitytools.util.xposed.base.AppRegister
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

object ThemeManager: AppRegister() {
    override val packageName: String = "com.android.thememanager"
    override val processName: List<String> = emptyList()
    override val logTag: String = "Simplicitytools"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        XposedBridge.log("Simplicitytools: 成功 Hook "+javaClass.simpleName)
        //移除主题壁纸的广告
        RemoveAds().handleLoadPackage(lpparam)
    }
}