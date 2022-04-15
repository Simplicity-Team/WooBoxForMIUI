package com.lt2333.simplicitytools.hook.app

import com.lt2333.simplicitytools.hook.app.securitycenter.*
import com.lt2333.simplicitytools.util.xposed.base.AppRegister
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

object SecurityCenter: AppRegister() {
    override val packageName: String = "com.miui.securitycenter"
    override val processName: List<String> = emptyList()
    override val logTag: String = "WooBox"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        XposedBridge.log("WooBox: 成功 Hook " + javaClass.simpleName)
        autoInitHooks(lpparam,
            SkipWaitingTime, //跳过 5/10秒等待时间
            LockOneHundred, //锁定 100分
            RemoveMacroBlacklist, //去除自动连招黑名单
            ShowBatteryTemperature, //显示电池温度
            RemoveOpenAppConfirmationPopup, //去除打开应用弹窗
        )
    }
}