package com.lt2333.simplicitytools.hook.app.systemui

import android.view.View
import android.widget.LinearLayout
import com.lt2333.simplicitytools.util.getObjectField
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookAfterMethod
import com.lt2333.simplicitytools.util.hookBeforeMethod
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object RemoveLockScreenCamera: HookRegister() {

    override fun init() {
        //屏蔽右下角组件显示
        "com.android.systemui.statusbar.phone.KeyguardBottomAreaView".hookAfterMethod(getDefaultClassLoader(), "onFinishInflate") {
            hasEnable("remove_lock_screen_camera") {
                (it.thisObject.getObjectField("mRightAffordanceViewLayout") as LinearLayout).visibility = View.GONE
            }
        }

        //屏蔽滑动撞墙动画
        "com.android.keyguard.KeyguardMoveRightController".hookBeforeMethod(getDefaultClassLoader(), "onTouchMove", Float::class.java, Float::class.java) {
            hasEnable("remove_lock_screen_camera") {
                it.result = false
            }
        }
        "com.android.keyguard.KeyguardMoveRightController".hookBeforeMethod(getDefaultClassLoader(), "reset") {
            hasEnable("remove_lock_screen_camera") {
                it.result = null
            }
        }
    }

}

