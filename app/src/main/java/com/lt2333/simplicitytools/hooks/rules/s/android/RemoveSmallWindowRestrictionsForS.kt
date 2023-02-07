package com.lt2333.simplicitytools.hooks.rules.s.android

import android.content.Context
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister

object RemoveSmallWindowRestrictionsForS : HookRegister() {
    override fun init() {
        // 强制所有活动设为可以调整大小
        findMethod("com.android.server.wm.Task") {
            name == "isResizeable"
        }.hookBefore {
            hasEnable("remove_small_window_restrictions") {
                it.result = true
            }
        }

        findMethod("android.util.MiuiMultiWindowAdapter") {
            name == "getFreeformBlackList"
        }.hookAfter {
            hasEnable("remove_small_window_restrictions") {
                it.result = (it.result as MutableList<*>).apply { clear() }
            }
        }

        findMethod("android.util.MiuiMultiWindowAdapter") {
            name == "getFreeformBlackListFromCloud" && parameterTypes[0] == Context::class.java
        }.hookAfter {
            hasEnable("remove_small_window_restrictions") {
                it.result = (it.result as MutableList<*>).apply { clear() }
            }
        }

        findMethod("android.util.MiuiMultiWindowUtils") {
            name == "supportFreeform"
        }.hookAfter {
            hasEnable("remove_small_window_restrictions") {
                it.result = true
            }
        }
    }

}