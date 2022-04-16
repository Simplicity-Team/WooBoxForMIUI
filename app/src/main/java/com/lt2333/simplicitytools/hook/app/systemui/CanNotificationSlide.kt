package com.lt2333.simplicitytools.hook.app.systemui

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookMethod
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object CanNotificationSlide: HookRegister() {

    override fun init() {
        findMethod("com.android.systemui.statusbar.notification.NotificationSettingsManager") {
            name == "canSlide"
        }.hookMethod {
            after { param ->
                hasEnable("can_notification_slide") {
                    param.result = true
                }
            }
        }
    }

}
