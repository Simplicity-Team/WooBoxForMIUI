package com.lt2333.simplicitytools.hooks.rules.all.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.lt2333.simplicitytools.utils.hasEnable

object DeleteOnPostNotificationForAll: YukiBaseHooker() {
    override fun onHook() {
        "com.android.server.wm.AlertWindowNotification".hook {
            injectMember {
                method { name = "onPostNotification" }
                beforeHook {
                    hasEnable("delete_on_post_notification") {
                        this.result = null
                    }
                }
            }
        }
    }
}