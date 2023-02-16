package com.lt2333.simplicitytools.hooks.rules.all.miuihome

import android.animation.ObjectAnimator
import android.view.View
import com.github.kyuubiran.ezxhelper.utils.args
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.getObject
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.github.kyuubiran.ezxhelper.utils.hookReplace
import com.github.kyuubiran.ezxhelper.utils.invokeMethod
import com.github.kyuubiran.ezxhelper.utils.invokeStaticMethod
import com.github.kyuubiran.ezxhelper.utils.loadClass
import com.github.kyuubiran.ezxhelper.utils.putObject
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister


object ModifyRecentViewRemoveCardAnimForAll : HookRegister() {
    override fun init() = hasEnable("miuihome_recentview_remove_card_animation") {
        findMethod("com.miui.home.recents.views.SwipeHelperForRecents") {
            name == "onTouchEvent" && parameterCount == 1
        }.hookAfter {
            val mCurrView = it.thisObject.getObject("mCurrView") as View?
            if (mCurrView != null) {
                mCurrView.alpha = 1f
                mCurrView.scaleX = 1f
                mCurrView.scaleY = 1f
            }
        }
        findMethod("com.miui.home.recents.TaskStackViewLayoutStyleHorizontal") {
            name == "createScaleDismissAnimation" && parameterCount == 2
        }.hookReplace {
            val view = it.args[0] as View
            val getScreenHeight = loadClass("com.miui.home.launcher.DeviceConfig").invokeStaticMethod("getScreenHeight") as Int
            val ofFloat = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, view.translationY, -getScreenHeight * 1.1484375f)
            ofFloat.duration = 200
            return@hookReplace ofFloat
        }
        findMethod("com.miui.home.recents.views.VerticalSwipe") {
            name == "calculate" && parameterCount == 1
        }.hookAfter {
            val f = it.args[0] as Float
            val asScreenHeightWhenDismiss = loadClass("com.miui.home.recents.views.VerticalSwipe").invokeStaticMethod("getAsScreenHeightWhenDismiss") as Int
            val f2 = f / asScreenHeightWhenDismiss
            val mTaskViewHeight = it.thisObject.getObject("mTaskViewHeight") as Float
            val mCurScale = it.thisObject.getObject("mCurScale") as Float
            val f3: Float = mTaskViewHeight * mCurScale
            val i = if (f2 > 0.0f) 1 else if (f2 == 0.0f) 0 else -1
            val afterFrictionValue: Float = it.thisObject.invokeMethod("afterFrictionValue", args(f, asScreenHeightWhenDismiss)) as Float
            if (i < 0) it.thisObject.putObject("mCurTransY", (mTaskViewHeight / 2.0f + afterFrictionValue * 2) - (f3 / 2.0f))
        }
    }

}
