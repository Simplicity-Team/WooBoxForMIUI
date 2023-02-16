package com.lt2333.simplicitytools.hooks.apps

import android.os.Build
import com.lt2333.simplicitytools.hooks.rules.all.reardisplay.RearDisplayWeatherForAll
import com.lt2333.simplicitytools.utils.xposed.base.AppRegister
import de.robv.android.xposed.callbacks.XC_LoadPackage

object RearDisplay : AppRegister() {
    override val packageName: String = "com.xiaomi.misubscreenui"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        when (Build.VERSION.SDK_INT) {
            Build.VERSION_CODES.TIRAMISU -> {
                autoInitHooks(
                    lpparam,
                    RearDisplayWeatherForAll, //背屏显示天气
                )
            }

            Build.VERSION_CODES.S -> {
                autoInitHooks(
                    lpparam,
                    RearDisplayWeatherForAll, //背屏显示天气
                )
            }
        }
    }
}