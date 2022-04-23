package com.lt2333.simplicitytools.hook.app.securitycenter

import android.view.View
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object LockOneHundred : HookRegister() {
    override fun init() {
        //防止点击重新检测
        findMethod("com.miui.securityscan.ui.main.MainContentFrame") {
            name == "onClick" && parameterTypes[0] == View::class.java
        }.hookBefore {
            hasEnable("lock_one_hundred") {
                it.result = null
            }
        }

        //锁定100分
        findMethod("com.miui.securityscan.scanner.ScoreManager") {
            name == "B"
        }.hookBefore {
            hasEnable("lock_one_hundred") {
                it.result = 0
            }
        }
    }
}