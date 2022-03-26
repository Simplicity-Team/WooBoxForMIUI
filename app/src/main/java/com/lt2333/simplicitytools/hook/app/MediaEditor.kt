package com.lt2333.simplicitytools.hook.app

import com.lt2333.simplicitytools.hook.app.mediaeditor.UnlockUnlimitedCropping
import com.lt2333.simplicitytools.util.xposed.base.AppRegister
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

object MediaEditor: AppRegister() {
    override val packageName: String = "com.miui.mediaeditor"
    override val processName: List<String> = emptyList()
    override val logTag: String = "Simplicitytools"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        XposedBridge.log("Simplicitytools: 成功 Hook "+javaClass.simpleName)
        //解锁裁切最小值
        autoInitHooks(lpparam,
            UnlockUnlimitedCropping, //解锁裁切最小值
        )
    }
}