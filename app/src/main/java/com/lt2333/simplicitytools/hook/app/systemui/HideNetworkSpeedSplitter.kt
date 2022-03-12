package com.lt2333.simplicitytools.hook.app.systemui

import android.widget.TextView
import com.lt2333.simplicitytools.util.XSPUtils
import com.lt2333.simplicitytools.util.findClass
import com.lt2333.simplicitytools.util.hookAfterMethod
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

class HideNetworkSpeedSplitter : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val networkSpeedSplitterClass = "com.android.systemui.statusbar.views.NetworkSpeedSplitter".findClass(lpparam.classLoader)
        networkSpeedSplitterClass.hookAfterMethod("init") {
            if (XSPUtils.getBoolean("hide_network_speed_splitter", false)) {
                val textView = it.thisObject as TextView
                textView.text = " "
            }
        }
    }
}
