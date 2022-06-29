package com.lt2333.simplicitytools.hook.app.systemui

import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.widget.TextView
import com.github.kyuubiran.ezxhelper.utils.*
import com.lt2333.simplicitytools.util.XSPUtils
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.xposed.base.HookRegister
import com.lt2333.simplicitytools.view.WeatherData


@SuppressLint("StaticFieldLeak")
object NewNotificationWeather : HookRegister() {
    lateinit var weather: WeatherData
    var clockId: Int = -2

    @SuppressLint("DiscouragedApi", "ClickableViewAccessibility")
    override fun init() = hasEnable("control_center_weather") {
        val isDisplayCity = XSPUtils.getBoolean("notification_weather_city", false)
        findMethod("com.android.systemui.controlcenter.phone.widget.ControlCenterDateView", findSuper = true) { name == "onDetachedFromWindow" }.hookBefore {
            if ((it.thisObject as TextView).id == clockId && this::weather.isInitialized) {
                weather.onDetachedFromWindow()
            }
        }
        findMethod("com.android.systemui.controlcenter.phone.widget.ControlCenterDateView", findSuper = true) { name == "setText" }.hookBefore {
            val time = it.args[0]?.toString()
            val view = it.thisObject as TextView
            if (view.id == clockId && time != null && this::weather.isInitialized) {
                // TODO("调高天气位置")
//                val layout = view.layoutParams as ViewGroup.MarginLayoutParams
//                val y = view.height / 2
//                layout.topMargin = -y
                it.args[0] = "${weather.weatherData}$time"
            }
        }

        findMethod("com.android.systemui.shared.plugins.PluginManagerImpl") { name == "getClassLoader" }.hookAfter { getClassLoader ->
            val appInfo = getClassLoader.args[0] as ApplicationInfo
            val classLoader = getClassLoader.result as ClassLoader
            if (appInfo.packageName == "miui.systemui.plugin") {
                findMethod("miui.systemui.controlcenter.windowview.MainPanelHeaderController", classLoader = classLoader) { name == "addClockViews" }.hookAfter {
                    val dateView = it.thisObject.getObjectAs<TextView>("dateView")
                    clockId = dateView.id
                    weather = WeatherData(dateView.context, isDisplayCity)
                    weather.callBacks = {
                        dateView.invokeMethod("updateTime")
                    }
                }
            }
        }
    }
}