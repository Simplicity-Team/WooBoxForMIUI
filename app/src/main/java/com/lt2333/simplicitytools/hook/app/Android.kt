package com.lt2333.simplicitytools.hook.app

import com.lt2333.simplicitytools.util.XSPUtils
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class Android : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {

        XposedBridge.log("成功Hook: "+javaClass.simpleName)
        //允许截图
        try {
            val classIfExists = XposedHelpers.findClassIfExists(
                "com.android.server.wm.WindowState",
                lpparam.classLoader
            )
            XposedHelpers.findAndHookMethod(classIfExists,
                "isSecureLocked",
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (XSPUtils.getBoolean("disable_flag_secure",false)) {
                            param.result = false
                        }
                    }
                })
        } catch (e: Exception) {
            XposedBridge.log(e)
        }


        //上层显示
        try {
            val classIfExists = XposedHelpers.findClassIfExists(
                "com.android.server.wm.AlertWindowNotification",
                lpparam.classLoader
            )
            XposedHelpers.findAndHookMethod(classIfExists,
                "onPostNotification",
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        if (XSPUtils.getBoolean("delete_on_post_notification",false)) {
                            param.result = null
                        }
                    }
                })


        } catch (e: Exception) {
            XposedBridge.log(e)
        }
    }
}