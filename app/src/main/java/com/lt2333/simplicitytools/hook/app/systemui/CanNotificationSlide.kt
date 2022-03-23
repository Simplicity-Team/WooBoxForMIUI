package com.lt2333.simplicitytools.hook.app.systemui

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookMethod
import com.lt2333.simplicitytools.util.hasEnable
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

class CanNotificationSlide : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        findMethod("com.android.systemui.statusbar.notification.policy.AppMiniWindowManager") {
            name == "canNotificationSlide"
        }.hookMethod {
            after { param ->
                hasEnable("can_notification_slide") {
                    param.result = true
                }
            }
        }
    }
}