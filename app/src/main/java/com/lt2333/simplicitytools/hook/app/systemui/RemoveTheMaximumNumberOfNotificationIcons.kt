package com.lt2333.simplicitytools.hook.app.systemui

import com.lt2333.simplicitytools.util.callMethod
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.replaceMethod
import com.lt2333.simplicitytools.util.setIntField
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object RemoveTheMaximumNumberOfNotificationIcons: HookRegister() {

    override fun init() {
        hasEnable("remove_the_maximum_number_of_notification_icons") {
            "com.android.systemui.statusbar.phone.NotificationIconContainer".replaceMethod(getDefaultClassLoader(), "miuiShowNotificationIcons", Boolean::class.java) {
                if (it.args[0] as Boolean) {
                    it.thisObject.setIntField("MAX_DOTS", 30)
                    it.thisObject.setIntField("MAX_STATIC_ICONS", 30)
                    it.thisObject.setIntField("MAX_VISIBLE_ICONS_ON_LOCK", 30)
                } else {
                    it.thisObject.setIntField("MAX_DOTS", 0)
                    it.thisObject.setIntField("MAX_STATIC_ICONS", 0)
                    it.thisObject.setIntField("MAX_VISIBLE_ICONS_ON_LOCK", 0)
                }
                it.thisObject.callMethod("updateState")
            }
        }
    }

}