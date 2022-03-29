package com.lt2333.simplicitytools.hook.app.systemui

import com.lt2333.simplicitytools.util.*
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object MaximumNumberOfNotificationIcons: HookRegister() {

    override fun init() {
        val size = XSPUtils.getInt("maximum_number_of_notification_icons",3)
            "com.android.systemui.statusbar.phone.NotificationIconContainer".replaceMethod(getDefaultClassLoader(), "miuiShowNotificationIcons", Boolean::class.java) {
                if (it.args[0] as Boolean) {
                    it.thisObject.setIntField("MAX_DOTS", size)
                    it.thisObject.setIntField("MAX_STATIC_ICONS", size)
                    it.thisObject.setIntField("MAX_VISIBLE_ICONS_ON_LOCK", size)
                } else {
                    it.thisObject.setIntField("MAX_DOTS", 0)
                    it.thisObject.setIntField("MAX_STATIC_ICONS", 0)
                    it.thisObject.setIntField("MAX_VISIBLE_ICONS_ON_LOCK", 0)
                }
                it.thisObject.callMethod("updateState")
            }
    }

}