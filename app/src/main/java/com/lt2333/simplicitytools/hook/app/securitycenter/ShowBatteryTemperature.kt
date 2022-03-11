package com.lt2333.simplicitytools.hook.app.securitycenter

import com.lt2333.simplicitytools.util.log
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage
import me.iacn.biliroaming.utils.DexHelper

class ShowBatteryTemperature(private val dexHelper: DexHelper?): IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (dexHelper == null) {
            log("DexHelper为null，hook ShowBatteryTemperature 失败")
            return
        }

        val methodIndex = dexHelper.findMethodUsingString(
            "isBatteryLifeFunctionSupported",
            true,
            -1,
            0,
            null,
            -1,
            null,
            null,
            null,
            true
        ).firstOrNull()
        if (methodIndex == null) {
            log("ShowBatteryTemperature 无法定位方法")
            return
        }
        val method = dexHelper.decodeMethodIndex(methodIndex)
        log(method)
        XposedBridge.hookMethod(method, XC_MethodReplacement.returnConstant(true))
    }

}