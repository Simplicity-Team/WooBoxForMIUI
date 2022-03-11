package com.lt2333.simplicitytools.hook.app.android

import android.content.Context
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookAfterMethod
import com.lt2333.simplicitytools.util.hookBeforeMethod
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

class RemoveSmallWindowRestrictions : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {

        // 强制所有活动设为可以调整大小
        "com.android.server.wm.Task".hookBeforeMethod(lpparam.classLoader, "isResizeable") {
            hasEnable("remove_small_window_restrictions") {
                it.result = true
            }
        }

        "android.util.MiuiMultiWindowAdapter".hookAfterMethod(lpparam.classLoader, "getFreeformBlackList") {
            hasEnable("remove_small_window_restrictions") {
                it.result = (it.result as MutableList<*>).apply { clear() }
            }
        }

        "android.util.MiuiMultiWindowAdapter".hookAfterMethod(lpparam.classLoader, "getFreeformBlackListFromCloud", Context::class.java) {
            hasEnable("remove_small_window_restrictions") {
                it.result = (it.result as MutableList<*>).apply { clear() }
            }
        }

        "android.util.MiuiMultiWindowUtils".hookAfterMethod(lpparam.classLoader, "supportFreeform") {
            hasEnable("remove_small_window_restrictions") {
                it.result = true
            }
        }

    }
}