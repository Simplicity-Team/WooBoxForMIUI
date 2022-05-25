package com.lt2333.simplicitytools.hook.app

import com.lt2333.simplicitytools.hook.app.android.*
import com.lt2333.simplicitytools.hook.app.android.corepatch.CorePatch
import com.lt2333.simplicitytools.util.xposed.base.AppRegister
import de.robv.android.xposed.callbacks.XC_LoadPackage

object Android : AppRegister() {
    override val packageName: String = "android"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        //核心破解
        CorePatch().handleLoadPackage(lpparam)

        autoInitHooks(
            lpparam,
            DisableFlagSecure, //允许截图
            DeleteOnPostNotification, //上层显示
            RemoveSmallWindowRestrictions, //解除小窗限制
            MaxWallpaperScale, //壁纸缩放比例
            SystemPropertiesHook, //SystemPropertiesHook
            AllowUntrustedTouches, //允许不受信任的触摸
        )
    }

}