package com.lt2333.simplicitytools.hook.app

import com.lt2333.simplicitytools.hook.app.powerkeeper.LockMaxFps
import com.lt2333.simplicitytools.hook.app.powerkeeper.PreventRecoveryOfBatteryOptimizationWhitelist
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class PowerKeeper : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        XposedBridge.log("成功Hook: "+javaClass.simpleName)
        //锁定最高刷新率
        LockMaxFps().handleLoadPackage(lpparam)
        //防止恢复电池优化白名单
        PreventRecoveryOfBatteryOptimizationWhitelist().handleLoadPackage(lpparam)

    }
}