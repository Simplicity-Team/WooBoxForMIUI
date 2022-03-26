package com.lt2333.simplicitytools.hook.app

import com.lt2333.simplicitytools.hook.app.powerkeeper.LockMaxFps
import com.lt2333.simplicitytools.hook.app.powerkeeper.PreventRecoveryOfBatteryOptimizationWhitelist
import com.lt2333.simplicitytools.util.xposed.base.AppRegister
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

object PowerKeeper: AppRegister() {
    override val packageName: String = "com.miui.powerkeeper"
    override val processName: List<String> = emptyList()
    override val logTag: String = "Simplicitytools"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        XposedBridge.log("Simplicitytools: 成功 Hook "+javaClass.simpleName)
        autoInitHooks(lpparam,
            LockMaxFps, //锁定最高刷新率
            PreventRecoveryOfBatteryOptimizationWhitelist, //防止恢复电池优化白名单
        )
    }
}