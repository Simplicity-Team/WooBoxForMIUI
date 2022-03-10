package com.lt2333.simplicitytools.hook.app.systemui

import android.content.ComponentName
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.lt2333.simplicitytools.util.XSPUtils
import com.lt2333.simplicitytools.view.WeatherView
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class OldNotificationWeather : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {

        var mWeatherView: TextView? = null

        if (!XSPUtils.getBoolean("notification_weather", false)) return

        val isDisplayCity = XSPUtils.getBoolean("notification_weather_city", false)

        val classIfExists = XposedHelpers.findClassIfExists(
            "com.android.systemui.qs.MiuiQSHeaderView",
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
                    "endToStart",
                    viewGroup.context.resources.getIdentifier(
                        "notification_shade_shortcut",
                        "id",
                        viewGroup.context.packageName
                    )
                )

                XposedHelpers.setObjectField(
                    layout,
                    "topToTop",
                    viewGroup.context.resources.getIdentifier(
                        "notification_shade_shortcut",
                        "id",
                        viewGroup.context.packageName
                    )
                )
                XposedHelpers.setObjectField(
                    layout,
                    "bottomToBottom",
                    viewGroup.context.resources.getIdentifier(
                        "notification_shade_shortcut",
                        "id",
                        viewGroup.context.packageName
                    )
                )


                mWeatherView = WeatherView(viewGroup.context,isDisplayCity).also {
                    it.setTextAppearance(
                        viewGroup.context.resources.getIdentifier(
                            "TextAppearance.StatusBar.Expanded.Clock.QuickSettingDate",
                            "style",
                            viewGroup.context.packageName
                        )
                    )
                    it.layoutParams = layout
                }
                viewGroup.addView(mWeatherView)
                (mWeatherView as WeatherView).setOnClickListener {
                    try {
                        val intent = Intent()
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        intent.component = ComponentName(
                            "com.miui.weather2",
                            "com.miui.weather2.ActivityWeatherMain"
                        )
                        viewGroup.context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(viewGroup.context, "启动失败，可能是不支持", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
        //解决横屏重叠
        XposedHelpers.findAndHookMethod(classIfExists, "updateLayout", object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                super.afterHookedMethod(param)
                val mOrientation =
                    XposedHelpers.getObjectField(param.thisObject, "mOrientation") as Int
                if (mOrientation == 1) {
                    mWeatherView!!.visibility = View.VISIBLE
                } else {
                    mWeatherView!!.visibility = View.GONE
                }
            }
        })
    }
}

