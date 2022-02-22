package com.lt2333.simplicitytools.hook.app.systemui

import android.view.View
import android.widget.FrameLayout
import com.lt2333.simplicitytools.util.XSPUtils
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class HideBatteryIcon :IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val classIfExists = XposedHelpers.findClassIfExists(
            "com.android.systemui.statusbar.views.MiuiBatteryMeterView",
            lpparam.classLoader
        )
        XposedHelpers.findAndHookMethod(
            classIfExists,
            "updateResources",
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    if (XSPUtils.getBoolean("hide_battery_icon", false)) {
                        val mBatteryIconView = XposedHelpers.getObjectField(
                            param.thisObject,
                            "mBatteryDigitalView"
                        ) as FrameLayout
                        mBatteryIconView.visibility = View.GONE
                    }
                }
            })
    }
}