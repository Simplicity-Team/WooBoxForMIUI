package com.lt2333.simplicitytools.hooks.apps

import android.os.Build
import com.lt2333.simplicitytools.hooks.rules.all.miuihome.AlwaysShowStatusBarClockForAll
import com.lt2333.simplicitytools.hooks.rules.all.miuihome.DisableRecentViewWallpaperDarkeningForAll
import com.lt2333.simplicitytools.hooks.rules.all.miuihome.DoubleTapToSleepForAll
import com.lt2333.simplicitytools.hooks.rules.all.miuihome.ModifyRecentViewRemoveCardAnimForAll
import com.lt2333.simplicitytools.hooks.rules.all.miuihome.RemoveSmallWindowRestrictionForMiuiHomeForAll
import com.lt2333.simplicitytools.hooks.rules.all.miuihome.ScrollIconNameForAll
import com.lt2333.simplicitytools.hooks.rules.all.miuihome.ShortcutAddSmallWindowForAll
import com.lt2333.simplicitytools.utils.xposed.base.AppRegister
import de.robv.android.xposed.callbacks.XC_LoadPackage

object MiuiHome : AppRegister() {
    override val packageName: String = "com.miui.home"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        when (Build.VERSION.SDK_INT) {
            Build.VERSION_CODES.TIRAMISU -> {
                autoInitHooks(
                    lpparam,
                    AlwaysShowStatusBarClockForAll, //时钟显示时钟
                    DoubleTapToSleepForAll, //双击锁屏
                    DisableRecentViewWallpaperDarkeningForAll, //取消后台壁纸压暗效果
                    ModifyRecentViewRemoveCardAnimForAll, //横向排布后台划卡动画
                    ScrollIconNameForAll, //滚动显示应用标题
                    RemoveSmallWindowRestrictionForMiuiHomeForAll, //取消小窗限制
                    ShortcutAddSmallWindowForAll, //快捷菜单添加小窗
                )
            }

            Build.VERSION_CODES.S -> {
                autoInitHooks(
                    lpparam,
                    AlwaysShowStatusBarClockForAll, //时钟显示时钟
                    DoubleTapToSleepForAll, //双击锁屏
                    DisableRecentViewWallpaperDarkeningForAll, //取消后台壁纸压暗效果
                    ModifyRecentViewRemoveCardAnimForAll, //横向排布后台划卡动画
                    ScrollIconNameForAll, //滚动显示应用标题
                    RemoveSmallWindowRestrictionForMiuiHomeForAll, //取消小窗限制
                    ShortcutAddSmallWindowForAll, //快捷菜单添加小窗
                )
            }
        }
    }
}