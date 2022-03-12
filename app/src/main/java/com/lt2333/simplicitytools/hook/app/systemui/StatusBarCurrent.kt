package com.lt2333.simplicitytools.hook.app.systemui

import android.view.ViewGroup
import android.widget.TextView
import com.lt2333.simplicitytools.util.findClass
import com.lt2333.simplicitytools.util.hookAfterMethod
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

class StatusBarCurrent :IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val miuiPhoneStatusBarViewClass =
            "com.android.systemui.statusbar.phone.MiuiPhoneStatusBarView".findClass(lpparam.classLoader)
        miuiPhoneStatusBarViewClass.hookAfterMethod("onFinishInflate") { param ->
            val viewGroup = param.thisObject as ViewGroup
            val textView = TextView(viewGroup.context).also {
                it.text = "Text"
                it.textSize = 14F
            }
            viewGroup.addView(textView)
        }
    }
}