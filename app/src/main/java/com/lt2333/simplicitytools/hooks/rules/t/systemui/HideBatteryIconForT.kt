package com.lt2333.simplicitytools.hooks.rules.t.systemui

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.lt2333.simplicitytools.utils.hasEnable

object HideBatteryIconForT : YukiBaseHooker() {
    override fun onHook() {

        "com.android.systemui.statusbar.views.MiuiBatteryMeterView".hook {
            injectMember {
                method { name = "updateResources" }
                afterHook {
                    //隐藏电池图标
                    hasEnable("hide_battery_icon") {
                        instance.current().field { name = "mBatteryStyle" }.cast<ImageView>()?.visibility = View.GONE
                        if (instance.current().field { name = "mBatteryStyle" }.int() == 1) {
                            instance.current().field { name = "mBatteryDigitalView" }.cast<FrameLayout>()?.visibility = View.GONE
                        }
                    }
                    //隐藏电池内的百分比
                    hasEnable("hide_battery_percentage_icon") {
                        instance.current().field { name = "mBatteryPercentMarkView" }.cast<TextView>()?.textSize=0F
                    }

                    "com.android.systemui.statusbar.views.MiuiBatteryMeterView".hook {
                        injectMember {
                            method {
                                name = "updateChargeAndText"
                            }
                            afterHook {
                                //隐藏电池充电图标
                                hasEnable("hide_battery_charging_icon") {
                                    instance.current().field { name = "mBatteryChargingInView" }.cast<ImageView>()?.visibility = View.GONE
                                    instance.current().field { name = "mBatteryChargingView" }.cast<ImageView>()?.visibility = View.GONE
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}