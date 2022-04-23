package com.lt2333.simplicitytools.hook.app.securitycenter

import android.app.AndroidAppHelper
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Typeface
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import cn.fkj233.ui.activity.dp2px
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.github.kyuubiran.ezxhelper.utils.loadClass
import com.lt2333.simplicitytools.util.*
import com.lt2333.simplicitytools.util.xposed.base.HookRegister
import java.lang.reflect.Field

object ShowBatteryTemperature : HookRegister() {

    private fun getBatteryTemperature(context: Context): Int {
        return context.registerReceiver(
            null as BroadcastReceiver?,
            IntentFilter("android.intent.action.BATTERY_CHANGED")
        )!!.getIntExtra("temperature", 0) / 10
    }

    override fun init() = hasEnable("battery_life_function") {
        findMethod("com.miui.powercenter.a") {
            name == "b" && parameterTypes[0] == Context::class.java
        }.hookBefore {
            it.result = getBatteryTemperature(it.args[0] as Context).toString()

        }

        findMethod("com.miui.powercenter.a\$a") {
            name == "run"
        }.hookAfter {
            val context = AndroidAppHelper.currentApplication().applicationContext
            val isDarkMode =
                context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
            val currentTemperatureValue = context.resources.getIdentifier(
                "current_temperature_value",
                "id",
                "com.miui.securitycenter"
            )
            val field = loadClass("com.miui.powercenter.a\$a").getDeclaredField("a") as Field
            field.isAccessible = true
            val view = field.get(it.thisObject) as View
            val textView = view.findViewById<TextView>(currentTemperatureValue)
            (textView.layoutParams as LinearLayout.LayoutParams).marginStart = dp2px(context, 25f)
            (textView.layoutParams as LinearLayout.LayoutParams).topMargin = 0
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 36.399998f)
            textView.setPadding(0, 0, 0, 0)
            textView.gravity = Gravity.NO_GRAVITY
            textView.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
            textView.height = dp2px(context, 49.099983f)
            textView.textAlignment = View.TEXT_ALIGNMENT_VIEW_START
            val tempView = TextView(context)
            tempView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp2px(context, 49.099983f)
            )
            (tempView.layoutParams as LinearLayout.LayoutParams).marginStart =
                dp2px(context, 3.599976f)
            tempView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13.099977f)
            tempView.setTextColor(Color.parseColor(if (isDarkMode) "#e6e6e6" else "#333333"))
            tempView.setPadding(0, dp2px(context, 25f), 0, 0)
            tempView.text = "℃"
            tempView.typeface = Typeface.create(null, 500, false)
            tempView.textAlignment = View.TEXT_ALIGNMENT_VIEW_START
            val tempeValueContainer = context.resources.getIdentifier(
                "tempe_value_container",
                "id",
                "com.miui.securitycenter"
            )
            val linearLayout = view.findViewById<LinearLayout>(tempeValueContainer)
            linearLayout.addView(tempView)
        }
    }
}