package com.lt2333.simplicitytools.hook.app

import com.lt2333.simplicitytools.util.XSPUtils
import com.lt2333.simplicitytools.util.hookBeforeMethod
import com.lt2333.simplicitytools.util.xposed.base.AppRegister
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

object Updater: AppRegister() {
    override val packageName: String = "com.android.updater"
    override val processName: List<String> = emptyList()
    override val logTag: String = "WooBox"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (XSPUtils.getBoolean("remove_ota_validate", false)) {
            var letter = 'a'
            for (i in 0..25) {
                val classIfExists = XposedHelpers.findClassIfExists(
                    "com.android.updater.common.utils.$letter", lpparam.classLoader
                ) ?: continue
                if (classIfExists.declaredFields.size >= 9 && classIfExists.declaredMethods.size > 60) {
                    classIfExists.hookBeforeMethod("T") {
                        it.result = false
                    }
                    return
                }
                letter++
            }
        }
    }
}