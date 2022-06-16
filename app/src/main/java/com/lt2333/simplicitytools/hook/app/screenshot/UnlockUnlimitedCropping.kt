package com.lt2333.simplicitytools.hook.app.screenshot

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object UnlockUnlimitedCropping : HookRegister() {

    override fun init() {
        //截图无限裁切
        findMethod("com.miui.gallery.editor.photo.screen.crop.ScreenCropView\$b") {
            name == "a" && parameterCount == 0 && returnType == Int::class.java
        }.hookBefore {
            hasEnable("unlock_unlimited_cropping") {
                it.result = 0
            }
        }
    }

}