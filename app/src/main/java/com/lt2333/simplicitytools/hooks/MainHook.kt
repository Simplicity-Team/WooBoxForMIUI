package com.lt2333.simplicitytools.hooks

import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.factory.configs
import com.highcapable.yukihookapi.hook.factory.encase
import com.highcapable.yukihookapi.hook.xposed.bridge.event.YukiXposedEvent
import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit
import com.lt2333.simplicitytools.hooks.apps.*
import com.lt2333.simplicitytools.hooks.rules.all.corepatch.CorePatchMainHook
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.callbacks.XC_LoadPackage

@InjectYukiHookWithXposed
class MainHook : IYukiHookXposedInit {

    override fun onInit() = configs {
        debugLog {
            tag = "WooBoxForMIUI-Debug"
        }
    }

    override fun onHook() = encase {
        loadSystem(AndroidHooker) //Andorid
        loadApp("com.android.systemui", SystemUIHooker) //系统界面
        loadApp("com.milink.service", CastHooker) //投屏
        loadApp("com.miui.mediaeditor", MediaEditorHooker) //相册编辑
        loadApp("com.miui.home", MiuiHomeHooker) //桌面
        loadApp("com.miui.packageinstaller", PackageInstallerHooker) //安装包管理组件
        loadApp("com.miui.powerkeeper", PowerKeepcerHooker) //电量与性能
        loadApp("com.xiaomi.misubscreenui", RearDisplayHooker) //背屏显示
        loadApp("com.miui.screenshot", ScreenShotHooker) //截屏
        loadApp("com.miui.securitycenter", SecurityCenterHooker) //手机管家
        loadApp("com.android.settings", SettingsHooker) //设置
        loadApp("com.android.thememanager", ThemeManagerHooker) //主题管理
        loadApp("com.android.updater", UpdaterHooker) //系统更新
    }

    override fun onXposedEvent() {
        YukiXposedEvent.onHandleLoadPackage {
            if (it.packageName == "android") CorePatchMainHook().handleLoadPackage(it)
        }
        YukiXposedEvent.onInitZygote {
            CorePatchMainHook().initZygote(it)
        }
    }
}