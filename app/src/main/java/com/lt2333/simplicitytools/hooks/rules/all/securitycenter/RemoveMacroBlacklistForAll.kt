package com.lt2333.simplicitytools.hooks.rules.all.securitycenter

import com.github.kyuubiran.ezxhelper.utils.*
import com.lt2333.simplicitytools.utils.XSPUtils
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers

object RemoveMacroBlacklistForAll : HookRegister() {

    override fun init() {
        if (XSPUtils.getBoolean("remove_macro_blacklist", false)) {
            var letter = 'a'
            for (i in 0..25) {
                val classIfExists = XposedHelpers.findClassIfExists(
                    "com.miui.gamebooster.utils.$letter" + "0", getDefaultClassLoader()
                ) ?: continue
                if (classIfExists.declaredMethods.size in 10..15 && classIfExists.fields.isEmpty() && classIfExists.declaredFields.size >= 2) {
                    XposedBridge.log("Woobox: RemoveMacroBlacklist in ${classIfExists.name}")
                    findMethod(classIfExists){
                        name=="c" && returnType==Boolean::class.java && isStatic &&parameterCount==1
                    }.hookBefore {
                        XposedBridge.log("Woobox: c")
                        it.result = false
                    }
                    return
                }
                letter++
            }
        }
    }

}