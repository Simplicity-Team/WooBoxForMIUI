package com.lt2333.simplicitytools.hook.app

import com.lt2333.simplicitytools.BuildConfig
import de.robv.android.xposed.*
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class MediaEditor : IXposedHookLoadPackage {

    var prefs = XSharedPreferences(BuildConfig.APPLICATION_ID, "config")

    override fun handleLoadPackage(lpparam: LoadPackageParam) {

        XposedBridge.log("成功Hook: "+javaClass.simpleName)

        //解锁图库裁切最小值
        val classIfExists = XposedHelpers.findClassIfExists(
            "com.miui.gallery.editor.photo.core.imports.obsoletes.Crop\$ResizeDetector",
            lpparam.classLoader
        )
        XposedHelpers.findAndHookMethod(
            classIfExists,
            "calculateMinSize",
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    if (prefs.hasFileChanged()) {
                        prefs.reload()
                    }
                    if (prefs.getBoolean("unlock_unlimited_cropping", false)) {
                        param.result = 0
                    }
                }
            })
        //截图无限裁切
        val classIfExists2 = XposedHelpers.findClassIfExists(
            "com.miui.gallery.editor.photo.screen.crop.ScreenCropView\$ResizeDetector",
            lpparam.classLoader
        )

        XposedHelpers.findAndHookMethod(
            classIfExists2,
            "calculateMinSize",
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    if (prefs.hasFileChanged()) {
                        prefs.reload()
                    }
                    if (prefs.getBoolean("unlock_unlimited_cropping", false)) {
                        param.result = 0
                    }
                }
            })

    }
}