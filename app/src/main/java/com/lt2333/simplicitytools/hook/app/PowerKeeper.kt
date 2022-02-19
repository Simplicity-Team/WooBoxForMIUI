package com.lt2333.simplicitytools.hook.app

import com.lt2333.simplicitytools.BuildConfig
import de.robv.android.xposed.*
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class PowerKeeper : IXposedHookLoadPackage {

    var prefs = XSharedPreferences(BuildConfig.APPLICATION_ID, "config")

    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        XposedBridge.log("成功Hook: "+javaClass.simpleName)
        //锁定最高刷新率
        val classIfExists = XposedHelpers.findClassIfExists(
            "com.miui.powerkeeper.statemachine.DisplayFrameSetting",
            lpparam.classLoader
        )
        XposedHelpers.findAndHookMethod(
            classIfExists,
            "setScreenEffect",
            String::class.java, Int::class.javaPrimitiveType, Int::class.javaPrimitiveType,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    super.beforeHookedMethod(param)
                    if (prefs.hasFileChanged()) {
                        prefs.reload()
                    }
                    if (prefs.getBoolean("lock_max_fps", false)) {
                        param.result = null
                    }
                }
            })

    }
}