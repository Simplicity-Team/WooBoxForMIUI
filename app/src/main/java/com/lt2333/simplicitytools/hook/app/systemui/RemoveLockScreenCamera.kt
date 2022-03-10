package com.lt2333.simplicitytools.hook.app.systemui

import android.view.View
import android.widget.LinearLayout
import com.lt2333.simplicitytools.util.getObjectField
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookAfterMethod
import com.lt2333.simplicitytools.util.hookBeforeMethod
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

class RemoveLockScreenCamera : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {

        //屏蔽右下角组件显示
        "com.android.systemui.statusbar.phone.KeyguardBottomAreaView".hookAfterMethod(lpparam.classLoader, "onFinishInflate") {
            hasEnable("remove_lock_screen_camera") {
                (it.thisObject.getObjectField("mRightAffordanceViewLayout") as LinearLayout).visibility = View.GONE
            }
        }

        //屏蔽滑动撞墙动画
        "com.android.keyguard.KeyguardMoveRightController".hookBeforeMethod(lpparam.classLoader, "onTouchMove", Float::class.java, Float::class.java) {
            hasEnable("remove_lock_screen_camera") {
                it.result = false
            }
        }
        "com.android.keyguard.KeyguardMoveRightController".hookBeforeMethod(lpparam.classLoader, "reset") {
            hasEnable("remove_lock_screen_camera") {
                it.result = null
            }
        }
    }
}

