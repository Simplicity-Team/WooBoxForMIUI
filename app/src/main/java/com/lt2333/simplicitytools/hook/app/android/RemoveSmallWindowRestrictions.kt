package com.lt2333.simplicitytools.hook.app.android

import android.content.Context
import com.lt2333.simplicitytools.util.XSPUtils
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class RemoveSmallWindowRestrictions : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {

        // 强制所有活动设为可以调整大小
        val classIfExists = XposedHelpers.findClassIfExists(
            "com.android.server.wm.Task",
            lpparam.classLoader
        )
        XposedHelpers.findAndHookMethod(
            classIfExists,
            "isResizeable",
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    if (XSPUtils.getBoolean("remove_small_window_restrictions", false)) {
                        param.result = true
                    }
                }
            })



        val classIfExists2 = XposedHelpers.findClassIfExists(
            "android.util.MiuiMultiWindowAdapter",
            lpparam.classLoader
        )
        XposedHelpers.findAndHookMethod(
            classIfExists2,
            "getFreeformBlackList",
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    if (XSPUtils.getBoolean("remove_small_window_restrictions", false)) {
                        val blackList = param.result as MutableList<String>
                        blackList.clear()
                        param.result = blackList
                    }
                }
            })
        XposedHelpers.findAndHookMethod(
            classIfExists2,
            "getFreeformBlackListFromCloud",
            Context::class.java,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    if (XSPUtils.getBoolean("remove_small_window_restrictions", false)) {
                        val blackList = param.result as MutableList<String>
                        blackList.clear()
                        param.result = blackList
                    }
                }
            })
        val classIfExists3 = XposedHelpers.findClassIfExists(
            "android.util.MiuiMultiWindowUtils",
            lpparam.classLoader
        )

        XposedHelpers.findAndHookMethod(
            classIfExists3,
            "supportFreeform",
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    if (XSPUtils.getBoolean("remove_small_window_restrictions", false)) {
                        param.result = true
                    }
                }
            })
    }
}