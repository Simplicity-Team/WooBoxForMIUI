package com.lt2333.simplicitytools.hook.app.systemui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.provider.Settings
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import com.lt2333.simplicitytools.util.*
import com.lt2333.simplicitytools.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import java.lang.reflect.Method
import java.text.SimpleDateFormat
import java.util.*

object LockScreenClockDisplaySeconds : HookRegister() {

    private var nowTime: Date = Calendar.getInstance().time
    private var isVertical: Boolean = false

    override fun init() {
        hasEnable("lock_screen_clock_display_seconds") {
            "com.miui.clock.MiuiBaseClock".findClass(getDefaultClassLoader())
                .hookAfterConstructor(
                    Context::class.java,
                    AttributeSet::class.java
                ) {
                    try {
                        val viewGroup = it.thisObject as LinearLayout

                        val d: Method = viewGroup.javaClass.getDeclaredMethod("updateTime")
                        val r = Runnable {
                            d.isAccessible = true
                            d.invoke(viewGroup)
                        }

                        class T : TimerTask() {
                            override fun run() {
                                Handler(viewGroup.context.mainLooper).post(r)
                            }
                        }
                        Timer().scheduleAtFixedRate(
                            T(),
                            1000 - System.currentTimeMillis() % 1000,
                            1000
                        )
                    } catch (e: java.lang.Exception) {
                    }
                }
            "com.miui.clock.MiuiLeftTopClock".hookAfterMethod(
                getDefaultClassLoader(),
                "updateTime"
            ) {
                updateTime(it)
                isVertical = false
            }

            "com.miui.clock.MiuiLeftTopLargeClock".hookAfterMethod(
                getDefaultClassLoader(),
                "updateTime"
            ) {
                updateTime(it)
                isVertical = false
            }

            "com.miui.clock.MiuiCenterHorizontalClock".hookAfterMethod(
                getDefaultClassLoader(),
                "updateTime"
            ) {
                updateTime(it)
                isVertical = false
            }

            "com.miui.clock.MiuiVerticalClock".hookAfterMethod(
                getDefaultClassLoader(),
                "updateTime"
            ) {
                updateTime(it)
                isVertical = true
            }
        }
    }

    private fun updateTime(it: XC_MethodHook.MethodHookParam) {
        val textV = it.thisObject.getObjectField("mTimeText") as TextView
        val c: Context = textV.context

        Log.d(
            "lock_screen_clock_display_seconds",
            "updateTime: " + it.thisObject.javaClass.simpleName
        )
        val is24 = Settings.System.getString(
            c.contentResolver,
            Settings.System.TIME_12_24
        ) == "24"

        nowTime = Calendar.getInstance().time

        textV.text = getTime(c, is24)

    }


    @SuppressLint("SimpleDateFormat")
    private fun getTime(context: Context, is24: Boolean): String {
        var timePattern = ""
        timePattern += if (is24) if (isVertical) "HH\n" else "HH" else if (isVertical) "h\n" else "h"
        timePattern += if (isVertical) "mm\n" else ":mm"
        timePattern += if (isVertical) "ss" else ":ss"

        timePattern = SimpleDateFormat(timePattern).format(nowTime)
        return timePattern
    }
}