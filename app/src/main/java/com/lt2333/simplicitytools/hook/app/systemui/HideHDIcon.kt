package com.lt2333.simplicitytools.hook.app.systemui

import android.view.View
import android.widget.ImageView
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.getObject
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook

object HideHDIcon : HookRegister() {
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
        hasEnable("hide_big_hd_icon") {
            (it.thisObject.getObject("mVolte") as ImageView).visibility = View.GONE
        }
        hasEnable("hide_small_hd_icon") {
            (it.thisObject.getObject("mSmallHd") as ImageView).visibility = View.GONE
        }
        hasEnable("hide_hd_no_service_icon") {
            (it.thisObject.getObject("mVolteNoService") as ImageView).visibility =
                View.GONE
        }
    }
}