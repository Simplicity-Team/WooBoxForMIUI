package com.lt2333.simplicitytools.hook.app.systemui

import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookBeforeMethod
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

class HideAlarmIcon :IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        "com.android.systemui.statusbar.phone.MiuiPhoneStatusBarPolicy".hookBeforeMethod(lpparam.classLoader, "onMiuiAlarmChanged") {
            hasEnable("hide_alarm_icon") {
                it.result = null
            }
        }
    }
}