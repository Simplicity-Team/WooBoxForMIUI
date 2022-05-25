package com.lt2333.simplicitytools.hook.app.mediaeditor

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object UnlockUnlimitedCropping : HookRegister() {

    override fun init() {
        //解锁图库裁切最小值
        findMethod("com.miui.gallery.editor.photo.core.imports.obsoletes.Crop\$ResizeDetector") {
            name == "calculateMinSize"
        }.hookBefore {
            hasEnable("unlock_unlimited_cropping") {
                it.result = 0
            }
        }
        //截图无限裁切
        findMethod("com.miui.gallery.editor.photo.screen.crop.ScreenCropView\$ResizeDetector") {
            name == "calculateMinSize"
        }.hookBefore {
            hasEnable("unlock_unlimited_cropping") {
                it.result = 0
            }
        }
    }

}