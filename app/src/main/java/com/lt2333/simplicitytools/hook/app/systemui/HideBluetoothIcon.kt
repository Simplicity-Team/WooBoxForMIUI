package com.lt2333.simplicitytools.hook.app.systemui

import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookBeforeMethod
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

class HideBluetoothIcon :IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        "com.android.systemui.statusbar.phone.MiuiPhoneStatusBarPolicy".hookBeforeMethod(lpparam.classLoader, "updateBluetooth", String::class.java) {
            hasEnable("hide_bluetooth_icon") {
                it.result = null
            }
        }
    }
}