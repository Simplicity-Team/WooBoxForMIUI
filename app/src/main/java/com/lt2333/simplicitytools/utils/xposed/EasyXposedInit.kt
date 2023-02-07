package com.lt2333.simplicitytools.utils.xposed

import com.github.kyuubiran.ezxhelper.init.EzXHelperInit
import com.github.kyuubiran.ezxhelper.utils.Log.logexIfThrow
import com.lt2333.simplicitytools.utils.xposed.base.AppRegister
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.callbacks.XC_LoadPackage

abstract class EasyXposedInit : IXposedHookLoadPackage, IXposedHookZygoteInit {

    private lateinit var packageParam: XC_LoadPackage.LoadPackageParam
    abstract val registeredApp: List<AppRegister>
    private val TAG = "WooBox"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {

        packageParam = lpparam!!
        registeredApp.forEach { app ->
            if (app.packageName == lpparam.packageName) {
                EzXHelperInit.apply {
                    setLogXp(true)
                    setLogTag(TAG)
                    setToastTag(TAG)
                    initHandleLoadPackage(lpparam)
                }
                runCatching { app.handleLoadPackage(lpparam) }.logexIfThrow("Failed call handleLoadPackage, package: ${app.packageName}")
            }
        }
    }

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam?) {
        EzXHelperInit.initZygote(startupParam!!)
    }

}