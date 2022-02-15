package com.lt2333.simplicitytools.hook

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam
import com.lt2333.simplicitytools.hook.app.PowerKeeper
import com.lt2333.simplicitytools.hook.app.SystemUI
import com.lt2333.simplicitytools.hook.app.MiuiHome
import com.lt2333.simplicitytools.hook.app.Android

class MainHook : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: LoadPackageParam) {

        when (lpparam.packageName) {
            "com.miui.powerkeeper" -> {
                PowerKeeper().handleLoadPackage(lpparam)
            }
            "com.android.systemui" -> {
                SystemUI().handleLoadPackage(lpparam)
            }
            "com.miui.home" -> {
                MiuiHome().handleLoadPackage(lpparam)
            }
            "android" -> {
                Android().handleLoadPackage(lpparam)
            }
        }
        
    }
}