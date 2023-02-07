package com.lt2333.simplicitytools.hook.app.android

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookAllConstructorAfter
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.github.kyuubiran.ezxhelper.utils.putObject
import com.lt2333.simplicitytools.util.XSPUtils
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object MaxWallpaperScale : HookRegister() {
    override fun init() {
        findMethod("com.android.server.wm.WallpaperController") {
            name == "zoomOutToScale" && parameterTypes[0] == Float::class.java
        }.hookBefore {
            val value = XSPUtils.getFloat("max_wallpaper_scale", 1.2f)
            it.thisObject.putObject("mMaxWallpaperScale", value)
        }
        hookAllConstructorAfter("com.android.server.wm.WallpaperController") {
            val value = XSPUtils.getFloat("max_wallpaper_scale", 1.2f)
            it.thisObject.putObject("mMaxWallpaperScale", value)
        }
    }

}