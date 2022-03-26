package com.lt2333.simplicitytools.hook.app.systemui

import android.view.View
import android.widget.ImageView
import com.lt2333.simplicitytools.util.getObjectField
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookAfterMethod
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object HideHDIcon: HookRegister() {

    override fun init() {
        val iconState = "com.android.systemui.statusbar.phone.StatusBarSignalPolicy\$MobileIconState"
        "com.android.systemui.statusbar.StatusBarMobileView".hookAfterMethod(getDefaultClassLoader(), "initViewState", iconState) {
            hasEnable("hide_big_hd_icon") {
                (it.thisObject.getObjectField("mVolte") as ImageView).visibility = View.GONE
            }
            hasEnable("hide_small_hd_icon") {
                (it.thisObject.getObjectField("mSmallHd") as ImageView).visibility = View.GONE
            }
            hasEnable("hide_hd_no_service_icon") {
                (it.thisObject.getObjectField("mVolteNoService") as ImageView).visibility = View.GONE
            }
        }

        "com.android.systemui.statusbar.StatusBarMobileView".hookAfterMethod(getDefaultClassLoader(), "updateState", iconState) {
            hasEnable("hide_big_hd_icon") {
                (it.thisObject.getObjectField("mVolte") as ImageView).visibility = View.GONE
            }
            hasEnable("hide_small_hd_icon") {
                (it.thisObject.getObjectField("mSmallHd") as ImageView).visibility = View.GONE
            }
            hasEnable("hide_hd_no_service_icon") {
                (it.thisObject.getObjectField("mVolteNoService") as ImageView).visibility = View.GONE
            }
        }
    }

}