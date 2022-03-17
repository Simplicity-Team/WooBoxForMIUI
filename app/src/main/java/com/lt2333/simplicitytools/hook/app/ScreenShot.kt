package com.lt2333.simplicitytools.hook.app

import com.lt2333.simplicitytools.hook.app.screenshot.UnlockUnlimitedCropping
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class ScreenShot : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        XposedBridge.log("Simplicitytools: 成功 Hook " + javaClass.simpleName)
        //锁定最高刷新率
        UnlockUnlimitedCropping().handleLoadPackage(lpparam)
    }
}