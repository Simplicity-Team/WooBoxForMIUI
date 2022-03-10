package com.lt2333.simplicitytools.hook.app.systemui

import android.content.Intent
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookBeforeMethod
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

class HideHeadsetIcon :IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        "com.android.systemui.statusbar.phone.MiuiPhoneStatusBarPolicy".hookBeforeMethod(lpparam.classLoader, "updateHeadsetPlug", Intent::class.java) {
            hasEnable("hide_headset_icon") {
                it.result = null
            }
        }
    }
}