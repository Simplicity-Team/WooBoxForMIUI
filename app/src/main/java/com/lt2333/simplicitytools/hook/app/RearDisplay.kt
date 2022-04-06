package com.lt2333.simplicitytools.hook.app

import com.lt2333.simplicitytools.hook.app.cast.RearDisplayWeather
import com.lt2333.simplicitytools.util.xposed.base.AppRegister
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

object RearDisplay: AppRegister() {
    override val packageName: String = "com.xiaomi.misubscreenui"
    override val processName: List<String> = emptyList()
    override val logTag: String = "Simplicitytools"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        XposedBridge.log("Simplicitytools: 成功 Hook "+javaClass.simpleName)
        autoInitHooks(lpparam,
            RearDisplayWeather, //背屏显示天气
        )
    }
}