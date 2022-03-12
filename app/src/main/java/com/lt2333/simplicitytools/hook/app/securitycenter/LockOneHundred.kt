package com.lt2333.simplicitytools.hook.app.securitycenter

import android.view.View
import com.lt2333.simplicitytools.util.XSPUtils
import com.lt2333.simplicitytools.util.findClass
import com.lt2333.simplicitytools.util.hookBeforeMethod
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

class LockOneHundred : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {

        //防止点击重新检测
        val mainContentFrameClass = "com.miui.securityscan.ui.main.MainContentFrame".findClass(lpparam.classLoader)
        mainContentFrameClass.hookBeforeMethod("onClick", View::class.java) {
            if (XSPUtils.getBoolean("lock_one_hundred", false)) {
                it.result = null
            }
        }

        //锁定100分
        var scoreManagerClass = "com.miui.securityscan.scanner.ScoreManager".findClass(lpparam.classLoader)
        scoreManagerClass.hookBeforeMethod("B") {
            if (XSPUtils.getBoolean("lock_one_hundred", false)) {
                it.result = 0
            }
        }
    }
}