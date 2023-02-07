package com.lt2333.simplicitytools.hooks.rules.s.android

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.github.kyuubiran.ezxhelper.utils.putObject
import com.lt2333.simplicitytools.utils.XSPUtils
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister

object MaxWallpaperScaleForS : HookRegister() {
    override fun init() {
        findMethod("com.android.server.wm.WallpaperController") {
            name == "zoomOutToScale" && parameterTypes[0] == Float::class.java
        }.hookBefore {
            val value = XSPUtils.getFloat("max_wallpaper_scale", 1.1f)
            it.thisObject.putObject("mMaxWallpaperScale", value)
        }
    }

}