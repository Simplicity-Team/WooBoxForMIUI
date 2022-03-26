package com.lt2333.simplicitytools.hook.app.systemui

import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookBeforeMethod
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object StatusBarNetworkSpeedRefreshSpeed: HookRegister() {

    override fun init() {
        "com.android.systemui.statusbar.policy.NetworkSpeedController".hookBeforeMethod(getDefaultClassLoader(), "postUpdateNetworkSpeedDelay", Long::class.java) {
            hasEnable("status_bar_network_speed_refresh_speed") {
                it.args[0] = 1000L
            }
        }
    }

}