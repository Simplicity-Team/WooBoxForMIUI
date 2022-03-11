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
import com.lt2333.simplicitytools.util.*
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.lang.reflect.Field

class ShowBatteryTemperature: IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (!XSPUtils.getBoolean("battery_life_function", false)) return
        val a = "com.miui.powercenter.a\$a".findClass(lpparam.classLoader)
        "com.miui.powercenter.a".hookBeforeMethod(lpparam.classLoader, "b", Context::class.java) {
           it.result = getBatteryTemperature(it.args[0] as Context).toString()
        }
        a.hookAfterMethod("run") {
            val context = AndroidAppHelper.currentApplication().applicationContext
            val isDarkMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
            val currentTemperatureValue = context.resources.getIdentifier("current_temperature_value", "id", "com.miui.securitycenter")
            val field = a.getDeclaredField("a") as Field
            field.isAccessible = true
            val view = field.get(it.thisObject) as View
            val textView = view.findViewById<TextView>(currentTemperatureValue)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 36.399998f)
            textView.gravity = Gravity.NO_GRAVITY
            textView.setPadding(0, 0, 0, 0)
            textView.height = dp2px(context, 49.099983f)
            textView.textAlignment = View.TEXT_ALIGNMENT_VIEW_START
            (textView.layoutParams as LinearLayout.LayoutParams).marginStart = dp2px(context, 25f)
            (textView.layoutParams as LinearLayout.LayoutParams).topMargin = 0
            val tempView = TextView(context)
            tempView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp2px(context, 49.099983f))
            (tempView.layoutParams as LinearLayout.LayoutParams).marginStart = dp2px(context, 3.599976f)
            tempView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13.099977f)
            tempView.setPadding(0, dp2px(context, 25f), 0, 0)
            tempView.text = "℃"
            tempView.setTextColor(Color.parseColor(if (isDarkMode) "#e6e6e6" else "#333333"))
            tempView.typeface = Typeface.create(null, 500, false)
            tempView.textAlignment = View.TEXT_ALIGNMENT_VIEW_START
            val tempeValueContainer = context.resources.getIdentifier("tempe_value_container", "id", "com.miui.securitycenter")
            val linearLayout = view.findViewById<LinearLayout>(tempeValueContainer)
            linearLayout.addView(tempView)
        }
    }
    private fun getBatteryTemperature(context: Context): Int {
        return context.registerReceiver(
            null as BroadcastReceiver?,
            IntentFilter("android.intent.action.BATTERY_CHANGED")
        )!!.getIntExtra("temperature", 0) / 10
    }
}