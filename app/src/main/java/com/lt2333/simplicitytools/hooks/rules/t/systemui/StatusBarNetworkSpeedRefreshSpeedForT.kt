package com.lt2333.simplicitytools.hooks.rules.t.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.lt2333.simplicitytools.utils.hasEnable

object StatusBarNetworkSpeedRefreshSpeedForT : YukiBaseHooker() {
    override fun onHook() {
        "com.android.systemui.statusbar.policy.NetworkSpeedController".hook {
            injectMember {
                method {
                    name = "postUpdateNetworkSpeedDelay"
                    param { it[0] == Long::class.java }
                }
                beforeHook {
                    hasEnable("status_bar_network_speed_refresh_speed") {
                        this.args[0] = 1000L
                    }
                }
            }
        }
    }
}