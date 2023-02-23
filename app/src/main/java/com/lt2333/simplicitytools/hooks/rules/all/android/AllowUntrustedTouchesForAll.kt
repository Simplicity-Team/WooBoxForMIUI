package com.lt2333.simplicitytools.hooks.rules.all.android

import android.content.Context
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.lt2333.simplicitytools.utils.hasEnable

object AllowUntrustedTouchesForAll : YukiBaseHooker() {
    override fun onHook() = hasEnable("allow_untrusted_touches") {
        "android.hardware.input.InputManager".hook {
            injectMember {
                method {
                    name = "getBlockUntrustedTouchesMode"
                    param(Context::class.java)
                }
                replaceTo(0)
            }
        }
    }
}