package com.lt2333.simplicitytools.hook.app

import com.lt2333.simplicitytools.hook.app.android.DeleteOnPostNotification
import com.lt2333.simplicitytools.hook.app.android.DisableFlagSecure
import com.lt2333.simplicitytools.hook.app.android.MaxWallpaperScale
import com.lt2333.simplicitytools.hook.app.android.RemoveSmallWindowRestrictions
import com.lt2333.simplicitytools.hook.app.android.corepatch.CorePatch
import com.lt2333.simplicitytools.util.xposed.base.AppRegister
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

object Android: AppRegister() {
    override val packageName: String = "android"
    override val processName: List<String> = emptyList()
    override val logTag: String = "Simplicitytools"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        XposedBridge.log("Simplicitytools: 成功 Hook "+javaClass.simpleName)
        //核心破解
        CorePatch().handleLoadPackage(lpparam)

        autoInitHooks(lpparam,
            DisableFlagSecure, //允许截图
            DeleteOnPostNotification, //上层显示
            RemoveSmallWindowRestrictions, //解除小窗限制
            MaxWallpaperScale, //壁纸缩放比例
        )
    }
}