package com.lt2333.simplicitytools.utils.xposed.base

import com.github.kyuubiran.ezxhelper.utils.Log.logexIfThrow
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

abstract class AppRegister: IXposedHookLoadPackage {

    abstract val packageName: String

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {}

    protected fun autoInitHooks(lpparam: XC_LoadPackage.LoadPackageParam, vararg hook: HookRegister) {
        hook.also {
            XposedBridge.log("WooBox: Try to Hook [$packageName]")
        }.forEach {
            runCatching {
                if (it.isInit) return@forEach
                it.setLoadPackageParam(lpparam)
                it.init()
                it.isInit = true
            }.logexIfThrow("Failed to Hook [$packageName]")
        }
    }

}