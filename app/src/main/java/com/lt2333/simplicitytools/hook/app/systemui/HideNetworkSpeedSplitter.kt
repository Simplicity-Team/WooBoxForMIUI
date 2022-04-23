package com.lt2333.simplicitytools.hook.app.systemui

import android.widget.TextView
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object HideNetworkSpeedSplitter : HookRegister() {
    override fun init() {
        findMethod("com.android.systemui.statusbar.views.NetworkSpeedSplitter") {
            name == "init"
        }.hookAfter {
            hasEnable("hide_network_speed_splitter") {
                val textView = it.thisObject as TextView
                textView.text = " "
            }
        }
    }
}
