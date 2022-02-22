package com.lt2333.simplicitytools.hook.app

import com.lt2333.simplicitytools.util.XSPUtils
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class MediaEditor : IXposedHookLoadPackage {

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
                    if (XSPUtils.getBoolean("unlock_unlimited_cropping",false)) {
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
                    if (XSPUtils.getBoolean("unlock_unlimited_cropping",false)) {
                        param.result = 0
                    }
                }
            })

    }
}