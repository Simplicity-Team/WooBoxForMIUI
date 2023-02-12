package com.lt2333.simplicitytools.hooks.rules.all.reardisplay

import android.graphics.Typeface
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import cn.fkj233.ui.activity.dp2px
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister
import com.lt2333.simplicitytools.views.WeatherView

object RearDisplayWeatherForAll : HookRegister() {

    override fun init() = hasEnable("rear_show_weather") {
        findMethod("com.xiaomi.misubscreenui.light.aod.view.VerticalClockView") {
            name == "onFinishInflate"
        }.hookAfter {
            val viewGroup = it.thisObject as ViewGroup
            val context = viewGroup.context

            //获取原组件
            val gradientLayoutID =
                context.resources.getIdentifier("gradient_layout", "id", context.packageName)
            val gradientLayout: ViewGroup = viewGroup.findViewById(gradientLayoutID)
            val dateID = context.resources.getIdentifier("date", "id", context.packageName)
            val date: TextView = viewGroup.findViewById(dateID)

            //创建天气组件
            val mWeatherView = WeatherView(context, false).apply {
                gravity = Gravity.END
                setTextAppearance(
                    context.resources.getIdentifier(
                        "Sub_Screen_Aod_date_s",
                        "style",
                        context.packageName
                    )
                )
                typeface = Typeface.create("mipro-medium", Typeface.NORMAL)
                setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12f)
            }
            gradientLayout.addView(mWeatherView, (gradientLayout.indexOfChild(date) + 1))
        }

        findMethod("com.xiaomi.misubscreenui.light.aod.view.HorizontalClockView") {
            name == "onFinishInflate"
        }.hookBefore { param ->
            val viewGroup = param.thisObject as ViewGroup
            val context = viewGroup.context

            //获取原组件
            val gradientLayoutID =
                context.resources.getIdentifier("gradient_layout", "id", context.packageName)
            val gradientLayout: RelativeLayout = viewGroup.findViewById(gradientLayoutID)
            val dateID = context.resources.getIdentifier("date", "id", context.packageName)
            val date: TextView = viewGroup.findViewById(dateID)
            val dateParentLayout: ViewGroup = date.parent as ViewGroup

            //创建天气组件
            val mWeatherView = WeatherView(context, false).apply {
                id = View.generateViewId()
                gravity = Gravity.CENTER
                maxLines = 1
                setTextAppearance(
                    context.resources.getIdentifier(
                        "Sub_Screen_Aod_date_s_horizontal",
                        "style",
                        context.packageName
                    )
                )
                typeface = Typeface.create("mipro-medium", Typeface.NORMAL)
                layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    dp2px(context, 30f)
                ).also {
                    it.addRule(RelativeLayout.END_OF, dateID)
                    it.marginStart = dp2px(context, 3f)
                    it.marginEnd = dp2px(context, 3f)
                }
            }
            dateParentLayout.addView(mWeatherView, (dateParentLayout.indexOfChild(date) + 1))

            //电池位置
            val batteryID =
                context.resources.getIdentifier("battery_container", "id", context.packageName)
            val battery: ViewGroup = viewGroup.findViewById(batteryID)

            battery.layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                dp2px(context, 30f)
            ).also {
                it.addRule(RelativeLayout.END_OF, mWeatherView.id)
            }
            gradientLayout.setPadding(0, dp2px(context, 5f), 0, 0)
        }
    }
}