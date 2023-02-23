package com.lt2333.simplicitytools.hooks.rules.t.systemui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.highcapable.yukihookapi.hook.param.HookParam
import com.lt2333.simplicitytools.utils.hasEnable

object HideWifiActivityIconForT: YukiBaseHooker() {
    override fun onHook() {
        "com.android.systemui.statusbar.StatusBarWifiView".hook {
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
        "com.android.systemui.statusbar.StatusBarWifiView".hook {
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
        //隐藏WIFI箭头
        hasEnable("hide_wifi_activity_icon") {
            it.instance.current().field { name = "mWifiActivityView" }.cast<ImageView>()?.visibility = View.INVISIBLE
        }
        //隐藏WIFI标准图标
        hasEnable("hide_wifi_standard_icon") {
            it.instance.current().field { name = "mWifiStandardView" }.cast<TextView>()?.visibility = View.INVISIBLE
        }
    }
}