package com.lt2333.simplicitytools.hooks.rules.all.securitycenter

import com.lt2333.simplicitytools.utils.XSPUtils
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers

object RemoveMacroBlacklistForAll : HookRegister() {

    override fun init() {
        // TODO: 新版已失效 
        if (XSPUtils.getBoolean("remove_macro_blacklist", false)) {
            var letter = 'a'
            for (i in 0..25) {
                val classIfExists = XposedHelpers.findClassIfExists(
                    "com.miui.gamebooster.v.$letter" + "0", getDefaultClassLoader()
                ) ?: continue
                if (classIfExists.declaredMethods.size in 6..12 && classIfExists.fields.isEmpty() && classIfExists.declaredFields.size >= 2) {
                    XposedHelpers.findAndHookMethod(classIfExists, "c", String::class.java,
                        object : XC_MethodHook() {
                            override fun beforeHookedMethod(param: MethodHookParam?) {
                                param?.result = false
                            }
                        })
                    return
                }
                letter++
            }
        }
    }

}