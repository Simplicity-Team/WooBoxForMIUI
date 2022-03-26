package com.lt2333.simplicitytools.hook.app.systemui

import android.util.TypedValue
import android.widget.TextView
import com.lt2333.simplicitytools.util.XSPUtils
import com.lt2333.simplicitytools.util.getObjectField
import com.lt2333.simplicitytools.util.hookAfterMethod
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object BatteryPercentage: HookRegister() {

    override fun init() {
        val size = XSPUtils.getFloat("battery_percentage_font_size", 0f)
        if (size == 0f) return
        "com.android.systemui.statusbar.views.MiuiBatteryMeterView".hookAfterMethod(
            getDefaultClassLoader(),
            "updateResources"
        ) {
            (it.thisObject.getObjectField("mBatteryPercentView") as TextView).setTextSize(
                TypedValue.COMPLEX_UNIT_DIP, size
            )
        }
    }

}