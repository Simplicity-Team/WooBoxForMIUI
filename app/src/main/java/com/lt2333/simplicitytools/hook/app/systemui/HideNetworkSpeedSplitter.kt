package com.lt2333.simplicitytools.hook.app.systemui

import android.widget.TextView
import com.lt2333.simplicitytools.util.XSPUtils
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class HideNetworkSpeedSplitter : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val classIfExists = XposedHelpers.findClassIfExists(
            "com.android.systemui.statusbar.views.NetworkSpeedSplitter",
            lpparam.classLoader
        )

        XposedHelpers.findAndHookMethod(classIfExists, "init", object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                if (XSPUtils.getBoolean("hide_network_speed_splitter", false)) {
                    val textView = param.thisObject as TextView
                    textView.text = " "
                }
            }
        })
    }
}
