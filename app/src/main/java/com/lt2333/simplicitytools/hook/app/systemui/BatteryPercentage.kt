package com.lt2333.simplicitytools.hook.app.systemui

import android.util.TypedValue
import android.widget.TextView
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.getObject
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.lt2333.simplicitytools.util.XSPUtils
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object BatteryPercentage : HookRegister() {
    override fun init() {
        val size = XSPUtils.getFloat("battery_percentage_font_size", 0f)
        if (size == 0f) return
        findMethod("com.android.systemui.statusbar.views.MiuiBatteryMeterView") {
            name == "updateResources"
        }.hookAfter {
            (it.thisObject.getObject("mBatteryPercentView") as TextView).setTextSize(
                TypedValue.COMPLEX_UNIT_DIP, size
            )
        }
    }
}