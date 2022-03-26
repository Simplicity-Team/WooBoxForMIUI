package com.lt2333.simplicitytools.hook.app

import com.lt2333.simplicitytools.hook.app.miuihome.AlwaysDisplayTime
import com.lt2333.simplicitytools.hook.app.miuihome.DoubleTapToSleep
import com.lt2333.simplicitytools.util.xposed.base.AppRegister
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

object MiuiHome: AppRegister() {
    override val packageName: String = "com.miui.home"
    override val processName: List<String> = emptyList()
    override val logTag: String = "Simplicitytools"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        XposedBridge.log("Simplicitytools: 成功 Hook "+javaClass.simpleName)
        //时钟显示时钟
        AlwaysDisplayTime().handleLoadPackage(lpparam)
        //双击锁屏
        DoubleTapToSleep().handleLoadPackage(lpparam)
    }
}