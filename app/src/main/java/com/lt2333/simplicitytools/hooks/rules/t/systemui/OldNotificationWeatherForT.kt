package com.lt2333.simplicitytools.hooks.rules.t.systemui

import android.content.ComponentName
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.lt2333.simplicitytools.utils.XSPUtils
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.views.WeatherView

object OldNotificationWeatherForT : YukiBaseHooker() {
    override fun onHook() = hasEnable("notification_weather") {
        var mWeatherView: TextView? = null
        val isDisplayCity = XSPUtils.getBoolean("notification_weather_city", false)
        "com.android.systemui.qs.MiuiQSHeaderView".hook {
            injectMember {
                method {
                    name = "onFinishInflate"
                }
                afterHook {
                    val viewGroup = instance<ViewGroup>()
                    val context = viewGroup.context

                    mWeatherView = WeatherView(context, isDisplayCity).apply {
                        setTextAppearance(
                            context.resources.getIdentifier(
                                "TextAppearance.StatusBar.Expanded.Clock.QuickSettingDate",
                                "style",
                                context.packageName
                            )
                        )
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
            }
        }

        "com.android.systemui.qs.MiuiQSHeaderView".hook {
            injectMember {
                method { name = "updateLayout" }
                afterHook {
                    val mOrientation = instance.current().field { name = "mOrientation" }.int()
                    if (mOrientation == 1) {
                        mWeatherView!!.visibility = View.VISIBLE
                    } else {
                        mWeatherView!!.visibility = View.GONE
                    }
                }
            }
        }

    }
}