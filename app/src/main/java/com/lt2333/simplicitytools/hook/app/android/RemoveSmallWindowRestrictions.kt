package com.lt2333.simplicitytools.hook.app.android

import android.content.Context
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookAfterMethod
import com.lt2333.simplicitytools.util.hookBeforeMethod
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object RemoveSmallWindowRestrictions : HookRegister() {

    override fun init() {
        // 强制所有活动设为可以调整大小
        "com.android.server.wm.Task".hookBeforeMethod(getDefaultClassLoader(), "isResizeable") {
            hasEnable("remove_small_window_restrictions") {
                it.result = true
            }
        }

        "android.util.MiuiMultiWindowAdapter".hookAfterMethod(getDefaultClassLoader(), "getFreeformBlackList") {
            hasEnable("remove_small_window_restrictions") {
                it.result = (it.result as MutableList<*>).apply { clear() }
            }
        }

        "android.util.MiuiMultiWindowAdapter".hookAfterMethod(getDefaultClassLoader(), "getFreeformBlackListFromCloud", Context::class.java) {
            hasEnable("remove_small_window_restrictions") {
                it.result = (it.result as MutableList<*>).apply { clear() }
            }
        }

        "android.util.MiuiMultiWindowUtils".hookAfterMethod(getDefaultClassLoader(), "supportFreeform") {
            hasEnable("remove_small_window_restrictions") {
                it.result = true
            }
        }
    }

}