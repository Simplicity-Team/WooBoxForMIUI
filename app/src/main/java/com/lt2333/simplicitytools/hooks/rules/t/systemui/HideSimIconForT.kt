package com.lt2333.simplicitytools.hooks.rules.t.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.lt2333.simplicitytools.utils.hasEnable

object HideSimIconForT:YukiBaseHooker() {
    override fun onHook() {
        "com.android.systemui.statusbar.phone.StatusBarSignalPolicy".hook {
            injectMember {
                method {
                    name = "hasCorrectSubs"
                    param(MutableList::class.java)
                }
                beforeHook {
                    val list = this.args[0] as MutableList<*>
                    val size = list.size
                    hasEnable("hide_sim_two_icon", extraCondition = { size == 2 }) {
                        list.removeAt(1)
                    }
                    hasEnable("hide_sim_one_icon", extraCondition = { size >= 1 }) {
                        list.removeAt(0)
                    }
                }
            }
        }

    }
}