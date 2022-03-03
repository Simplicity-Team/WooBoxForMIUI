package com.lt2333.simplicitytools.hook.app.securitycenter

import android.view.View
import com.lt2333.simplicitytools.util.XSPUtils
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class LockOneHundred : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {

        //防止点击重新检测
        var classIfExists = XposedHelpers.findClassIfExists(
            "com.miui.securityscan.ui.main.MainContentFrame",
            lpparam.classLoader
        )
        XposedHelpers.findAndHookMethod(
            classIfExists,
            "onClick",
            View::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    if (XSPUtils.getBoolean("lock_one_hundred", false)) {
                        param.result = null
                    }
                }
            })

        //锁定100分
        var classIfExists2 = XposedHelpers.findClassIfExists(
            "com.miui.securityscan.scanner.ScoreManager",
            lpparam.classLoader
        )
        XposedHelpers.findAndHookMethod(
            classIfExists2,
            "B",
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    if (XSPUtils.getBoolean("lock_one_hundred", false)) {
                        param.result = 0
                    }
                }
            })

    }
}