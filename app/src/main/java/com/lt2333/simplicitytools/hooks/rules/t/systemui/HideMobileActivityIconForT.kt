package com.lt2333.simplicitytools.hooks.rules.t.systemui

import android.view.View
import android.widget.ImageView
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.getObjectAs
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook

object HideMobileActivityIconForT : HookRegister() {

    override fun init() {
        findMethod("com.android.systemui.statusbar.StatusBarMobileView") {
            name == "initViewState" && parameterCount == 1
        }.hookAfter {
            hide(it)
        }

        findMethod("com.android.systemui.statusbar.StatusBarMobileView") {
            name == "updateState" && parameterCount == 1
        }.hookAfter {
            hide(it)
        }
    }

    private fun hide(it: XC_MethodHook.MethodHookParam) {
        hasEnable("hide_mobile_activity_icon") {
            (it.thisObject.getObjectAs<ImageView>("mLeftInOut")).visibility = View.GONE
            (it.thisObject.getObjectAs<ImageView>("mRightInOut")).visibility = View.GONE
        }
    }

}