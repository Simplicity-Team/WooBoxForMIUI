package com.lt2333.simplicitytools.hook.app.screenshot

import com.lt2333.simplicitytools.util.XSPUtils
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class UnlockUnlimitedCropping :IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        //截图无限裁切
        val classIfExists2 = XposedHelpers.findClassIfExists(
            "com.miui.gallery.editor.photo.screen.crop.ScreenCropView\$b",
            lpparam.classLoader
        )

        XposedHelpers.findAndHookMethod(
            classIfExists2,
            "a",
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    if (XSPUtils.getBoolean("unlock_unlimited_cropping",false)) {
                        param.result = 0
                    }
                }
            })
    }
}