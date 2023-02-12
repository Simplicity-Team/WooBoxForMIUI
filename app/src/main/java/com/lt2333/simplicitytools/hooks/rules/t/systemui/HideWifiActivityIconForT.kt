package com.lt2333.simplicitytools.hooks.rules.t.systemui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.getObjectAs
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook

object HideWifiActivityIconForT : HookRegister() {

    override fun init() {
        findMethod("com.android.systemui.statusbar.StatusBarWifiView") {
            name == "initViewState" && parameterCount == 1
        }.hookAfter {
            hide(it)
        }

        findMethod("com.android.systemui.statusbar.StatusBarWifiView") {
            name == "updateState" && parameterCount == 1
        }.hookAfter {
            hide(it)
        }
    }

    private fun hide(it: XC_MethodHook.MethodHookParam) {
        //隐藏WIFI箭头
        hasEnable("hide_wifi_activity_icon") {
            (it.thisObject.getObjectAs<ImageView>("mWifiActivityView")).visibility =
                View.INVISIBLE
        }
        //隐藏WIFI标准图标
        hasEnable("hide_wifi_standard_icon") {
            (it.thisObject.getObjectAs<TextView>("mWifiStandardView")).visibility =
                View.INVISIBLE
        }
    }

}