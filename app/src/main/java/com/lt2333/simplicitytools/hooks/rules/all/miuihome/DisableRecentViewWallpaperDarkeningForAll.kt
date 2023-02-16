package com.lt2333.simplicitytools.hooks.rules.all.miuihome

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.github.kyuubiran.ezxhelper.utils.putObject
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister

object DisableRecentViewWallpaperDarkeningForAll : HookRegister() {
    override fun init() = hasEnable("miuihome_recentwiew_wallpaper_darkening") {
        findMethod("com.miui.home.recents.DimLayer") {
            name == "dim" && parameterCount == 3
        }.hookBefore {
            it.args[0] = 0.0f
            it.thisObject.putObject("mCurrentAlpha", 0.0f)
        }
    }

}
