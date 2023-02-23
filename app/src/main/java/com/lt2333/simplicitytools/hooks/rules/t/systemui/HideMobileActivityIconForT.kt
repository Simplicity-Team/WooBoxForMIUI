package com.lt2333.simplicitytools.hooks.rules.t.systemui

import android.view.View
import android.widget.ImageView
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.highcapable.yukihookapi.hook.param.HookParam
import com.lt2333.simplicitytools.utils.hasEnable

object HideMobileActivityIconForT : YukiBaseHooker() {
    override fun onHook() {
        "com.android.systemui.statusbar.StatusBarMobileView".hook {
            injectMember {
                method {
                    name = "initViewState"
                    paramCount = 1
                }
                afterHook {
                    hide(this)
                }
            }
        }

        "com.android.systemui.statusbar.StatusBarMobileView".hook {
            injectMember {
                method {
                    name = "updateState"
                    paramCount = 1
                }
                afterHook {
                    hide(this)
                }
            }
        }
    }

    private fun hide(it: HookParam) {
        hasEnable("hide_mobile_activity_icon") {
            it.instance.current().field { name = "mLeftInOut" }.cast<ImageView>()?.visibility = View.GONE
            it.instance.current().field { name = "mRightInOut" }.cast<ImageView>()?.visibility = View.GONE
        }
    }
}