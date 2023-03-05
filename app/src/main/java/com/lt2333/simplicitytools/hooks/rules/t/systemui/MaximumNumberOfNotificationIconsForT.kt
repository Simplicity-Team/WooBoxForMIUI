package com.lt2333.simplicitytools.hooks.rules.t.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.lt2333.simplicitytools.utils.XSPUtils
import java.lang.reflect.Method

object MaximumNumberOfNotificationIconsForT: YukiBaseHooker() {
    override fun onHook() {
        val icons = XSPUtils.getInt("maximum_number_of_notification_icons", 3)
        val dots = XSPUtils.getInt("maximum_number_of_notification_dots", 3)
        "com.android.systemui.statusbar.phone.NotificationIconContainer".hook {
            injectMember {
                method {
                    name = "miuiShowNotificationIcons"
                    paramCount = 1
                }
                replaceUnit {
                    if (this.args[0] as Boolean) {
                        instance.current().field { name = "MAX_DOTS" }.set(dots)
                        instance.current().field { name = "MAX_STATIC_ICONS" }.set(icons)
                        instance.current().field { name = "MAX_ICONS_ON_LOCKSCREEN" }.set(icons)
                    } else {
                        instance.current().field { name = "MAX_DOTS" }.set(0)
                        instance.current().field { name = "MAX_STATIC_ICONS" }.set(0)
                        instance.current().field { name = "MAX_ICONS_ON_LOCKSCREEN" }.set(0)
                    }
                    instance.current().method { name = "updateState" }.call()
                }
            }
        }
    }
}