package com.lt2333.simplicitytools.hook

import com.lt2333.simplicitytools.BuildConfig
import com.lt2333.simplicitytools.hook.app.Android
import com.lt2333.simplicitytools.hook.app.MiuiHome
import com.lt2333.simplicitytools.hook.app.PowerKeeper
import com.lt2333.simplicitytools.hook.app.SystemUI
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XSharedPreferences
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class MainHook : IXposedHookLoadPackage {

    var prefs = XSharedPreferences(BuildConfig.APPLICATION_ID, "config")

    override fun handleLoadPackage(lpparam: LoadPackageParam) {

        if (prefs.getBoolean("main_switch", true)) {

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
}