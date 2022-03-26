package com.lt2333.simplicitytools.hook.app.systemui

import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookBeforeMethod
import com.lt2333.simplicitytools.util.isNonNull
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object HideStatusBarNetworkSpeedSecond: HookRegister() {

    override fun init() {
        "com.android.systemui.statusbar.views.NetworkSpeedView".hookBeforeMethod(getDefaultClassLoader(), "setNetworkSpeed", String::class.java) {
            hasEnable("hide_status_bar_network_speed_second") {
                it.args[0].isNonNull { s ->
                    it.args[0] = (s as String)
                        .replace("/", "")
                        .replace("s", "")
                        .replace("'", "")
                }
            }
        }
    }

}