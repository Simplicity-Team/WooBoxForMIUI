package com.lt2333.simplicitytools.hooks.apps

import android.os.Build
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

object UpdaterHooker: YukiBaseHooker() {
    override fun onHook() {
        when (Build.VERSION.SDK_INT) {
            Build.VERSION_CODES.TIRAMISU -> {
            }

            Build.VERSION_CODES.S -> {
            }
        }
    }
}