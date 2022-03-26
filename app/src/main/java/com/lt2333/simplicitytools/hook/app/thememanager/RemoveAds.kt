package com.lt2333.simplicitytools.hook.app.thememanager

import com.lt2333.simplicitytools.util.findClass
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookBeforeMethod
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object RemoveAds : HookRegister() {

    override fun init() {
        "com.android.thememanager.basemodule.ad.model.AdInfoResponse".hookBeforeMethod(getDefaultClassLoader(), "isAdValid", "com.android.thememanager.basemodule.ad.model.AdInfo".findClass(getDefaultClassLoader())) {
            hasEnable("remove_thememanager_ads") {
                it.result = false
            }
        }
    }

}