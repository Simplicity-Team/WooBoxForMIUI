package com.lt2333.simplicitytools.hook.app.systemui

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.lt2333.simplicitytools.util.XSPUtils
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class HideBatteryIcon : IXposedHookLoadPackage {
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

                    //隐藏电池图标
                    if (XSPUtils.getBoolean("hide_battery_icon", false)) {
                        val mBatteryIconView = XposedHelpers.getObjectField(
                            param.thisObject,
                            "mBatteryIconView"
                        ) as ImageView
                        mBatteryIconView.visibility = View.GONE

                        if (XposedHelpers.getObjectField(param.thisObject, "mBatteryStyle") == 1) {
                            (XposedHelpers.getObjectField(
                                param.thisObject,
                                "mBatteryDigitalView"
                            ) as FrameLayout).visibility = View.GONE
                        }

                    }
                    //隐藏电池内的百分比
                    if (XSPUtils.getBoolean("hide_battery_percentage_icon", false)) {
                        val mBatteryPercentMarkView = XposedHelpers.getObjectField(
                            param.thisObject,
                            "mBatteryPercentMarkView"
                        ) as TextView
                        mBatteryPercentMarkView.textSize = 0F
                    }

                    //隐藏电池百分号
                    if (XSPUtils.getBoolean("hide_battery_percentage_icon", false)) {
                        val mBatteryPercentMarkView = XposedHelpers.getObjectField(
                            param.thisObject,
                            "mBatteryPercentMarkView"
                        ) as TextView
                        mBatteryPercentMarkView.textSize = 0F
                    }




                    XposedHelpers.findAndHookMethod(classIfExists,"updateChargeAndText",object :XC_MethodHook(){
                        override fun afterHookedMethod(param: MethodHookParam) {
                            //隐藏电池充电图标
                            if (XSPUtils.getBoolean("hide_battery_charging_icon", false)) {
                                (XposedHelpers.getObjectField(
                                    param.thisObject,
                                    "mBatteryChargingInView"
                                ) as ImageView).visibility = View.GONE

                                (XposedHelpers.getObjectField(
                                    param.thisObject,
                                    "mBatteryChargingView"
                                ) as ImageView).visibility = View.GONE

                            }
                        }
                    })

                }
            })


    }
}