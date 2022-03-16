package com.lt2333.simplicitytools.hook.app

import com.lt2333.simplicitytools.hook.app.miuihome.AlwaysDisplayTime
import com.lt2333.simplicitytools.hook.app.miuihome.DoubleTapToSleep
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class MiuiHome : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        XposedBridge.log("Simplicitytools: 成功 Hook "+javaClass.simpleName)
        //时钟显示时钟
        AlwaysDisplayTime().handleLoadPackage(lpparam)
        //双击锁屏
        DoubleTapToSleep().handleLoadPackage(lpparam)
    }
}