package com.lt2333.simplicitytools.hook.app.systemui

import android.view.View
import android.widget.LinearLayout
import com.lt2333.simplicitytools.util.XSPUtils
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class RemoveLockScreenCamera : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {

        //屏蔽右下角组件显示
        val classIfExists1 = XposedHelpers.findClassIfExists(
            "com.android.systemui.statusbar.phone.KeyguardBottomAreaView",
            lpparam.classLoader
        )
        XposedHelpers.findAndHookMethod(
            classIfExists1,
            "onFinishInflate",
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    if (XSPUtils.getBoolean("remove_lock_screen_camera", false)) {
                        val view = XposedHelpers.getObjectField(
                            param.thisObject,
                            "mRightAffordanceViewLayout"
                        ) as LinearLayout
                        view.visibility = View.GONE
                    }
                }
            })


        //屏蔽滑动撞墙动画
        val classIfExists2 = XposedHelpers.findClassIfExists(
            "com.android.keyguard.KeyguardMoveRightController",
            lpparam.classLoader
        )

        XposedHelpers.findAndHookMethod(
            classIfExists2,
            "onTouchMove",
            Float::class.java,
            Float::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    if (XSPUtils.getBoolean("remove_lock_screen_camera", false)) {
                        param.result = false
                    }
                }
            })
        XposedHelpers.findAndHookMethod(
            classIfExists2,
            "reset",
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    if (XSPUtils.getBoolean("remove_lock_screen_camera", false)) {
                        param.result = null
                    }
                }
            })
    }
}

