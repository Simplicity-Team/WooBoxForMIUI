package com.lt2333.simplicitytools.hook.app.securitycenter

import android.view.View
import com.lt2333.simplicitytools.util.XSPUtils
import com.lt2333.simplicitytools.util.findClass
import com.lt2333.simplicitytools.util.hookBeforeMethod
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object LockOneHundred : HookRegister() {

    override fun init() {
        //防止点击重新检测
        val mainContentFrameClass = "com.miui.securityscan.ui.main.MainContentFrame".findClass(getDefaultClassLoader())
        mainContentFrameClass.hookBeforeMethod("onClick", View::class.java) {
            if (XSPUtils.getBoolean("lock_one_hundred", false)) {
                it.result = null
            }
        }

        //锁定100分
        val scoreManagerClass = "com.miui.securityscan.scanner.ScoreManager".findClass(getDefaultClassLoader())
        scoreManagerClass.hookBeforeMethod("B") {
            if (XSPUtils.getBoolean("lock_one_hundred", false)) {
                it.result = 0
            }
        }
    }

}