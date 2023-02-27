package com.lt2333.simplicitytools.hooks.rules.t.systemui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.provider.Settings
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.lt2333.simplicitytools.utils.XSPUtils
import java.lang.reflect.Method
import java.text.SimpleDateFormat
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
            1 -> {
                var c: Context? = null

                "com.android.systemui.statusbar.views.MiuiClock".hook {
                    injectMember {
                        constructor {
                            paramCount = 3
                        }
                        afterHook {
                            try {
                                c = this.args[0] as Context
                                val textV = instance<TextView>()
                                if (textV.resources.getResourceEntryName(textV.id) != "clock") return@afterHook
                                textV.isSingleLine = false
                                if (isDoubleLine) {
                                    str = "\n"
                                    var clockDoubleLineSize = 7F
                                    if (getClockDoubleSize != 0) {
                                        clockDoubleLineSize = getClockDoubleSize.toFloat()
                                    }
                                    textV.setTextSize(
                                        TypedValue.COMPLEX_UNIT_DIP,
                                        clockDoubleLineSize
                                    )
                                    textV.setLineSpacing(0F, 0.8F)
                                } else {
                                    if (getClockSize != 0) {
                                        val clockSize = getClockSize.toFloat()
                                        textV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, clockSize)
                                    }
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

                                if (isCenterAlign) {
                                    try {
                                        if (textV.resources.getResourceEntryName(textV.id) == "clock") {
                                            c = this.args[0] as Context
                                            textV.gravity = Gravity.CENTER
                                        }
                                    } catch (_: Exception) {
                                    }
                                }
                            } catch (_: Exception) {
                            }
                        }
                    }
                    injectMember {
                        method { name = "updateTime" }
                        afterHook {
                            try {
                                val textV = instance<TextView>()
                                if (textV.resources.getResourceEntryName(textV.id) == "clock") {
                                    val t = Settings.System.getString(
                                        c!!.contentResolver, Settings.System.TIME_12_24
                                    )
                                    val is24 = t == "24"
                                    nowTime = Calendar.getInstance().time
                                    textV.text = getDate(c!!) + str + getTime(c!!, is24)
                                }
                            } catch (_: Exception) {
                            }
                        }
                    }
                }
            }
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

                                if (isGeekCenterAlign) {
                                    try {
                                        if (textV.resources.getResourceEntryName(textV.id) == "clock") {
                                            c = this.args[0] as Context
                                            textV.gravity = Gravity.CENTER
                                        }
                                    } catch (_: Exception) {
                                    }
                                }
                            } catch (_: Exception) {
                            }
                        }
                    }
                    injectMember {
                        method { name = "updateTime" }
                        beforeHook {
                            try {
                                val textV = instance<TextView>()
                                if (textV.resources.getResourceEntryName(textV.id) == "clock") {
                                    val resultSb = StringBuilder()
                                    val formatSb = StringBuilder(getGeekFormat.toString())

                                    val mMiuiStatusBarClockController =
                                        textV.current()
                                            .field { name = "mMiuiStatusBarClockController" }
                                    val mCalendar =
                                        mMiuiStatusBarClockController.current()
                                            ?.method { name = "getCalendar" }?.call()
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
    @SuppressLint("SimpleDateFormat")
    private fun getDate(context: Context): String {
        var datePattern = ""
        val isZh = isZh(context)

        if (isYear) {
            if (isZh) {
                datePattern += "YY年"
            } else {
                datePattern += "YY"
                if (isMonth || isDay) datePattern += "/"
            }
        }
        if (isMonth) {
            if (isZh) {
                datePattern += "M月"
            } else {
                datePattern += "M"
                if (isDay) datePattern += "/"
            }
        }
        if (isDay) {
            datePattern += if (isZh) {
                "d日"
            } else {
                "d"
            }
        }
        if (isWeek) {
            if (!isHideSpace) datePattern = "$datePattern "
            datePattern += "E"
            if (!isDoubleLine) {
                if (!isHideSpace) datePattern = "$datePattern "
            }
        }
        datePattern = SimpleDateFormat(datePattern).format(nowTime)
        return datePattern
    }

    @SuppressLint("SimpleDateFormat")
    private fun getTime(context: Context, t: Boolean): String {
        var timePattern = ""
        val isZh = isZh(context)
        timePattern += if (t) "HH:mm" else "h:mm"
        if (isSecond) timePattern += ":ss"
        timePattern = SimpleDateFormat(timePattern).format(nowTime)
        if (isZh) timePattern = getPeriod(isZh) + timePattern else timePattern += getPeriod(isZh)
        timePattern = getDoubleHour() + timePattern
        return timePattern
    }

    @SuppressLint("SimpleDateFormat")
    private fun getPeriod(isZh: Boolean): String {
        var period = ""
        if (isPeriod) {
            if (isZh) {
                when (SimpleDateFormat("HH").format(nowTime)) {
                    "00", "01", "02", "03", "04", "05" -> {
                        period = "凌晨"
                    }
                    "06", "07", "08", "09", "10", "11" -> {
                        period = "上午"
                    }
                    "12" -> {
                        period = "中午"
                    }
                    "13", "14", "15", "16", "17" -> {
                        period = "下午"
                    }
                    "18" -> {
                        period = "傍晚"
                    }
                    "19", "20", "21", "22", "23" -> {
                        period = "晚上"
                    }
                }
            } else {
                period = SimpleDateFormat("a").format(nowTime)
                if (!isHideSpace) {
                    period = " $period"
                }
            }

        }
        return period
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDoubleHour(): String {
        var doubleHour = ""
        if (isDoubleHour) {
            when (SimpleDateFormat("HH").format(nowTime)) {
                "23", "00" -> {
                    doubleHour = "子时"
                }
                "01", "02" -> {
                    doubleHour = "丑时"
                }
                "03", "04" -> {
                    doubleHour = "寅时"
                }
                "05", "06" -> {
                    doubleHour = "卯时"
                }
                "07", "08" -> {
                    doubleHour = "辰时"
                }
                "09", "10" -> {
                    doubleHour = "巳时"
                }
                "11", "12" -> {
                    doubleHour = "午时"
                }
                "13", "14" -> {
                    doubleHour = "未时"
                }
                "15", "16" -> {
                    doubleHour = "申时"
                }
                "17", "18" -> {
                    doubleHour = "酉时"
                }
                "19", "20" -> {
                    doubleHour = "戌时"
                }
                "21", "22" -> {
                    doubleHour = "亥时"
                }
            }
            if (!isHideSpace) {
                doubleHour += " "
            }
        }
        return doubleHour
    }

    private fun isZh(context: Context): Boolean {
        val locale = context.resources.configuration.locale
        val language = locale.language
        return language.endsWith("zh")
    }
}