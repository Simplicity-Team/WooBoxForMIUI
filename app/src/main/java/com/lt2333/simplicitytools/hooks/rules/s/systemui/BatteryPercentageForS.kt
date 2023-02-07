package com.lt2333.simplicitytools.hooks.rules.s.systemui

import android.util.TypedValue
import android.widget.TextView
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.getObjectAs
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.lt2333.simplicitytools.utils.XSPUtils
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister

object BatteryPercentageForS : HookRegister() {

    override fun init() {
        val size = XSPUtils.getFloat("battery_percentage_font_size", 0f)
        if (size == 0f) return
        findMethod("com.android.systemui.statusbar.views.MiuiBatteryMeterView") {
            name == "updateResources"
        }.hookAfter {
            (it.thisObject.getObjectAs<TextView>("mBatteryPercentView")).setTextSize(
                TypedValue.COMPLEX_UNIT_DIP, size
            )
        }
    }

}