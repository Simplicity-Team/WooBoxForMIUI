package com.lt2333.simplicitytools.hooks.rules.t.systemui

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister

object CanNotificationSlideForT: HookRegister() {

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
