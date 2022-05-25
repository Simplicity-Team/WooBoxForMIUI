package com.lt2333.simplicitytools.hook.app

import com.lt2333.simplicitytools.hook.app.reardisplay.RearDisplayWeather
import com.lt2333.simplicitytools.util.xposed.base.AppRegister
import de.robv.android.xposed.callbacks.XC_LoadPackage

object RearDisplay: AppRegister() {
    override val packageName: String = "com.xiaomi.misubscreenui"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        autoInitHooks(lpparam,
            RearDisplayWeather, //背屏显示天气
        )
    }
}