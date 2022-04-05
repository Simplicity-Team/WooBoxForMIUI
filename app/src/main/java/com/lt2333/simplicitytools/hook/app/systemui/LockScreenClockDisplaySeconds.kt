package com.lt2333.simplicitytools.hook.app.systemui

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import com.lt2333.simplicitytools.util.getObjectField
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookAfterMethod
import com.lt2333.simplicitytools.util.hookBeforeMethod
import com.lt2333.simplicitytools.util.xposed.base.HookRegister
import java.text.SimpleDateFormat
import java.util.*

object LockScreenClockDisplaySeconds : HookRegister() {

    private var nowTime: Date = Calendar.getInstance().time
    override fun init() {
        hasEnable("lock_screen_clock_display_seconds") {
            "com.miui.clock.MiuiLeftTopClock".hookAfterMethod(
                getDefaultClassLoader(),
                "updateTime"
            ) {
                val textV = it.thisObject.getObjectField("mTimeText") as TextView
                val c: Context = textV.context

                val t = Settings.System.getString(
                    c.contentResolver,
                    Settings.System.TIME_12_24
                )
                val is24 = t == "24"
                nowTime = Calendar.getInstance().time
                textV.text = getTime(c, is24)
                Log.d("lock_screen_clock_display_seconds", textV.text.toString())
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getTime(context: Context, t: Boolean): String {
        var timePattern = ""
        timePattern += if (t) "HH:mm" else "h:mm"
        timePattern += ":ss"
        timePattern = SimpleDateFormat(timePattern).format(nowTime)
        return timePattern
    }
}