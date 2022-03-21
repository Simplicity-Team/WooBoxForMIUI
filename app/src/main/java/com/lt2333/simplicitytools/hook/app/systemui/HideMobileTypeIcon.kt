package com.lt2333.simplicitytools.hook.app.systemui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.lt2333.simplicitytools.util.XSPUtils
import com.lt2333.simplicitytools.util.getObjectField
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookAfterMethod
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.callbacks.XC_LoadPackage

class HideMobileTypeIcon : IXposedHookLoadPackage {
    val isBigType=XSPUtils.getBoolean("big_mobile_type_icon", false)
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val iconState =
            "com.android.systemui.statusbar.phone.StatusBarSignalPolicy\$MobileIconState"
        "com.android.systemui.statusbar.StatusBarMobileView".hookAfterMethod(
            lpparam.classLoader,
            "initViewState",
            iconState
        ) {
            hideMobileTypeIcon(it)
        }

        "com.android.systemui.statusbar.StatusBarMobileView".hookAfterMethod(
            lpparam.classLoader,
            "updateState",
            iconState
        ) {
            hideMobileTypeIcon(it)
        }
    }

    fun hideMobileTypeIcon(it: XC_MethodHook.MethodHookParam) {
        hasEnable("hide_mobile_type_icon") {
            if (isBigType) {
                (it.thisObject.getObjectField("mMobileType") as TextView).visibility =
                    View.GONE
                (it.thisObject.getObjectField("mMobileTypeImage") as ImageView).visibility =
                    View.GONE
                (it.thisObject.getObjectField("mMobileTypeSingle") as TextView).visibility =
                    View.GONE
            } else {
                (it.thisObject.getObjectField("mMobileType") as TextView).visibility =
                    View.INVISIBLE
                (it.thisObject.getObjectField("mMobileTypeImage") as ImageView).visibility =
                    View.INVISIBLE
                (it.thisObject.getObjectField("mMobileTypeSingle") as TextView).visibility =
                    View.INVISIBLE
            }
        }
    }

}