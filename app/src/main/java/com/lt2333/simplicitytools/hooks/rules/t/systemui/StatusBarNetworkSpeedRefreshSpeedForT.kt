package com.lt2333.simplicitytools.hooks.rules.t.systemui

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister

object StatusBarNetworkSpeedRefreshSpeedForT : HookRegister() {

    override fun init() {
        findMethod("com.android.systemui.statusbar.policy.NetworkSpeedController") {
            name == "postUpdateNetworkSpeedDelay" && parameterTypes[0] == Long::class.java
        }.hookBefore {
            hasEnable("status_bar_network_speed_refresh_speed") {
                it.args[0] = 1000L
            }
        }
    }

}