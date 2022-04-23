package com.lt2333.simplicitytools.hook.app

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookReturnConstant
import com.github.kyuubiran.ezxhelper.utils.loadClassOrNull
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.xposed.base.AppRegister
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

object Updater : AppRegister() {
    override val packageName: String = "com.android.updater"
    override val processName: List<String> = emptyList()
    override val logTag: String = "WooBox"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        XposedBridge.log("WooBox: 成功 Hook " + javaClass.simpleName)

        hasEnable("remove_ota_validate") {
            Array(26) { "com.android.updater.common.utils.${'a' + it}" }
                .mapNotNull { loadClassOrNull(it) }
                .firstOrNull() { it.declaredFields.size >= 9 && it.declaredMethods.size > 60 }
                ?.findMethod { name == "T" && returnType == Boolean::class.java }
                ?.hookReturnConstant(false)
        }
    }
}