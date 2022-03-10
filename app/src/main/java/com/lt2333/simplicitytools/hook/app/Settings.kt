package com.lt2333.simplicitytools.hook.app

import com.lt2333.simplicitytools.hook.app.settings.ShowNotificationImportance
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

class Settings: IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        ShowNotificationImportance().handleLoadPackage(lpparam)
    }
}