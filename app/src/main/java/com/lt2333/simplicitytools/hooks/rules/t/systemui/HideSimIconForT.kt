package com.lt2333.simplicitytools.hooks.rules.t.systemui

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister

object HideSimIconForT : HookRegister() {

    override fun init() {
        findMethod("com.android.systemui.statusbar.phone.StatusBarSignalPolicy") {
            name == "hasCorrectSubs" && parameterTypes[0] == MutableList::class.java
        }.hookBefore {
            val list = it.args[0] as MutableList<*>
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