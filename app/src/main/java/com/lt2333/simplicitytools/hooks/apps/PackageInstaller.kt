package com.lt2333.simplicitytools.hooks.apps

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.AppRegister
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

object PackageInstaller : AppRegister() {

    override val packageName: String = "com.miui.packageinstaller"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        hasEnable("pkg_installer_count_checking") {
            findMethod("com.miui.packageInstaller.model.RiskControlRules") {
                name == "getCurrentLevel"
            }.hookBefore { param ->
                XposedBridge.log("Hooked getCurrentLevel, param result = ${param.result}")
                param.result = 0
            }
        }
    }
}