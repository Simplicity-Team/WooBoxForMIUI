package com.lt2333.simplicitytools.hook.app

import com.lt2333.simplicitytools.hook.app.mediaeditor.UnlockUnlimitedCropping
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class MediaEditor : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        XposedBridge.log("Simplicitytools: 成功 Hook "+javaClass.simpleName)
        //解锁裁切最小值
        UnlockUnlimitedCropping().handleLoadPackage(lpparam)
    }
}