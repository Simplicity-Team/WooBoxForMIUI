package com.lt2333.simplicitytools.hook.app

import com.lt2333.simplicitytools.hook.app.securitycenter.LockOneHundred
import com.lt2333.simplicitytools.hook.app.securitycenter.RemoveMacroBlacklist
import com.lt2333.simplicitytools.hook.app.securitycenter.ShowBatteryTemperature
import com.lt2333.simplicitytools.hook.app.securitycenter.SkipWaitingTime
import com.lt2333.simplicitytools.util.log
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam
import me.iacn.biliroaming.utils.DexHelper

class SecurityCenter : IXposedHookLoadPackage {

    var dexHelper: DexHelper? = null

    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        XposedBridge.log("Simplicitytools: 成功 Hook "+javaClass.simpleName)
        try {
            System.loadLibrary("DexBuilder")
            dexHelper = DexHelper.getInstance(lpparam.classLoader)
        } catch (e: UnsatisfiedLinkError) {
            log("DexBuilder 加载失败")
        }
        //跳过 5/10秒等待时间
        SkipWaitingTime().handleLoadPackage(lpparam)
        //锁定 100分
        LockOneHundred().handleLoadPackage(lpparam)
        //去除自动连招黑名单
        RemoveMacroBlacklist().handleLoadPackage(lpparam)
        //显示电池温度
        ShowBatteryTemperature(dexHelper).handleLoadPackage(lpparam)
    }
}