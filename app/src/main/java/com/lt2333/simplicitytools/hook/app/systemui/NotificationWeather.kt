package com.lt2333.simplicitytools.hook.app.systemui

import android.view.ViewGroup
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


                val layout = XposedHelpers.findClass(
                    "androidx.constraintlayout.widget.ConstraintLayout\$LayoutParams",
                    lpparam.classLoader
                ).getConstructor(Int::class.java, Int::class.java).newInstance(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ) as ViewGroup.MarginLayoutParams

                XposedHelpers.setObjectField(
                    layout,
                    "bottomToTop",
                    viewGroup.context.resources.getIdentifier(
                        "date_time",
                        "id",
                        viewGroup.context.packageName
                    )
                )
                XposedHelpers.setObjectField(
                    layout,
                    "startToEnd",
                    viewGroup.context.resources.getIdentifier(
                        "big_time",
                        "id",
                        viewGroup.context.packageName
                    )
                )
                layout.marginStart = viewGroup.context.resources.getDimensionPixelSize(
                    viewGroup.context.resources.getIdentifier(
                        "notification_panel_time_date_space",
                        "dimen",
                        viewGroup.context.packageName
                    )
                )


                val textView = TextView(viewGroup.context).also {
                    it.setTextAppearance(viewGroup.context.resources.getIdentifier("TextAppearance.QSControl.Date","style",viewGroup.context.packageName))
                    it.text = "This is a TextView"
                    it.layoutParams = layout
                }

                viewGroup.addView(textView)

            }
        })
    }
}