package com.lt2333.simplicitytools.hook.app.cast

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookMethod
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object ForceSupportSendApp : HookRegister() {
    override fun init() {
        hasEnable("force_support_send_app") {
            findMethod("com.xiaomi.mirror.synergy.MiuiSynergySdk") {
                name == "isSupportSendApp"
            }.hookMethod {
                after { param ->
                    param.result = true
                }
            }
        }
    }
}