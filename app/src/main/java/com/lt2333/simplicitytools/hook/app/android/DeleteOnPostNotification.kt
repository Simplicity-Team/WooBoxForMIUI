package com.lt2333.simplicitytools.hook.app.android

import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookBeforeMethod
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

class DeleteOnPostNotification : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        "com.android.server.wm.AlertWindowNotification".hookBeforeMethod(lpparam.classLoader, "onPostNotification") {
            hasEnable("delete_on_post_notification") {
                it.result = null
            }
        }
    }
}