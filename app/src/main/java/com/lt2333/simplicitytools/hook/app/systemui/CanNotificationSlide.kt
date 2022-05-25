package com.lt2333.simplicitytools.hook.app.systemui

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object CanNotificationSlide: HookRegister() {

    override fun init() {
        findMethod("com.android.systemui.statusbar.notification.NotificationSettingsManager") {
            name == "canSlide"
        }.hookAfter {
            hasEnable("can_notification_slide") {
                it.result = true
            }
        }
    }

}
