package com.lt2333.simplicitytools.hook.app.systemui

import android.view.View
import android.widget.ImageView
import com.lt2333.simplicitytools.util.XSPUtils
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class HideHDIcon :IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val classIfExists = XposedHelpers.findClassIfExists(
            "com.android.systemui.statusbar.StatusBarMobileView",
            lpparam.classLoader
        )
        XposedHelpers.findAndHookMethod(
            classIfExists,
            "initViewState",
            "com.android.systemui.statusbar.phone.StatusBarSignalPolicy\$MobileIconState",
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    if (XSPUtils.getBoolean("hide_big_hd_icon", false)) {
                        val bigHd = XposedHelpers.getObjectField(
                            param.thisObject,
                            "mVolte"
                        ) as ImageView
                        bigHd.visibility = View.GONE
                    }
                    if (XSPUtils.getBoolean("hide_small_hd_icon", false)) {
                        val smallHd = XposedHelpers.getObjectField(
                            param.thisObject,
                            "mSmallHd"
                        ) as ImageView
                        smallHd.visibility = View.GONE
                    }
                    if (XSPUtils.getBoolean("hide_hd_no_service_icon", false)) {
                        val volteNoService = XposedHelpers.getObjectField(
                            param.thisObject,
                            "mVolteNoService"
                        ) as ImageView
                        volteNoService.visibility = View.GONE
                    }
                }
            })
        XposedHelpers.findAndHookMethod(
            classIfExists,
            "updateState",
            "com.android.systemui.statusbar.phone.StatusBarSignalPolicy\$MobileIconState",
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    if (XSPUtils.getBoolean("hide_big_hd_icon", false)) {
                        val bigHd = XposedHelpers.getObjectField(
                            param.thisObject,
                            "mVolte"
                        ) as ImageView
                        bigHd.visibility = View.GONE
                    }
                    if (XSPUtils.getBoolean("hide_small_hd_icon", false)) {
                        val smallHd = XposedHelpers.getObjectField(
                            param.thisObject,
                            "mSmallHd"
                        ) as ImageView

                        smallHd.visibility = View.GONE
                    }
                    if (XSPUtils.getBoolean("hide_hd_no_service_icon", false)) {
                        val volteNoService = XposedHelpers.getObjectField(
                            param.thisObject,
                            "mVolteNoService"
                        ) as ImageView
                        volteNoService.visibility = View.GONE
                    }
                }
            })
    }
}