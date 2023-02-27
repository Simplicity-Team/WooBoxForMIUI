package com.lt2333.simplicitytools.hooks.rules.t.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.lt2333.simplicitytools.utils.hasEnable

object RemoveTheLeftSideOfTheLockScreenForT : YukiBaseHooker() {
    override fun onHook() {
        "com.android.keyguard.negative.MiuiKeyguardMoveLeftViewContainer".hook {
            injectMember {
                method { name = "inflateLeftView" }
                beforeHook {
                    hasEnable("remove_the_left_side_of_the_lock_screen") {
                        resultNull()
                    }
                }
            }
        }
    }
}