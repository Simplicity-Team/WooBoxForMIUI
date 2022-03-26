package com.lt2333.simplicitytools.hook.app.mediaeditor

import com.lt2333.simplicitytools.util.XSPUtils
import com.lt2333.simplicitytools.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers

object UnlockUnlimitedCropping : HookRegister() {

    override fun init() {
        //解锁图库裁切最小值
        val classIfExists = XposedHelpers.findClassIfExists(
            "com.miui.gallery.editor.photo.core.imports.obsoletes.Crop\$ResizeDetector",
            getDefaultClassLoader()
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
            getDefaultClassLoader()
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