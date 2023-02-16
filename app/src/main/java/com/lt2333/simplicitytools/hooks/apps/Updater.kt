package com.lt2333.simplicitytools.hooks.apps

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.github.kyuubiran.ezxhelper.utils.hookReturnConstant
import com.github.kyuubiran.ezxhelper.utils.loadClassOrNull
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.AppRegister
import de.robv.android.xposed.callbacks.XC_LoadPackage

object Updater : AppRegister() {
    override val packageName: String = "com.android.updater"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {

        hasEnable("remove_ota_validate") {
            Array(26) { "com.android.updater.common.utils.${'a' + it}" }
                .mapNotNull { loadClassOrNull(it) }
                .firstOrNull { it.declaredFields.size >= 9 && it.declaredMethods.size > 60 }
                ?.findMethod { name == "T" && returnType == Boolean::class.java }
                ?.hookReturnConstant(false)

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
