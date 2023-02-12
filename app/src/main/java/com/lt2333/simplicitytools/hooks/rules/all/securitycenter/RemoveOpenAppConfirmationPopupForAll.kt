package com.lt2333.simplicitytools.hooks.rules.all.securitycenter

import android.widget.TextView
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister

object RemoveOpenAppConfirmationPopupForAll : HookRegister() {

    override fun init() {
        findMethod("android.widget.TextView") {
            name == "setText" && parameterTypes[0] == CharSequence::class.java
        }.hookAfter {
            hasEnable("remove_open_app_confirmation_popup") {
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
