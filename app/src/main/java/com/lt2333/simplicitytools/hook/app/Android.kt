package com.lt2333.simplicitytools.hook.app

import com.lt2333.simplicitytools.hook.app.android.DeleteOnPostNotification
import com.lt2333.simplicitytools.hook.app.android.DisableFlagSecure
import com.lt2333.simplicitytools.hook.app.android.corepatch.CorePatch
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

class Android : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        XposedBridge.log("成功Hook: "+javaClass.simpleName)
        //核心破解
        CorePatch().handleLoadPackage(lpparam)
        //允许截图
        DisableFlagSecure().handleLoadPackage(lpparam)
        //上层显示
        DeleteOnPostNotification().handleLoadPackage(lpparam)
    }
}