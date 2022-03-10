package com.lt2333.simplicitytools.hook.app.systemui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.lt2333.simplicitytools.util.getObjectField
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookAfterMethod
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

class HideMobileTypeIcon : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val iconState = "com.android.systemui.statusbar.phone.StatusBarSignalPolicy\$MobileIconState"
        "com.android.systemui.statusbar.StatusBarMobileView".hookAfterMethod(lpparam.classLoader, "initViewState", iconState) {
            hasEnable("hide_mobile_type_icon") {
                (it.thisObject.getObjectField("mMobileType") as TextView).visibility = View.INVISIBLE
                (it.thisObject.getObjectField("mMobileTypeImage") as ImageView).visibility = View.INVISIBLE
                (it.thisObject.getObjectField("mMobileTypeSingle") as TextView).visibility = View.INVISIBLE
            }
        }

        "com.android.systemui.statusbar.StatusBarMobileView".hookAfterMethod(lpparam.classLoader, "updateState", iconState) {
            hasEnable("hide_mobile_type_icon") {
                (it.thisObject.getObjectField("mMobileType") as TextView).visibility = View.INVISIBLE
                (it.thisObject.getObjectField("mMobileTypeImage") as ImageView).visibility = View.INVISIBLE
                (it.thisObject.getObjectField("mMobileTypeSingle") as TextView).visibility = View.INVISIBLE
            }
        }
    }
}