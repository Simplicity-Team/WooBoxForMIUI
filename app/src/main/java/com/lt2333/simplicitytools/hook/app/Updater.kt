package com.lt2333.simplicitytools.hook.app

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.xposed.base.AppRegister
import de.robv.android.xposed.callbacks.XC_LoadPackage

object Updater : AppRegister() {
    override val packageName: String = "com.android.updater"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {

        hasEnable("remove_ota_validate") {

            findMethod("miui.util.FeatureParser") {
                name == "hasFeature" && parameterCount == 2
            }.hookBefore {
                if (it.args[0] == "support_ota_validate") {
                    it.result = false
                }
            }

        }
    }
}