package com.lt2333.simplicitytools.hooks.apps

import android.os.Build
import com.lt2333.simplicitytools.hooks.rules.all.powerkeeper.LockMaxFpsForAll
import com.lt2333.simplicitytools.hooks.rules.s.powerkeeper.DoNotClearAppForS
import com.lt2333.simplicitytools.hooks.rules.s.powerkeeper.MakeMilletMoreAggressiveForS
import com.lt2333.simplicitytools.hooks.rules.s.powerkeeper.PreventRecoveryOfBatteryOptimizationWhitelistForS
import com.lt2333.simplicitytools.utils.xposed.base.AppRegister
import de.robv.android.xposed.callbacks.XC_LoadPackage

object PowerKeeper : AppRegister() {
    override val packageName: String = "com.miui.powerkeeper"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        when (Build.VERSION.SDK_INT) {
            Build.VERSION_CODES.TIRAMISU -> {
                autoInitHooks(
                    lpparam,
                    LockMaxFpsForAll, //锁定最高刷新率
                    PreventRecoveryOfBatteryOptimizationWhitelistForS, //防止恢复电池优化白名单
                    DoNotClearAppForS,//阻止杀后台
                    MakeMilletMoreAggressiveForS,//使 Millet 更激进
                )
            }

            Build.VERSION_CODES.S -> {
                autoInitHooks(
                    lpparam,
                    LockMaxFpsForAll, //锁定最高刷新率
                    PreventRecoveryOfBatteryOptimizationWhitelistForS, //防止恢复电池优化白名单
                    DoNotClearAppForS,//阻止杀后台
                    MakeMilletMoreAggressiveForS,//使 Millet 更激进
                )
            }
        }
    }
}