package com.lt2333.simplicitytools.hooks.rules.t.android

import com.github.kyuubiran.ezxhelper.utils.hookAllConstructorAfter
import com.github.kyuubiran.ezxhelper.utils.putObject
import com.lt2333.simplicitytools.utils.XSPUtils
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister

object MaxWallpaperScaleForT : HookRegister() {
    override fun init() {
        hookAllConstructorAfter("com.android.server.wm.WallpaperController") {
            val value = XSPUtils.getFloat("max_wallpaper_scale", 1.2f)
            it.thisObject.putObject("mMaxWallpaperScale", value)
        }
    }
}