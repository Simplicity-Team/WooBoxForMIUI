package com.lt2333.simplicitytools.hook.app.systemui

import com.lt2333.simplicitytools.util.XSPUtils
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class HideSimIcon :IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val classIfExists = XposedHelpers.findClassIfExists(
            "com.android.systemui.statusbar.phone.StatusBarSignalPolicy",
            lpparam.classLoader
        )
        XposedHelpers.findAndHookMethod(
            classIfExists,
            "hasCorrectSubs", MutableList::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    val list = param.args[0] as MutableList<*>
                    val size = list.size

                    if (size == 2 && XSPUtils.getBoolean("hide_sim_two_icon", false)) {
                        list.removeAt(1)
                    }
                    if (size >= 1 && XSPUtils.getBoolean("hide_sim_one_icon", false)) {
                        list.removeAt(0)
                    }
                }
            })
    }
}