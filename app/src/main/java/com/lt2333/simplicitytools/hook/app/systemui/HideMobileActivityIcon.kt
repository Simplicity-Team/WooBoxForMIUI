package com.lt2333.simplicitytools.hook.app.systemui

import android.view.View
import android.widget.ImageView
import com.lt2333.simplicitytools.util.getObjectField
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookAfterMethod
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object HideMobileActivityIcon: HookRegister() {

    override fun init() {
        val iconState = "com.android.systemui.statusbar.phone.StatusBarSignalPolicy\$MobileIconState"
        "com.android.systemui.statusbar.StatusBarMobileView".hookAfterMethod(getDefaultClassLoader(), "initViewState", iconState) {
            hasEnable("hide_mobile_activity_icon") {
                (it.thisObject.getObjectField("mLeftInOut") as ImageView).visibility = View.GONE
                (it.thisObject.getObjectField("mRightInOut") as ImageView).visibility = View.GONE
            }
        }

        "com.android.systemui.statusbar.StatusBarMobileView".hookAfterMethod(getDefaultClassLoader(), "updateState", iconState) {
            hasEnable("hide_mobile_activity_icon") {
                (it.thisObject.getObjectField("mLeftInOut") as ImageView).visibility = View.GONE
                (it.thisObject.getObjectField("mRightInOut") as ImageView).visibility = View.GONE
            }
        }
    }

}