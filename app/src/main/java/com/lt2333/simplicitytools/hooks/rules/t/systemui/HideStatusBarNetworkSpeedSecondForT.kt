package com.lt2333.simplicitytools.hooks.rules.t.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.lt2333.simplicitytools.utils.hasEnable

object HideStatusBarNetworkSpeedSecondForT : YukiBaseHooker() {
    override fun onHook() {
        "com.android.systemui.statusbar.views.NetworkSpeedView".hook {
            injectMember {
                method {
                    name = "setNetworkSpeed"
                    paramCount = 1
                }
                beforeHook {
                    hasEnable("hide_status_bar_network_speed_second") {
                        if (args[0] != null) {
                            val mText = (args[0] as String)
                                .replace("/", "")
                                .replace("s", "")
                                .replace("\'", "")
                                .replace("วิ", "")
                            args[0] = mText
                        }
                    }
                }
            }
        }
    }
}