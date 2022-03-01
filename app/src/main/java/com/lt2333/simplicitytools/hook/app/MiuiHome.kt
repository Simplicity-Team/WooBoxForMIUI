package com.lt2333.simplicitytools.hook.app

import com.lt2333.simplicitytools.hook.app.miuihome.AlwaysDisplayTime
import com.lt2333.simplicitytools.hook.app.miuihome.RemoveSmallWindowRestrictions
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class MiuiHome : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        XposedBridge.log("成功Hook: " + javaClass.simpleName)
        //时钟显示时钟
        AlwaysDisplayTime().handleLoadPackage(lpparam)
        //解除小窗限制
        RemoveSmallWindowRestrictions().handleLoadPackage(lpparam)
    }
}