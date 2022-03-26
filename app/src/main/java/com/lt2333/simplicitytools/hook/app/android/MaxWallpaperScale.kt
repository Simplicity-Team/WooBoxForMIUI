package com.lt2333.simplicitytools.hook.app.android

import com.lt2333.simplicitytools.util.XSPUtils
import com.lt2333.simplicitytools.util.hookBeforeMethod
import com.lt2333.simplicitytools.util.setFloatField
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object MaxWallpaperScale : HookRegister() {

    override fun init() {
        "com.android.server.wm.WallpaperController".hookBeforeMethod(getDefaultClassLoader(), "zoomOutToScale", Float::class.java) {
            val value = XSPUtils.getFloat("max_wallpaper_scale", 1.1f)
            it.thisObject.setFloatField("mMaxWallpaperScale", value)
        }
    }

}