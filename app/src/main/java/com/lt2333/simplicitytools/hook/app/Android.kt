package com.lt2333.simplicitytools.hook.app

import com.lt2333.simplicitytools.BuildConfig
import de.robv.android.xposed.*
import de.robv.android.xposed.callbacks.XC_LoadPackage

class Android : IXposedHookLoadPackage {

    var prefs = XSharedPreferences(BuildConfig.APPLICATION_ID, "config")

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        //允许截图
        try {
            XposedHelpers.findAndHookMethod("com.android.server.wm.WindowState",
                    lpparam.classLoader,
                    "isSecureLocked",
                    object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: MethodHookParam) {
                            if (prefs.hasFileChanged()) {
                                prefs.reload()
                            }
                            if (prefs.getBoolean("disable_flag_secure", false)) {
                                param.result = false
                            }
                        }
                    })
        } catch (e: Exception) {
            XposedBridge.log(e)
        }


        //上层显示
        try {
            XposedHelpers.findAndHookMethod("com.android.server.wm.AlertWindowNotification",
                    lpparam.classLoader,
                    "onPostNotification",
                    object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: MethodHookParam) {
                            if (prefs.hasFileChanged()) {
                                prefs.reload()
                            }
                            if (prefs.getBoolean("delete_on_post_notification", false)) {
                                param.result = null
                            }
                        }
                    })


        } catch (e: Exception) {
            XposedBridge.log(e)
        }
    }
}