package com.lt2333.simplicitytools.hook.app.systemui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.lt2333.simplicitytools.util.getObjectField
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookAfterMethod
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object HideWifiActivityIcon: HookRegister() {

    override fun init() {
        val iconState = "com.android.systemui.statusbar.phone.StatusBarSignalPolicy\$WifiIconState"
        "com.android.systemui.statusbar.StatusBarWifiView".hookAfterMethod(getDefaultClassLoader(), "initViewState", iconState) {
            //隐藏WIFI箭头
            hasEnable("hide_wifi_activity_icon") {
                (it.thisObject.getObjectField("mWifiActivityView") as ImageView).visibility = View.INVISIBLE
            }
            //隐藏WIFI标准图标
            hasEnable("hide_wifi_standard_icon") {
                (it.thisObject.getObjectField("mWifiStandardView") as TextView).visibility = View.INVISIBLE
            }
        }

        "com.android.systemui.statusbar.StatusBarWifiView".hookAfterMethod(getDefaultClassLoader(), "updateState", iconState) {
            //隐藏WIFI箭头
            hasEnable("hide_wifi_activity_icon") {
                (it.thisObject.getObjectField("mWifiActivityView") as ImageView).visibility = View.INVISIBLE
            }
            //隐藏WIFI标准图标
            hasEnable("hide_wifi_standard_icon") {
                (it.thisObject.getObjectField("mWifiStandardView") as TextView).visibility = View.INVISIBLE
            }
        }
    }

}