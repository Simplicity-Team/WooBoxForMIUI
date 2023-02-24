package com.lt2333.simplicitytools.hooks.rules.t.systemui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.util.TypedValue
import android.widget.TextView
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.lt2333.simplicitytools.utils.XSPUtils
import java.lang.reflect.Method
import java.util.Calendar
import java.util.Date
import java.util.Timer
import java.util.TimerTask

object StatusBarTimeCustomizationForT : YukiBaseHooker() {

    private val getMode = XSPUtils.getInt("custom_clock_mode", 0)
    private val getClockSize = XSPUtils.getInt("status_bar_clock_size", 0)
    private val getClockDoubleSize = XSPUtils.getInt("status_bar_clock_double_line_size", 0)
    private val isYear = XSPUtils.getBoolean("status_bar_time_year", false)
    private val isMonth = XSPUtils.getBoolean("status_bar_time_month", false)
    private val isDay = XSPUtils.getBoolean("status_bar_time_day", false)
    private val isWeek = XSPUtils.getBoolean("status_bar_time_week", false)
    private val isHideSpace = XSPUtils.getBoolean("status_bar_time_hide_space", false)
    private val isDoubleLine = XSPUtils.getBoolean("status_bar_time_double_line", false)
    private val isSecond = XSPUtils.getBoolean("status_bar_time_seconds", false)
    private val isDoubleHour = XSPUtils.getBoolean("status_bar_time_double_hour", false)
    private val isPeriod = XSPUtils.getBoolean("status_bar_time_period", true)
    private val isCenterAlign = XSPUtils.getBoolean("status_bar_time_double_line_center_align", false)

    //极客模式
    private val getGeekClockSize = XSPUtils.getInt("status_bar_clock_size_geek", 0)
    private val getGeekFormat = XSPUtils.getString("custom_clock_format_geek", "HH:mm:ss")
    private val isGeekCenterAlign = XSPUtils.getBoolean("status_bar_time_center_align_geek", false)

    private lateinit var nowTime: Date
    private var str = ""

    @SuppressLint("SetTextI18n")
    override fun onHook() {
        when (getMode) {
            //预设模式
            1 -> {}
            //极客模式
            2 -> {
                var c: Context? = null
                "com.android.systemui.statusbar.views.MiuiClock".hook {
                    injectMember {
                        constructor { paramCount = 3 }
                        afterHook {
                            try {
                                c = this.args[0] as Context
                                val textV = instance<TextView>()
                                if (textV.resources.getResourceEntryName(textV.id) != "clock") return@afterHook
                                textV.isSingleLine = false
                                textV.setLineSpacing(0F, 0.8F)
                                if (getGeekClockSize != 0) {
                                    val clockSize = getGeekClockSize.toFloat()
                                    textV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, clockSize)
                                }

                                val d: Method = textV.javaClass.getDeclaredMethod("updateTime")
                                val r = Runnable {
                                    d.isAccessible = true
                                    d.invoke(textV)
                                }

                                class T : TimerTask() {
                                    override fun run() {
                                        Handler(textV.context.mainLooper).post(r)
                                    }
                                }
                                Timer().scheduleAtFixedRate(
                                    T(), 1000 - System.currentTimeMillis() % 1000, 1000
                                )
                            } catch (_: Exception) {
                            }
                        }
                    }
                }

                "com.android.systemui.statusbar.views.MiuiClock".hook {
                    injectMember {
                        method { name = "updateTime" }
                        beforeHook {
                            try {
                                val textV = instance<TextView>()
                                if (textV.resources.getResourceEntryName(textV.id) == "clock") {
                                    val resultSb = StringBuilder()
                                    val formatSb = StringBuilder(getGeekFormat.toString())

                                    val mMiuiStatusBarClockController =
                                        textV.current().field { name = "mMiuiStatusBarClockController" }
                                    val mCalendar =
                                        mMiuiStatusBarClockController.current()?.method { name = "getCalendar" }?.call()
                                    mCalendar?.current {
                                        method {
                                            name = "setTimeInMillis"
                                            paramCount = 1
                                        }.call(System.currentTimeMillis())
                                        method {
                                            name = "format"
                                            paramCount = 3
                                            returnType = StringBuilder::class.java
                                        }.call(c, resultSb, formatSb)
                                    }
                                    textV.text = resultSb.toString()
                                    resultNull()
                                }
                            } catch (_: Exception) {
                            }
                        }
                    }
                }
            }
        }
    }
}