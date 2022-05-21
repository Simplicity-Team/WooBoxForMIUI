package com.lt2333.simplicitytools.hook.app.android

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object DeleteOnPostNotification : HookRegister() {
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