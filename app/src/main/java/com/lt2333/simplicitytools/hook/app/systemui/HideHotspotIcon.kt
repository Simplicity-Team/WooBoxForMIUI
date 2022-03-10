package com.lt2333.simplicitytools.hook.app.systemui

import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookBeforeMethod
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

class HideHotspotIcon :IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        "com.android.systemui.statusbar.phone.PhoneStatusBarPolicy\$2".hookBeforeMethod(lpparam.classLoader, "onHotspotChanged", Boolean::class.java, Int::class.java) {
            hasEnable("hide_hotspot_icon") {
                it.result = null
            }
        }
    }
}