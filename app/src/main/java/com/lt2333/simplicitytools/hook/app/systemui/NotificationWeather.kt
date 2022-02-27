package com.lt2333.simplicitytools.hook.app.systemui

import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
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

                val layoutParams = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ).also {
                    it.bottomToTop = viewGroup.context.resources.getIdentifier("date_time","id",viewGroup.context.packageName)
                    it.startToEnd = viewGroup.context.resources.getIdentifier("big_time","id",viewGroup.context.packageName)
                    it.marginStart = viewGroup.context.resources.getIdentifier("notification_panel_time_date_space","dimen",viewGroup.context.packageName)
                }

                val textView = TextView(viewGroup.context).also {
                    it.setTextAppearance(viewGroup.context.resources.getIdentifier("TextAppearance.QSControl.Date","style",viewGroup.context.packageName))
                    it.text = "This is a TextView"
                    //TODO:状态栏天气定位异常
                    it.layoutParams = layoutParams
                }

                viewGroup.addView(textView)

            }
        })
    }
}