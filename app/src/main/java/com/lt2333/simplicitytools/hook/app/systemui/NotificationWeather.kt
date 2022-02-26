package com.lt2333.simplicitytools.hook.app.systemui

import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class NotificationWeather :IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val classIfExists = XposedHelpers.findClassIfExists(
            "com.android.systemui.qs.MiuiNotificationHeaderView",
            lpparam.classLoader
        )
        XposedHelpers.findAndHookMethod(classIfExists, "onFinishInflate", object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                val viewGroup = param.thisObject as ViewGroup
                val layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                ).also {
                    it.addRule(RelativeLayout.ABOVE)
                    it.addRule(RelativeLayout.START_OF)
                    it.addRule(RelativeLayout.END_OF)
                }
                val textView = TextView(viewGroup.context).also {
                    it.text = "This is a TextView"
                    it.textSize=18F
                    it.layoutParams = layoutParams
                }
                viewGroup.addView(textView)
            }
        })
    }
}