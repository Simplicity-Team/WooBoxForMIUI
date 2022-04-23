package com.lt2333.simplicitytools.hook.app.systemui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.getObject
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.lt2333.simplicitytools.util.XSPUtils
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook

object HideMobileTypeIcon : HookRegister() {

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
                (it.thisObject.getObject("mMobileType") as TextView).visibility =
                    View.GONE
                (it.thisObject.getObject("mMobileTypeImage") as ImageView).visibility =
                    View.GONE
                (it.thisObject.getObject("mMobileTypeSingle") as TextView).visibility =
                    View.GONE
            } else {
                (it.thisObject.getObject("mMobileType") as TextView).visibility =
                    View.INVISIBLE
                (it.thisObject.getObject("mMobileTypeImage") as ImageView).visibility =
                    View.INVISIBLE
                (it.thisObject.getObject("mMobileTypeSingle") as TextView).visibility =
                    View.INVISIBLE
            }
        }
    }
}