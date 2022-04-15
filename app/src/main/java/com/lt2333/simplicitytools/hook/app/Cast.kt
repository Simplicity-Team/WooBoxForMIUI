package com.lt2333.simplicitytools.hook.app

import com.lt2333.simplicitytools.hook.app.cast.ForceSupportSendApp
import com.lt2333.simplicitytools.util.xposed.base.AppRegister
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

object Cast: AppRegister() {
    override val packageName: String = "com.milink.service"
    override val processName: List<String> = emptyList()
    override val logTag: String = "WooBox"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        XposedBridge.log("WooBox: 成功 Hook "+javaClass.simpleName)
        autoInitHooks(lpparam,
            ForceSupportSendApp, //强制允许所有应用接力
        )
    }
}