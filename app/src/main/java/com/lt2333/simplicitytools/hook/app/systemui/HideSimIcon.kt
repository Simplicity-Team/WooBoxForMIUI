package com.lt2333.simplicitytools.hook.app.systemui

import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookBeforeMethod
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

class HideSimIcon :IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        "com.android.systemui.statusbar.phone.StatusBarSignalPolicy".hookBeforeMethod(lpparam.classLoader, "hasCorrectSubs", MutableList::class.java) {
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