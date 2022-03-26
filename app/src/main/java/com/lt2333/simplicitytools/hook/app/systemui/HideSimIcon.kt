package com.lt2333.simplicitytools.hook.app.systemui

import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookBeforeMethod
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object HideSimIcon: HookRegister() {

    override fun init() {
        "com.android.systemui.statusbar.phone.StatusBarSignalPolicy".hookBeforeMethod(getDefaultClassLoader(), "hasCorrectSubs", MutableList::class.java) {
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