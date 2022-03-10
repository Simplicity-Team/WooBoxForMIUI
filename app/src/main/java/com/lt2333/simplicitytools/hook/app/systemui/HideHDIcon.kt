package com.lt2333.simplicitytools.hook.app.systemui

import android.view.View
import android.widget.ImageView
import com.lt2333.simplicitytools.util.getObjectField
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookAfterMethod
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

class HideHDIcon :IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val iconState = "com.android.systemui.statusbar.phone.StatusBarSignalPolicy\$MobileIconState"
        "com.android.systemui.statusbar.StatusBarMobileView".hookAfterMethod(lpparam.classLoader, "initViewState", iconState) {
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

        "com.android.systemui.statusbar.StatusBarMobileView".hookAfterMethod(lpparam.classLoader, "updateState", iconState) {
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