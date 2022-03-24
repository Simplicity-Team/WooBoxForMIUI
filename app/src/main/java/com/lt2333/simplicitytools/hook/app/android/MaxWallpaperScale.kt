package com.lt2333.simplicitytools.hook.app.android

import com.lt2333.simplicitytools.util.XSPUtils
import com.lt2333.simplicitytools.util.hookBeforeMethod
import com.lt2333.simplicitytools.util.setFloatField
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

class MaxWallpaperScale : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {

        "com.android.server.wm.WallpaperController".hookBeforeMethod(lpparam.classLoader, "zoomOutToScale", Float::class.java) {
            val value = XSPUtils.getFloat("max_wallpaper_scale", 1.1f)
            it.thisObject.setFloatField("mMaxWallpaperScale", value)
        }
    }
}