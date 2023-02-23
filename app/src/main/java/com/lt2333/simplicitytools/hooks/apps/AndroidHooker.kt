package com.lt2333.simplicitytools.hooks.apps

import android.os.Build
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.lt2333.simplicitytools.hooks.rules.all.android.DisableFlagSecureForAll
import com.lt2333.simplicitytools.hooks.rules.all.corepatch.CorePatchMainHook

object AndroidHooker: YukiBaseHooker() {
    override fun onHook() {
        when (Build.VERSION.SDK_INT) {
            Build.VERSION_CODES.TIRAMISU -> {
                loadHooker(DisableFlagSecureForAll)  //允许截图
            }

            Build.VERSION_CODES.S -> {
                loadHooker(DisableFlagSecureForAll)  //允许截图
            }
        }
    }

}