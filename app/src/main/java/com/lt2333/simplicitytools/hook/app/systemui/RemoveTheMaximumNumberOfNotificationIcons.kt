package com.lt2333.simplicitytools.hook.app.systemui

import com.lt2333.simplicitytools.util.XSPUtils
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class RemoveTheMaximumNumberOfNotificationIcons : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (XSPUtils.getBoolean("remove_the_maximum_number_of_notification_icons", false)) {
            val classIfExists = XposedHelpers.findClassIfExists(
                "com.android.systemui.statusbar.phone.NotificationIconContainer",
                lpparam.classLoader
            )
            XposedHelpers.findAndHookMethod(
                classIfExists,
                "miuiShowNotificationIcons",
                Boolean::class.java,
                object : XC_MethodReplacement() {
                    override fun replaceHookedMethod(param: MethodHookParam) {

                        if (param.args[0] as Boolean) {
                            XposedHelpers.setIntField(param.thisObject, "MAX_DOTS", 30)
                            XposedHelpers.setIntField(param.thisObject, "MAX_STATIC_ICONS", 30)
                            XposedHelpers.setIntField(
                                param.thisObject,
                                "MAX_VISIBLE_ICONS_ON_LOCK",
                                30
                            )
                        } else {
                            XposedHelpers.setIntField(param.thisObject, "MAX_DOTS", 0)
                            XposedHelpers.setIntField(param.thisObject, "MAX_STATIC_ICONS", 0)
                            XposedHelpers.setIntField(
                                param.thisObject,
                                "MAX_VISIBLE_ICONS_ON_LOCK",
                                0
                            )
                        }

                        XposedHelpers.callMethod(param.thisObject, "updateState")
                    }
                })
        }
    }
}