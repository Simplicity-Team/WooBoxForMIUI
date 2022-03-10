package com.lt2333.simplicitytools.hook.app.systemui

import android.view.View
import android.widget.ImageView
import com.lt2333.simplicitytools.util.getObjectField
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookAfterMethod
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

class HideMobileActivityIcon : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val iconState = "com.android.systemui.statusbar.phone.StatusBarSignalPolicy\$MobileIconState"
        "com.android.systemui.statusbar.StatusBarMobileView".hookAfterMethod(lpparam.classLoader, "initViewState", iconState) {
            hasEnable("hide_mobile_activity_icon") {
                (it.thisObject.getObjectField("mLeftInOut") as ImageView).visibility = View.GONE
                (it.thisObject.getObjectField("mRightInOut") as ImageView).visibility = View.GONE
            }
        }

        "com.android.systemui.statusbar.StatusBarMobileView".hookAfterMethod(lpparam.classLoader, "updateState", iconState) {
            hasEnable("hide_mobile_activity_icon") {
                (it.thisObject.getObjectField("mLeftInOut") as ImageView).visibility = View.GONE
                (it.thisObject.getObjectField("mRightInOut") as ImageView).visibility = View.GONE
            }
        }
    }
}