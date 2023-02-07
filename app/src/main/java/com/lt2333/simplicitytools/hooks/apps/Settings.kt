package com.lt2333.simplicitytools.hooks.apps

import android.os.Build
import com.lt2333.simplicitytools.hooks.rules.s.settings.ShowNotificationImportanceForS
import com.lt2333.simplicitytools.utils.xposed.base.AppRegister
import de.robv.android.xposed.callbacks.XC_LoadPackage

object Settings: AppRegister() {
    override val packageName: String = "com.android.settings"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        when (Build.VERSION.SDK_INT) {
            Build.VERSION_CODES.TIRAMISU -> {
                autoInitHooks(lpparam,
                    ShowNotificationImportanceForS, //显示通知重要程度
                )
            }
            Build.VERSION_CODES.S -> {
                autoInitHooks(lpparam,
                    ShowNotificationImportanceForS, //显示通知重要程度
                )
            }
        }
    }
}