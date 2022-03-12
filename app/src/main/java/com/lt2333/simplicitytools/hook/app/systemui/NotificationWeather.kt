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

class NotificationWeather : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        hasEnable("notification_weather") {
            var mWeatherView: TextView? = null
            val isDisplayCity = XSPUtils.getBoolean("notification_weather_city", false)
            "com.android.systemui.qs.MiuiNotificationHeaderView".hookAfterMethod(lpparam.classLoader, "onFinishInflate") {
                val viewGroup = it.thisObject as ViewGroup
                val context = viewGroup.context
                val layoutParam = loadClass("androidx.constraintlayout.widget.ConstraintLayout\$LayoutParams").getConstructor(Int::class.java, Int::class.java).newInstance(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT) as ViewGroup.MarginLayoutParams
                layoutParam.setObjectField("bottomToTop", context.resources.getIdentifier("date_time", "id", context.packageName))
                layoutParam.setObjectField("startToEnd", context.resources.getIdentifier("big_time", "id", context.packageName))
                layoutParam.marginStart = context.resources.getDimensionPixelSize(context.resources.getIdentifier("notification_panel_time_date_space", "dimen", context.packageName))
                mWeatherView = WeatherView(context, isDisplayCity).apply {
                    setTextAppearance(context.resources.getIdentifier("TextAppearance.QSControl.Date", "style", context.packageName))
                    layoutParams = layoutParam
                }
                viewGroup.addView(mWeatherView)
                (mWeatherView as WeatherView).setOnClickListener {
                    try {
                        val intent = Intent().apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            component = ComponentName("com.miui.weather2", "com.miui.weather2.ActivityWeatherMain")
                        }
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "启动失败", Toast.LENGTH_LONG).show()
                    }
                }
            }
            //解决横屏重叠
            "com.android.systemui.qs.MiuiNotificationHeaderView".hookAfterMethod(lpparam.classLoader, "updateLayout") {
                val mOrientation = it.thisObject.getObjectField("mOrientation") as Int
                if (mOrientation == 1) {
                    mWeatherView!!.visibility = View.VISIBLE
                } else {
                    mWeatherView!!.visibility = View.GONE
                }
            }
        }
    }
}