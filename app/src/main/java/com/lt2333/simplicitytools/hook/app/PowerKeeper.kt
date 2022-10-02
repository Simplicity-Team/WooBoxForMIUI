package com.lt2333.simplicitytools.hook.app

import com.lt2333.simplicitytools.hook.app.powerkeeper.DoNotClearApp
import com.lt2333.simplicitytools.hook.app.powerkeeper.LockMaxFps
import com.lt2333.simplicitytools.hook.app.powerkeeper.MakeMilletMoreAggressive
import com.lt2333.simplicitytools.hook.app.powerkeeper.PreventRecoveryOfBatteryOptimizationWhitelist
import com.lt2333.simplicitytools.util.xposed.base.AppRegister
import de.robv.android.xposed.callbacks.XC_LoadPackage

object PowerKeeper: AppRegister() {
    override val packageName: String = "com.miui.powerkeeper"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        autoInitHooks(lpparam,
            LockMaxFps, //锁定最高刷新率
            PreventRecoveryOfBatteryOptimizationWhitelist, //防止恢复电池优化白名单
            DoNotClearApp,//阻止杀后台
            MakeMilletMoreAggressive,//使 Millet 更激进
        )
    }
}