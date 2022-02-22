package com.lt2333.simplicitytools.hook.app

import com.lt2333.simplicitytools.hook.app.securitycenter.SkipWaitingTime
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class SecurityCenter : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        XposedBridge.log("成功Hook: " + javaClass.simpleName)
        //跳过 5/10秒等待时间
        SkipWaitingTime().handleLoadPackage(lpparam)

    }
}