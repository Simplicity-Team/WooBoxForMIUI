package com.lt2333.simplicitytools.hooks.rules.t.systemui

import android.view.View
import android.widget.LinearLayout
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.lt2333.simplicitytools.utils.hasEnable

object RemoveLockScreenCameraForT : YukiBaseHooker() {
    override fun onHook() {
        "com.android.systemui.statusbar.phone.KeyguardBottomAreaView".hook {
            injectMember {
                method { name = "onFinishInflate" }
                afterHook {
                    hasEnable("remove_lock_screen_camera") {
                        instance.current().field { name = "mRightAffordanceViewLayout" }
                            .cast<LinearLayout>()?.visibility = View.GONE
                    }
                }
            }
        }
    }
}