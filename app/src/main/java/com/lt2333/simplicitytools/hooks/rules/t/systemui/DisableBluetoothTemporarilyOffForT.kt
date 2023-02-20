package com.lt2333.simplicitytools.hooks.rules.t.systemui

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister

object DisableBluetoothTemporarilyOffForT: HookRegister() {
    override fun init()= hasEnable("disable_bluetooth_temporarily_off") {
        findMethod("com.android.settingslib.bluetooth.LocalBluetoothAdapter"){
            name == "isSupportBluetoothRestrict" && parameterCount==1
        }.hookBefore {
            it.result = false
        }
    }
}