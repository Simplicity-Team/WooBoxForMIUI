package com.lt2333.simplicitytools.hooks.rules.t.systemui

import android.view.View
import android.widget.ImageView
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.highcapable.yukihookapi.hook.param.HookParam
import com.lt2333.simplicitytools.utils.XSPUtils
import com.lt2333.simplicitytools.utils.hasEnable

object HideMobileTypeIconForT : YukiBaseHooker() {

    private val isBigType = XSPUtils.getBoolean("big_mobile_type_icon", false)

    override fun onHook() {
        "com.android.systemui.statusbar.StatusBarMobileView".hook {
            injectMember {
                method {
                    name = "initViewState"
                    paramCount = 1
                }
                afterHook {
                    hideMobileTypeIcon(this)
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
                    hideMobileTypeIcon(this)
                }
            }
        }
    }

    private fun hideMobileTypeIcon(it: HookParam) {
        hasEnable("hide_mobile_type_icon") {
            if (isBigType) {
                it.instance.current().field { name = "mMobileType" }.cast<ImageView>()?.visibility = View.GONE
                it.instance.current().field { name = "mMobileTypeImage" }.cast<ImageView>()?.visibility = View.GONE
                it.instance.current().field { name = "mMobileTypeSingle" }.cast<ImageView>()?.visibility = View.GONE
            } else {
                it.instance.current().field { name = "mMobileType" }.cast<ImageView>()?.visibility = View.INVISIBLE
                it.instance.current().field { name = "mMobileTypeImage" }.cast<ImageView>()?.visibility = View.INVISIBLE
                it.instance.current().field { name = "mMobileTypeSingle" }.cast<ImageView>()?.visibility = View.INVISIBLE
            }
        }
    }
}