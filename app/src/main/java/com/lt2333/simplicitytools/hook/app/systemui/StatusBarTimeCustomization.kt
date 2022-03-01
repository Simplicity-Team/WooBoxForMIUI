package com.lt2333.simplicitytools.hook.app.systemui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.provider.Settings
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.TextView
import com.lt2333.simplicitytools.util.XSPUtils
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.lang.reflect.Method
import java.text.SimpleDateFormat
import java.util.*

class StatusBarTimeCustomization : IXposedHookLoadPackage {

    var now_time: Date? = null
    var str = ""

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (XSPUtils.getBoolean("custom_clock_switch", false)) {
            var c: Context? = null
            val classIfExists = XposedHelpers.findClassIfExists(
                "com.android.systemui.statusbar.views.MiuiClock",
                lpparam.classLoader
            )
            XposedHelpers.findAndHookConstructor(classIfExists,
                Context::class.java,
                AttributeSet::class.java,
                Integer.TYPE,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        try {
                            c = param.args[0] as Context
                            val textV = param.thisObject as TextView
                            textV.isSingleLine = false

                            if (XSPUtils.getBoolean("status_bar_time_double_line", false)) {
                                str = "\n"
                                var clock_double_line_size = 7F
                                if (XSPUtils.getInt("status_bar_clock_double_line_size", 0) != 0) {
                                    clock_double_line_size =
                                        XSPUtils.getInt("status_bar_clock_double_line_size", 0)
                                            .toFloat()
                                }
                                textV.setTextSize(
                                    TypedValue.COMPLEX_UNIT_DIP,
                                    clock_double_line_size
                                )
                            } else {
                                if (XSPUtils.getInt("status_bar_clock_size", 0) != 0) {
                                    val clock_size =
                                        XSPUtils.getInt("status_bar_clock_size", 0).toFloat()
                                    textV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, clock_size)
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
                            if (textV.resources.getResourceEntryName(textV.id) == "clock")
                                Timer().scheduleAtFixedRate(
                                    T(),
                                    1000 - System.currentTimeMillis() % 1000,
                                    1000
                                )

                        } catch (e: java.lang.Exception) {
                        }
                    }
                }
            )
            XposedHelpers.findAndHookMethod(classIfExists, "updateTime",
                object : XC_MethodHook() {
                    @SuppressLint("SetTextI18n", "SimpleDateFormat")
                    override fun afterHookedMethod(param: MethodHookParam) {
                        try {
                            val textV = param.thisObject as TextView
                            if (textV.resources.getResourceEntryName(textV.id) == "clock") {
                                val t = Settings.System.getString(
                                    c!!.contentResolver,
                                    Settings.System.TIME_12_24
                                )
                                now_time = Calendar.getInstance().time

                                textV.text =
                                    getYear() + getMonth() + getDay() + getDateSpace() + getWeek() + str + getDoubleHour() + getTime(
                                        t
                                    ) + getSecond()
                            }

                        } catch (e: Exception) {
                        }
                    }
                })
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getYear(): String {
        if (XSPUtils.getBoolean("status_bar_time_year", false)) {
            return SimpleDateFormat("YY年").format(now_time)
        }
        return ""
    }

    @SuppressLint("SimpleDateFormat")
    private fun getMonth(): String {
        if (XSPUtils.getBoolean("status_bar_time_month", false)) {
            return SimpleDateFormat("M月").format(now_time)
        }
        return ""
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDay(): String {
        if (XSPUtils.getBoolean("status_bar_time_day", false)) {
            return SimpleDateFormat("d日").format(now_time)
        }
        return ""
    }

    @SuppressLint("SimpleDateFormat")
    private fun getSecond(): String {
        if (XSPUtils.getBoolean("status_bar_time_seconds", false)) {
            return SimpleDateFormat(":ss").format(now_time)
        }
        return ""
    }

    @SuppressLint("SimpleDateFormat")
    private fun getTime(t: String): String {
        if (t == "24") {
            return getPeriod() + SimpleDateFormat("HH:mm").format(now_time)
        } else {
            return getPeriod() + SimpleDateFormat("h:mm").format(now_time)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getWeek(): String {
        var week = ""
        if (XSPUtils.getBoolean("status_bar_time_week", false)) {
            week = SimpleDateFormat("E").format(now_time)
            if (XSPUtils.getBoolean("status_bar_time_hide_space", false)) {
                week += ""
            } else {
                week += " "
            }
        }
        return week
    }

    @SuppressLint("SimpleDateFormat")
    private fun getPeriod(): String {
        var period = ""
        if (XSPUtils.getBoolean("status_bar_time_period", true)) {
            var now = SimpleDateFormat("HH").format(now_time)
            when (now) {
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
        }
        return period
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDoubleHour(): String {
        var doubleHour = ""
        if (XSPUtils.getBoolean("status_bar_time_double_hour", false)) {
            var now = SimpleDateFormat("HH").format(now_time)

            when (now) {
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

            if (XSPUtils.getBoolean("status_bar_time_hide_space", false)) {
                doubleHour += ""
            } else {
                doubleHour += " "
            }

        }
        return doubleHour
    }

    private fun getDateSpace(): String {
        if (XSPUtils.getBoolean("status_bar_time_year", false) ||
            XSPUtils.getBoolean("status_bar_time_month", false) ||
            XSPUtils.getBoolean("status_bar_time_day", false)
        ) {
            if (XSPUtils.getBoolean("status_bar_time_hide_space", false)) {
                return ""
            } else {
                return " "
            }
        }

        return ""
    }

}