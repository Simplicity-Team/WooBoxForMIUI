package com.lt2333.simplicitytools.hooks.rules.t.systemui

import android.view.View
import android.widget.ImageView
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.highcapable.yukihookapi.hook.param.HookParam
import com.lt2333.simplicitytools.utils.hasEnable

object HideHDIconForT : YukiBaseHooker() {
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
        hasEnable("hide_new_hd_icon") {
            "com.android.systemui.statusbar.policy.HDController".hook {
                injectMember {
                    method { name = "update" }
                    beforeHook {
                        this.result = null
                    }
                }
            }
        }
    }
    private fun hide(it: HookParam) {
        hasEnable("hide_big_hd_icon") {
            //从当前实例中，获取名为mVolte的Field，并转换成ImageView类型，操作可见度
            it.instance.current().field {name = "mVolte" }.cast<ImageView>()?.visibility = View.GONE
        }
        hasEnable("hide_small_hd_icon") {
            it.instance.current().field {name = "mSmallHd" }.cast<ImageView>()?.visibility = View.GONE
        }
        hasEnable("hide_hd_no_service_icon") {
            it.instance.current().field {name = "mVolteNoService" }.cast<ImageView>()?.visibility = View.GONE
        }
    }
}