package com.lt2333.simplicitytools.hook.app.screenshot

import com.lt2333.simplicitytools.util.XSPUtils
import com.lt2333.simplicitytools.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers

object UnlockUnlimitedCropping : HookRegister() {

    override fun init() {
        //截图无限裁切
        val classIfExists2 = XposedHelpers.findClassIfExists(
            "com.miui.gallery.editor.photo.screen.crop.ScreenCropView\$b",
            getDefaultClassLoader()
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