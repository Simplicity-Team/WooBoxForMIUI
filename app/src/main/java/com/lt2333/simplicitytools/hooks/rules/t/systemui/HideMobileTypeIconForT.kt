package com.lt2333.simplicitytools.hooks.rules.t.systemui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.getObjectAs
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.lt2333.simplicitytools.utils.XSPUtils
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook

object HideMobileTypeIconForT : HookRegister() {

    private val isBigType = XSPUtils.getBoolean("big_mobile_type_icon", false)

    override fun init() {
        findMethod("com.android.systemui.statusbar.StatusBarMobileView") {
            name == "initViewState" && parameterCount == 1
        }.hookAfter {
            hideMobileTypeIcon(it)
        }

        findMethod("com.android.systemui.statusbar.StatusBarMobileView") {
            name == "updateState" && parameterCount == 1
        }.hookAfter {
            hideMobileTypeIcon(it)
        }
    }

    private fun hideMobileTypeIcon(it: XC_MethodHook.MethodHookParam) {
        hasEnable("hide_mobile_type_icon") {
            if (isBigType) {
                (it.thisObject.getObjectAs<ImageView>("mMobileType")).visibility =
                    View.GONE
                (it.thisObject.getObjectAs<ImageView>("mMobileTypeImage")).visibility =
                    View.GONE
                (it.thisObject.getObjectAs<TextView>("mMobileTypeSingle")).visibility =
                    View.GONE
            } else {
                (it.thisObject.getObjectAs<ImageView>("mMobileType")).visibility =
                    View.INVISIBLE
                (it.thisObject.getObjectAs<ImageView>("mMobileTypeImage")).visibility =
                    View.INVISIBLE
                (it.thisObject.getObjectAs<TextView>("mMobileTypeSingle")).visibility =
                    View.INVISIBLE
            }
        }
    }

}