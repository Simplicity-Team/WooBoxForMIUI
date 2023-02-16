package com.lt2333.simplicitytools.hooks.apps

import android.os.Build
import com.lt2333.simplicitytools.hooks.rules.all.securitycenter.LockOneHundredForAll
import com.lt2333.simplicitytools.hooks.rules.all.securitycenter.RemoveMacroBlacklistForAll
import com.lt2333.simplicitytools.hooks.rules.all.securitycenter.RemoveOpenAppConfirmationPopupForAll
import com.lt2333.simplicitytools.hooks.rules.all.securitycenter.SkipWaitingTimeForAll
import com.lt2333.simplicitytools.hooks.rules.s.securitycenter.ShowBatteryTemperatureForS
import com.lt2333.simplicitytools.hooks.rules.t.securitycenter.ShowBatteryTemperatureForT
import com.lt2333.simplicitytools.utils.xposed.base.AppRegister
import de.robv.android.xposed.callbacks.XC_LoadPackage

object SecurityCenter : AppRegister() {
    override val packageName: String = "com.miui.securitycenter"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        when (Build.VERSION.SDK_INT) {
            Build.VERSION_CODES.TIRAMISU -> {
                autoInitHooks(
                    lpparam,
                    SkipWaitingTimeForAll, //跳过 5/10秒等待时间
                    LockOneHundredForAll, //锁定 100分
                    RemoveMacroBlacklistForAll, //去除自动连招黑名单
                    ShowBatteryTemperatureForT, //显示电池温度
                    RemoveOpenAppConfirmationPopupForAll, //去除打开应用弹窗
                )
            }

            Build.VERSION_CODES.S -> {
                autoInitHooks(
                    lpparam,
                    SkipWaitingTimeForAll, //跳过 5/10秒等待时间
                    LockOneHundredForAll, //锁定 100分
                    RemoveMacroBlacklistForAll, //去除自动连招黑名单
                    ShowBatteryTemperatureForS, //显示电池温度
                    RemoveOpenAppConfirmationPopupForAll, //去除打开应用弹窗
                )
            }
        }
    }
}