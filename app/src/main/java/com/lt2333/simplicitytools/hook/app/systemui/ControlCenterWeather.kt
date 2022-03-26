package com.lt2333.simplicitytools.hook.app.systemui

import android.content.ComponentName
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import cn.fkj233.ui.activity.dp2px
import com.github.kyuubiran.ezxhelper.utils.loadClass
import com.lt2333.simplicitytools.util.*
import com.lt2333.simplicitytools.util.xposed.base.HookRegister
import com.lt2333.simplicitytools.view.WeatherView

object ControlCenterWeather: HookRegister() {

    override fun init() {
        hasEnable("control_center_weather") {
            var mWeatherView: TextView? = null
            var mConstraintLayout: ConstraintLayout? = null
            val isDisplayCity = XSPUtils.getBoolean("control_center_weather_city", false)
            "com.android.systemui.controlcenter.phone.widget.QSControlCenterHeaderView".hookAfterMethod(
                getDefaultClassLoader(),
                "onFinishInflate"
            ) {
                val viewGroup = it.thisObject as ViewGroup
                val context = viewGroup.context

                // MIUI编译时间大于 2022-03-12 00:00:00 且为内测版
                if (SystemProperties.get(context, "ro.build.date.utc")!!
                        .toInt() >= 1647014400 &&

                    !SystemProperties.get(
                        context,
                        "ro.build.version.incremental"
                    )!!.endsWith("DEV") &&

                    !SystemProperties.get(
                        context,
                        "ro.build.version.incremental"
                    )!!.endsWith("XM")
                ) {
                    //获取原组件
                    val big_time_ID =
                        context.resources.getIdentifier("big_time", "id", context.packageName)
                    val big_time: TextView = viewGroup.findViewById(big_time_ID)

                    val date_time_ID =
                        context.resources.getIdentifier("date_time", "id", context.packageName)
                    val date_time: TextView = viewGroup.findViewById(date_time_ID)

                    //创建新布局
                    val mConstraintLayoutLp = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).also {
                        it.topMargin = context.resources.getDimensionPixelSize(
                            context.resources.getIdentifier(
                                "qs_control_header_tiles_margin_top",
                                "dimen",
                                context.packageName
                            )
                        )
                    }

                    mConstraintLayout =
                        ConstraintLayout(context).also { it.layoutParams = mConstraintLayoutLp }

                    (big_time.parent as ViewGroup).addView(mConstraintLayout, 0)


                    //从原布局中删除组件
                    (big_time.parent as ViewGroup).removeView(big_time)
                    (date_time.parent as ViewGroup).removeView(date_time)


                    //添加组件至新布局
                    mConstraintLayout!!.addView(big_time)
                    mConstraintLayout!!.addView(date_time)

                    //组件属性

                    val date_time_LP = ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                    ).also {
                        it.startToEnd = big_time_ID
                        it.bottomToBottom = 0
                        it.marginStart = context.resources.getDimensionPixelSize(
                            context.resources.getIdentifier(
                                "notification_panel_time_date_space",
                                "dimen",
                                context.packageName
                            )
                        )
                        it.bottomMargin = dp2px(context, 5f)
                    }
                    date_time.layoutParams = date_time_LP


                    //创建天气组件
                    mWeatherView = WeatherView(context, isDisplayCity).apply {
                        setTextAppearance(
                            context.resources.getIdentifier(
                                "TextAppearance.QSControl.Date",
                                "style",
                                context.packageName
                            )
                        )

                    }
                    mConstraintLayout!!.addView(mWeatherView)

                    val mWeatherView_LP = ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                    ).also {
                        it.startToEnd = big_time_ID
                        it.bottomToTop = date_time_ID
                        it.marginStart = context.resources.getDimensionPixelSize(
                            context.resources.getIdentifier(
                                "notification_panel_time_date_space",
                                "dimen",
                                context.packageName
                            )
                        )
                    }

                    (mWeatherView as WeatherView).layoutParams = mWeatherView_LP

                } else {
                    val layoutParam =
                        loadClass("androidx.constraintlayout.widget.ConstraintLayout\$LayoutParams").getConstructor(
                            Int::class.java,
                            Int::class.java
                        ).newInstance(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        ) as ViewGroup.MarginLayoutParams
                    layoutParam.setObjectField(
                        "bottomToTop",
                        context.resources.getIdentifier("date_time", "id", context.packageName)
                    )
                    layoutParam.setObjectField(
                        "startToEnd",
                        context.resources.getIdentifier("big_time", "id", context.packageName)
                    )
                    layoutParam.marginStart = context.resources.getDimensionPixelSize(
                        context.resources.getIdentifier(
                            "notification_panel_time_date_space",
                            "dimen",
                            context.packageName
                        )
                    )
                    mWeatherView = WeatherView(context, isDisplayCity).apply {
                        setTextAppearance(
                            context.resources.getIdentifier(
                                "TextAppearance.QSControl.Date",
                                "style",
                                context.packageName
                            )
                        )
                        layoutParams = layoutParam
                    }
                    viewGroup.addView(mWeatherView)
                }

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
                        Toast.makeText(context, "启动失败", Toast.LENGTH_LONG).show()
                    }
                }

            }
            //解决横屏重叠
            "com.android.systemui.controlcenter.phone.widget.QSControlCenterHeaderView".hookAfterMethod(
                getDefaultClassLoader(),
                "updateLayout"
            ) {
                val viewGroup = it.thisObject as ViewGroup
                val context = viewGroup.context
                val mOrientation = viewGroup.getObjectField("mOrientation") as Int
                // MIUI编译时间大于 2022-03-12 00:00:00 且为内测版
                if (SystemProperties.get(context, "ro.build.date.utc")!!
                        .toInt() >= 1647014400 &&

                    !SystemProperties.get(
                        context,
                        "ro.build.version.incremental"
                    )!!.endsWith("DEV") &&

                    !SystemProperties.get(
                        context,
                        "ro.build.version.incremental"
                    )!!.endsWith("XM")
                ) {
                    if (mOrientation == 1) {
                        mConstraintLayout!!.visibility = View.VISIBLE
                    } else {
                        mConstraintLayout!!.visibility = View.GONE
                    }
                } else {
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
