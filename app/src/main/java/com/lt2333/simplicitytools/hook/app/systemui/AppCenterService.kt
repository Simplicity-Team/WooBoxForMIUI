package com.lt2333.simplicitytools.hook.app.systemui

import android.app.Application
import android.content.Context
import com.lt2333.simplicitytools.util.hookAfterMethod
import com.lt2333.simplicitytools.util.xposed.base.HookRegister
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics

object AppCenterService : HookRegister() {

    override fun init() {
        Application::class.java.hookAfterMethod("attach", Context::class.java) {
            runCatching {
                AppCenter.start(it.thisObject as Application, "ae2037d3-9914-4e0c-b02b-f9b2bb2574e5", Analytics::class.java)
            }
        }
    }

}