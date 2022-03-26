package com.lt2333.simplicitytools.hook

import com.lt2333.simplicitytools.BuildConfig
import com.lt2333.simplicitytools.hook.app.*
import com.lt2333.simplicitytools.util.xposed.EasyXposedInit
import com.lt2333.simplicitytools.util.xposed.base.AppRegister
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.XSharedPreferences
import de.robv.android.xposed.callbacks.XC_LoadPackage

class XposedEntry: EasyXposedInit() {
    private var prefs = XSharedPreferences(BuildConfig.APPLICATION_ID, "config")

    override val registeredApp: List<AppRegister> = listOf(
        Android, // Android
        SystemUI, // 系统界面
        PowerKeeper, // 电量与性能
        MiuiHome, // 桌面
        SecurityCenter, // 手机管家
        MediaEditor, // 相册编辑
        Updater, // 系统更新
        Settings, // 设置
        ThemeManager, // 主题壁纸
        ScreenShot, // 截屏
    )

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        if (prefs.getBoolean("main_switch", true)) {
            super.handleLoadPackage(lpparam)
        }
    }

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam?) {
        super.initZygote(startupParam)
    }

}