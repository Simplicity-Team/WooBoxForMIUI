package com.lt2333.simplicitytools.hook.app.systemui

import android.content.ComponentName
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.github.kyuubiran.ezxhelper.utils.loadClass
import com.lt2333.simplicitytools.util.*
import com.lt2333.simplicitytools.view.WeatherView
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

class OldNotificationWeather : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        hasEnable("notification_weather") {
            var mWeatherView: TextView? = null
            val isDisplayCity = XSPUtils.getBoolean("notification_weather_city", false)
            "com.android.systemui.qs.MiuiQSHeaderView".hookAfterMethod(
                lpparam.classLoader,
                "onFinishInflate"
            ) {
                val viewGroup = it.thisObject as ViewGroup
                val context = viewGroup.context
                val layoutParam =
                    loadClass("androidx.constraintlayout.widget.ConstraintLayout\$LayoutParams")
                        .getConstructor(Int::class.java, Int::class.java)
                        .newInstance(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        ) as ViewGroup.MarginLayoutParams

                layoutParam.setObjectField(
                    "endToStart",
                    context.resources.getIdentifier(
                        "notification_shade_shortcut",
                        "id",
                        context.packageName
                    )
                )
                layoutParam.setObjectField(
                    "topToTop",
                    context.resources.getIdentifier(
                        "notification_shade_shortcut",
                        "id",
                        context.packageName
                    )
                )
                layoutParam.setObjectField(
                    "bottomToBottom",
                    context.resources.getIdentifier(
                        "notification_shade_shortcut",
                        "id",
                        context.packageName
                    )
                )

                mWeatherView = WeatherView(context, isDisplayCity).apply {
                    setTextAppearance(
                        context.resources.getIdentifier(
                            "TextAppearance.StatusBar.Expanded.Clock.QuickSettingDate",
                            "style",
                            context.packageName
                        )
                    )
                    layoutParams = layoutParam
                }
                viewGroup.addView(mWeatherView)
                (mWeatherView as WeatherView).setOnClickListener {
                    try {
                        val intent = Intent().apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            component = ComponentName(
                                "com.miui.weather2",
                                "com.miui.weather2.ActivityWeatherMain"
                            )
                        }
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "启动失败，可能是不支持", Toast.LENGTH_LONG).show()
                    }
                }
            }
            //解决横屏重叠
            "com.android.systemui.qs.MiuiQSHeaderView".hookAfterMethod(
                lpparam.classLoader,
                "updateLayout"
            ) {
                val mOritation = it.thisObject.getObjectField("mOrientation") as Int
                if (mOritation == 1) {
                    mWeatherView!!.visibility = View.VISIBLE
                } else {
                    mWeatherView!!.visibility = View.GONE
                }
            }
        }
    }
}

