package com.lt2333.simplicitytools.hooks.rules.t.systemui

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.github.kyuubiran.ezxhelper.utils.putObject
import com.lt2333.simplicitytools.utils.getObjectField
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister

object HideSimIconForT : HookRegister() {

    override fun init() {
        findMethod("com.android.systemui.statusbar.StatusBarMobileView") {
            name == "applyMobileState"
        }.hookBefore {
            val mobileIconState = it.args[0]
            val subId = mobileIconState.getObjectField("subId") as Int
            hasEnable("hide_sim_one_icon", extraCondition = { subId == 1 }) {
                mobileIconState.putObject("visible", false)
            }
            hasEnable("hide_sim_two_icon", extraCondition = { subId == 2 }) {
                mobileIconState.putObject("visible", false)
            }
        }
    }

}