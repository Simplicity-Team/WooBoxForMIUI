package com.lt2333.simplicitytools.hook.app.systemui

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object HideStatusBarNetworkSpeedSecond : HookRegister() {
    override fun init() {
        findMethod("com.android.systemui.statusbar.views.NetworkSpeedView") {
            name == "setNetworkSpeed" && parameterTypes[0] == String::class.java
        }.hookBefore {
            hasEnable("hide_status_bar_network_speed_second") {
                if (it.args[0] != null) {
                    (it.args[0] as String)
                        .replace("/", "")
                        .replace("s", "")
                        .replace("'", "")
                        .replace("วิ", "")
                }
            }
        }
    }
}