package com.lt2333.simplicitytools.hook.app

import com.lt2333.simplicitytools.hook.app.screenshot.UnlockUnlimitedCropping
import com.lt2333.simplicitytools.util.xposed.base.AppRegister
import de.robv.android.xposed.callbacks.XC_LoadPackage

object ScreenShot: AppRegister() {
    override val packageName: String = "com.miui.screenshot"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        autoInitHooks(lpparam,
            UnlockUnlimitedCropping, //锁定最高刷新率
        )
    }
}