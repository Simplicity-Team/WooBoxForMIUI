package com.lt2333.simplicitytools.hook.app.systemui

import android.util.TypedValue
import android.widget.TextView
import com.lt2333.simplicitytools.util.XSPUtils
import com.lt2333.simplicitytools.util.getObjectField
import com.lt2333.simplicitytools.util.hookAfterMethod
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

class BatteryPercentage : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val size = XSPUtils.getFloat("battery_percentage_font_size", 0f)
        if (size == 0f) return
        "com.android.systemui.statusbar.views.MiuiBatteryMeterView".hookAfterMethod(
            lpparam.classLoader,
            "updateResources"
        ) {
            (it.thisObject.getObjectField("mBatteryPercentView") as TextView).setTextSize(
                TypedValue.COMPLEX_UNIT_DIP, size
            )
        }
    }
}