package com.lt2333.simplicitytools.hooks.rules.all.android

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister

object DeleteOnPostNotificationForAll : HookRegister() {
    override fun init() {
        findMethod("com.android.server.wm.AlertWindowNotification") {
            name == "onPostNotification"
        }.hookBefore {
            hasEnable("delete_on_post_notification") {
                it.result = null
            }
        }
    }

}