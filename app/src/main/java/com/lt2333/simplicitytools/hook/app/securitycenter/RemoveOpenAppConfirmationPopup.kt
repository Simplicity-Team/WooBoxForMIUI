package com.lt2333.simplicitytools.hook.app.securitycenter

import android.widget.TextView
import com.lt2333.simplicitytools.util.XSPUtils
import com.lt2333.simplicitytools.util.findClass
import com.lt2333.simplicitytools.util.hookAfterMethod
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

class RemoveOpenAppConfirmationPopup : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val textViewClass = "android.widget.TextView".findClass(lpparam.classLoader)
        textViewClass.hookAfterMethod(
            "setText",
            CharSequence::class.java
        ) {
            if (XSPUtils.getBoolean("remove_open_app_confirmation_popup", false)) {
                val textView = it.thisObject as TextView
                if (it.args.isNotEmpty() && it.args[0]?.toString().equals(
                        textView.context.resources.getString(
                            textView.context.resources.getIdentifier(
                                "button_text_accept",
                                "string",
                                textView.context.packageName
                            )
                        )
                    )
                ) {
                    textView.performClick()
                }
            }
        }
    }
}
