package com.lt2333.simplicitytools.hook.app

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookReturnConstant
import com.github.kyuubiran.ezxhelper.utils.loadClassOrNull
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.xposed.base.AppRegister
import de.robv.android.xposed.callbacks.XC_LoadPackage
@@ -13,11 +12,15 @@ object Updater : AppRegister() {
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
