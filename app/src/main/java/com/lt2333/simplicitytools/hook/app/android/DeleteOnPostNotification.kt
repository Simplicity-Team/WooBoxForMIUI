package com.lt2333.simplicitytools.hook.app.android

import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookBeforeMethod
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object DeleteOnPostNotification : HookRegister() {

    override fun init() {
        "com.android.server.wm.AlertWindowNotification".hookBeforeMethod(getDefaultClassLoader(), "onPostNotification") {
            hasEnable("delete_on_post_notification") {
                it.result = null
            }
        }
    }

}