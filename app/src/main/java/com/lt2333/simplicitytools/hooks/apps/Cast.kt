package com.lt2333.simplicitytools.hooks.apps

import android.os.Build
import com.lt2333.simplicitytools.hooks.rules.all.cast.ForceSupportSendAppForAll
import com.lt2333.simplicitytools.utils.xposed.base.AppRegister
import de.robv.android.xposed.callbacks.XC_LoadPackage

object Cast : AppRegister() {
    override val packageName: String = "com.milink.service"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        when (Build.VERSION.SDK_INT) {
            Build.VERSION_CODES.TIRAMISU -> {
                autoInitHooks(
                    lpparam,
                    ForceSupportSendAppForAll, //强制允许所有应用接力
                )
            }

            Build.VERSION_CODES.S -> {
                autoInitHooks(
                    lpparam,
                    ForceSupportSendAppForAll, //强制允许所有应用接力
                )
            }
        }
    }
}