package com.lt2333.simplicitytools.hook.app.systemui

import android.widget.TextView
import com.lt2333.simplicitytools.util.XSPUtils
import com.lt2333.simplicitytools.util.findClass
import com.lt2333.simplicitytools.util.hookAfterMethod
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object HideNetworkSpeedSplitter: HookRegister() {

    override fun init() {
        val networkSpeedSplitterClass = "com.android.systemui.statusbar.views.NetworkSpeedSplitter".findClass(getDefaultClassLoader())
        networkSpeedSplitterClass.hookAfterMethod("init") {
            if (XSPUtils.getBoolean("hide_network_speed_splitter", false)) {
                val textView = it.thisObject as TextView
                textView.text = " "
            }
        }
    }

}
