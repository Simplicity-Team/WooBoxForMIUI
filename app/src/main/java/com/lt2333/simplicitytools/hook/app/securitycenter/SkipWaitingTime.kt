package com.lt2333.simplicitytools.hook.app.securitycenter

import android.widget.TextView
import com.lt2333.simplicitytools.util.XSPUtils
import com.lt2333.simplicitytools.util.findClass
import com.lt2333.simplicitytools.util.hookBeforeMethod
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

class SkipWaitingTime : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {

        val textViewClass = "android.widget.TextView".findClass(lpparam.classLoader)
        textViewClass.hookBeforeMethod(
            "setText",
            CharSequence::class.java,
            TextView.BufferType::class.java,
            Boolean::class.java,
            Int::class.java
        ) {
            if (XSPUtils.getBoolean("skip_waiting_time", false)) {
                if (it.args.isNotEmpty() && it.args[0]?.toString()?.startsWith("确定(") == true
                ) {
                    it.args[0] = "确定"
                }
            }
        }

        textViewClass.hookBeforeMethod(
            "setEnabled",
            Boolean::class.java
        ) {
            if (XSPUtils.getBoolean("skip_waiting_time", false)) {
                it.args[0] = true
            }
        }
    }
}