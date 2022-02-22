package com.lt2333.simplicitytools.hook.app.securitycenter

import android.widget.TextView
import com.lt2333.simplicitytools.util.XSPUtils
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class SkipWaitingTime : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {

        var classIfExists = XposedHelpers.findClassIfExists(
            "android.widget.TextView",
            lpparam.classLoader
        )
        XposedHelpers.findAndHookMethod(
            classIfExists,
            "setText",
            CharSequence::class.java,
            TextView.BufferType::class.java,
            Boolean::class.java,
            Int::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    if (XSPUtils.getBoolean("skip_waiting_time", false)) {
                        if (param.args.isNotEmpty() && param.args[0]?.toString()
                                ?.startsWith("确定(") == true
                        ) {
                            param.args[0] = "确定"
                        }
                    }
                }
            })

        val classIfExists2 = XposedHelpers.findClassIfExists(
            "android.widget.TextView",
            lpparam.classLoader
        )
        XposedHelpers.findAndHookMethod(
            classIfExists2,
            "setEnabled",
            Boolean::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    if (XSPUtils.getBoolean("skip_waiting_time", false)) {
                        param.args[0] = true
                    }
                }
            })
    }
}