package com.lt2333.simplicitytools.hooks.apps

import android.os.Build
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.lt2333.simplicitytools.hooks.rules.all.miuihome.ShortcutAddSmallWindowForAll

object MiuiHomeHooker: YukiBaseHooker() {
    override fun onHook() {
        when (Build.VERSION.SDK_INT) {
            Build.VERSION_CODES.TIRAMISU -> {
                loadHooker(ShortcutAddSmallWindowForAll) //快捷方式添加小窗
            }

            Build.VERSION_CODES.S -> {
                loadHooker(ShortcutAddSmallWindowForAll) //快捷方式添加小窗
            }
        }
    }
}