package com.lt2333.simplicitytools.hooks.rules.t.systemui

import android.widget.TextView
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister

object HideNetworkSpeedSplitterForT : HookRegister() {

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
