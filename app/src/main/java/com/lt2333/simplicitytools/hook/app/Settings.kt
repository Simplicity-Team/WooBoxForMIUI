package com.lt2333.simplicitytools.hook.app

import com.lt2333.simplicitytools.hook.app.settings.ShowNotificationImportance
import com.lt2333.simplicitytools.util.xposed.base.AppRegister
import de.robv.android.xposed.callbacks.XC_LoadPackage

object Settings: AppRegister() {
    override val packageName: String = "com.android.settings"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        autoInitHooks(lpparam,
            ShowNotificationImportance, //显示通知重要程度
        )
    }
}