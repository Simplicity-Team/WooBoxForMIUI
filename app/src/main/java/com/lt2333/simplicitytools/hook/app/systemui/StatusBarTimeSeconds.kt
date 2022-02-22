package com.lt2333.simplicitytools.hook.app.systemui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.provider.Settings
import android.util.AttributeSet
import android.widget.TextView
import com.lt2333.simplicitytools.util.XSPUtils
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.lang.reflect.Method
import java.text.SimpleDateFormat
import java.util.*

class StatusBarTimeSeconds : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {

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
                    c = param.args[0] as Context
                    val textV = param.thisObject as TextView
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
                }
            }
        )
        XposedHelpers.findAndHookMethod(classIfExists, "updateTime",
            object : XC_MethodHook() {
                @SuppressLint("SetTextI18n", "SimpleDateFormat")
                override fun afterHookedMethod(param: MethodHookParam) {
                    val textV = param.thisObject as TextView
                    if (textV.resources.getResourceEntryName(textV.id) == "clock") {
                        val t = Settings.System.getString(
                            c!!.contentResolver,
                            Settings.System.TIME_12_24
                        )
                        if (t == "24")
                            textV.text =
                                getYear() + getMonth() + getDay() + getDateSpace() + getWeek() + SimpleDateFormat(
                                    "HH:mm"
                                ).format(Calendar.getInstance().time)+getSecond()
                        else
                            textV.text =
                                getYear() + getMonth() + getDay() + getDateSpace() + getWeek() +textV.text.toString() + getSecond()
                    }
                }
            })


    }

    @SuppressLint("SimpleDateFormat")
    private fun getYear(): String {
        if (XSPUtils.getBoolean("status_bar_time_year", false)) {
            return SimpleDateFormat("YY年").format(Calendar.getInstance().time)
        }
        return ""
    }

    @SuppressLint("SimpleDateFormat")
    private fun getMonth(): String {
        if (XSPUtils.getBoolean("status_bar_time_month", false)) {
            return SimpleDateFormat("M月").format(Calendar.getInstance().time)
        }
        return ""
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDay(): String {
        if (XSPUtils.getBoolean("status_bar_time_day", false)) {
            return SimpleDateFormat("d日").format(Calendar.getInstance().time)
        }
        return ""
    }

    @SuppressLint("SimpleDateFormat")
    private fun getSecond(): String {
        if (XSPUtils.getBoolean("status_bar_time_seconds", false)) {
            return SimpleDateFormat(":ss").format(Calendar.getInstance().time)
        }
        return ""
    }

    @SuppressLint("SimpleDateFormat")
    private fun getWeek(): String {
        var week = ""
        if (XSPUtils.getBoolean("status_bar_time_week", false)) {
            week = SimpleDateFormat("E").format(Calendar.getInstance().time)
            if (XSPUtils.getBoolean("status_bar_time_hide_space", false)) {
                week += ""
            } else {
                week += " "
            }
        }
        return week
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