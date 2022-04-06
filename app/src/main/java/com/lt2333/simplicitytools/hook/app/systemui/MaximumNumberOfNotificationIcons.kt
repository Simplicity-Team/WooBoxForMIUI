package com.lt2333.simplicitytools.hook.app.systemui

import com.lt2333.simplicitytools.util.*
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object MaximumNumberOfNotificationIcons: HookRegister() {

    override fun init() {
        val icons = XSPUtils.getInt("maximum_number_of_notification_icons",3)
        val dots = XSPUtils.getInt("maximum_number_of_notification_dots",3)
            "com.android.systemui.statusbar.phone.NotificationIconContainer".replaceMethod(getDefaultClassLoader(), "miuiShowNotificationIcons", Boolean::class.java) {
                if (it.args[0] as Boolean) {
                    it.thisObject.setIntField("MAX_DOTS", dots)
                    it.thisObject.setIntField("MAX_STATIC_ICONS", icons)
                    it.thisObject.setIntField("MAX_VISIBLE_ICONS_ON_LOCK", icons)
                } else {
                    it.thisObject.setIntField("MAX_DOTS", 0)
                    it.thisObject.setIntField("MAX_STATIC_ICONS", 0)
                    it.thisObject.setIntField("MAX_VISIBLE_ICONS_ON_LOCK", 0)
                }
                it.thisObject.callMethod("updateState")
            }
    }

}