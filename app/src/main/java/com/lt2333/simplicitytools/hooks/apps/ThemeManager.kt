package com.lt2333.simplicitytools.hooks.apps

import android.os.Build
import com.lt2333.simplicitytools.hooks.rules.s.thememanager.RemoveAdsForS
import com.lt2333.simplicitytools.utils.xposed.base.AppRegister
import de.robv.android.xposed.callbacks.XC_LoadPackage

object ThemeManager: AppRegister() {
    override val packageName: String = "com.android.thememanager"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        when (Build.VERSION.SDK_INT) {
            Build.VERSION_CODES.TIRAMISU -> {
                autoInitHooks(lpparam,
                    RemoveAdsForS, //移除主题壁纸的广告
                )
            }
            Build.VERSION_CODES.S -> {
                autoInitHooks(lpparam,
                    RemoveAdsForS, //移除主题壁纸的广告
                )
            }
        }
    }
}