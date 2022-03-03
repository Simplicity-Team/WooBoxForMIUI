package com.lt2333.simplicitytools.hook

import com.lt2333.simplicitytools.BuildConfig
import com.lt2333.simplicitytools.hook.app.*
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XSharedPreferences
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class MainHook : IXposedHookLoadPackage {
    var prefs = XSharedPreferences(BuildConfig.APPLICATION_ID, "config")
    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        if (prefs.getBoolean("main_switch", true)) {
            when (lpparam.packageName) {
                //Andorid
                "android" -> {
                    Android().handleLoadPackage(lpparam)
                }
                //系统界面
                "com.android.systemui" -> {
                    SystemUI().handleLoadPackage(lpparam)
                }
                //电量与性能
                "com.miui.powerkeeper" -> {
                    PowerKeeper().handleLoadPackage(lpparam)
                }
                //桌面
                "com.miui.home" -> {
                    MiuiHome().handleLoadPackage(lpparam)
                }
                //手机管家
                "com.miui.securitycenter" -> {
                    SecurityCenter().handleLoadPackage(lpparam)
                }
                //相册编辑
                "com.miui.mediaeditor" -> {
                    MediaEditor().handleLoadPackage(lpparam)
                }
            }
        }
    }
}