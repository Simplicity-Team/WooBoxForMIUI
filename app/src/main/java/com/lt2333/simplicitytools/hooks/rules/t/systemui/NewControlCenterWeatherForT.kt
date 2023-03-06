package com.lt2333.simplicitytools.hooks.rules.t.systemui

import android.content.pm.ApplicationInfo
import android.widget.TextView
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.lt2333.simplicitytools.utils.XSPUtils
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.views.WeatherData

object NewControlCenterWeatherForT : YukiBaseHooker() {
    override fun onHook() = hasEnable("control_center_weather") {
        lateinit var weather: WeatherData
        var clockId: Int = -2
        val isDisplayCity = XSPUtils.getBoolean("notification_weather_city", false)

        "com.android.systemui.controlcenter.phone.widget.ControlCenterDateView".hook {
            injectMember {
                method {
                    name = "onDetachedFromWindow"
                    superClass(true)
                }
                beforeHook {
                    if (instance<TextView>().id == clockId) {
                        weather.onDetachedFromWindow()
                    }
                }
            }
        }

        "com.android.systemui.controlcenter.phone.widget.ControlCenterDateView".hook {
            injectMember {
                method {
                    name = "setText"
                    superClass(true)
                }
                beforeHook {
                    val time = args[0]?.toString()
                    val view = instance<TextView>()
                    if (view.id == clockId && time != null) {
                        args[0] = "${weather.weatherData}$time"
                    }
                }
            }
        }

        "com.android.systemui.shared.plugins.PluginInstance\$Factory".hook {
            injectMember {
                method {
                    name = "getClassLoader"
                }
                afterHook {
                    val appInfo = args[0] as ApplicationInfo
                    val classLoader = this.result as ClassLoader
                    if (appInfo.packageName == "miui.systemui.plugin") {
                        "miui.systemui.controlcenter.windowview.MainPanelHeaderController".hook {
                            injectMember {
                                appClassLoader = classLoader
                                method {
                                    name = "addClockViews"
                                }
                                afterHook {
                                    val dateView = instance.current().field { name = "dateView" }.cast<TextView>()
                                    if (dateView != null) {
                                        clockId = dateView.id
                                    }
                                    weather = WeatherData(dateView?.context, isDisplayCity)
                                    weather.callBacks = {
                                        dateView?.current {
                                            method {
                                                name = "updateTime"
                                                superClass(true)
                                            }.call()
                                        }
                                    }
                                }
                            }
                        }.onHookClassNotFoundFailure {  }
                    }
                }
            }
        }
    }
}