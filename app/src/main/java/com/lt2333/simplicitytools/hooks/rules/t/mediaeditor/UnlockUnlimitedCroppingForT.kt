package com.lt2333.simplicitytools.hooks.rules.t.mediaeditor

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.github.kyuubiran.ezxhelper.utils.loadClassOrNull
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister

object UnlockUnlimitedCroppingForT : HookRegister() {

    override fun init() {
        val resizeDetectorClass = loadClassOrNull("com.miui.gallery.editor.photo.core.imports.obsoletes.Crop\$ResizeDetector")
        if (resizeDetectorClass != null) {
            findMethod("com.miui.gallery.editor.photo.core.imports.obsoletes.Crop\$ResizeDetector") {
                name == "calculateMinSize"
            }.hookBefore {
                hasEnable("unlock_unlimited_cropping") {
                    it.result = 0
                }
            }
        } else {
            var resizeDetector = 'a'
            for (i in 0..25) {
                try {
                    findMethod("com.miui.gallery.editor.photo.screen.crop.ScreenCropView\$${resizeDetector}") {
                        returnType == Int::class.javaPrimitiveType && parameterCount == 0
                    }.hookBefore {
                        hasEnable("unlock_unlimited_cropping") {
                            it.result = 0
                        }
                    }
                } catch (t: Throwable) {
                    resizeDetector++
                }
            }
        }
        val resizeDetectorClass1 = loadClassOrNull("com.miui.gallery.editor.photo.screen.crop.ScreenCropView\$ResizeDetector")
        if (resizeDetectorClass1 != null) {
            findMethod("com.miui.gallery.editor.photo.screen.crop.ScreenCropView\$ResizeDetector") {
                name == "calculateMinSize"
            }.hookBefore {
                hasEnable("unlock_unlimited_cropping") {
                    it.result = 0
                }
            }
        } else {
            var resizeDetector1 = 'a'
            for (i in 0..25) {
                try {
                    findMethod("com.miui.gallery.editor.photo.core.imports.obsoletes.Crop\$${resizeDetector1}") {
                        returnType == Int::class.javaPrimitiveType && parameterCount == 0
                    }.hookBefore {
                        hasEnable("unlock_unlimited_cropping") {
                            it.result = 0
                        }
                    }
                } catch (_: Throwable) {
                    resizeDetector1++
                }
            }
        }
    }

}