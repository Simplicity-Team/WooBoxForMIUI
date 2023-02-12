package com.lt2333.simplicitytools.hooks.rules.all.cast

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister

object ForceSupportSendAppForAll : HookRegister() {

    override fun init() = hasEnable("force_support_send_app") {
        findMethod("com.xiaomi.mirror.synergy.MiuiSynergySdk") {
            name == "isSupportSendApp"
        }.hookAfter {
            it.result = true
        }
    }
}