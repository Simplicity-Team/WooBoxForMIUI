package com.lt2333.simplicitytools.hook.app.android

import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookBeforeMethod
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

class DisableFlagSecure : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        "com.android.server.wm.WindowState".hookBeforeMethod(lpparam.classLoader, "isSecureLocked") {
            hasEnable("disable_flag_secure") {
                it.result = false
            }
        }
    }
}