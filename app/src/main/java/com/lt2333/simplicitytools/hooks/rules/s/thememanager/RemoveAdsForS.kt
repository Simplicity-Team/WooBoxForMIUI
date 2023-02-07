package com.lt2333.simplicitytools.hooks.rules.s.thememanager

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister

object RemoveAdsForS : HookRegister() {

    override fun init() {
        findMethod("com.android.thememanager.basemodule.ad.model.AdInfoResponse") {
            name == "isAdValid" && parameterCount == 1
        }.hookBefore {
            hasEnable("remove_thememanager_ads") {
                it.result = false
            }
        }
    }

}