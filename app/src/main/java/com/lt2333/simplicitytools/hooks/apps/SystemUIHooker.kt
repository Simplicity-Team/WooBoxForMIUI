package com.lt2333.simplicitytools.hooks.apps

import android.os.Build
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.lt2333.simplicitytools.hooks.rules.t.systemui.HideBatteryIconForT
import com.lt2333.simplicitytools.hooks.rules.t.systemui.HideHDIconForT
import com.lt2333.simplicitytools.hooks.rules.t.systemui.HideMobileActivityIconForT
import com.lt2333.simplicitytools.hooks.rules.t.systemui.HideMobileTypeIconForT
import com.lt2333.simplicitytools.hooks.rules.t.systemui.HideSimIconForT
import com.lt2333.simplicitytools.hooks.rules.t.systemui.HideStatusBarIconForT
import com.lt2333.simplicitytools.hooks.rules.t.systemui.HideStatusBarNetworkSpeedSecondForT
import com.lt2333.simplicitytools.hooks.rules.t.systemui.HideWifiActivityIconForT
import com.lt2333.simplicitytools.hooks.rules.t.systemui.MaximumNumberOfNotificationIconsForT
import com.lt2333.simplicitytools.hooks.rules.t.systemui.NewControlCenterWeatherForT
import com.lt2333.simplicitytools.hooks.rules.t.systemui.NotificationWeatherForT
import com.lt2333.simplicitytools.hooks.rules.t.systemui.OldNotificationWeatherForT
import com.lt2333.simplicitytools.hooks.rules.t.systemui.RemoveLockScreenCameraForT
import com.lt2333.simplicitytools.hooks.rules.t.systemui.RemoveTheLeftSideOfTheLockScreenForT
import com.lt2333.simplicitytools.hooks.rules.t.systemui.StatusBarLayoutForT
import com.lt2333.simplicitytools.hooks.rules.t.systemui.StatusBarNetworkSpeedRefreshSpeedForT
import com.lt2333.simplicitytools.hooks.rules.t.systemui.StatusBarTimeCustomizationForT

object SystemUIHooker : YukiBaseHooker() {
    override fun onHook() {
        when (Build.VERSION.SDK_INT) {
            Build.VERSION_CODES.TIRAMISU -> {
                loadHooker(HideStatusBarIconForT) //隐藏状态栏图标
                loadHooker(HideBatteryIconForT) //隐藏电池
                loadHooker(HideHDIconForT) //隐藏HD图标
                loadHooker(HideSimIconForT) //隐藏SIM卡图标
                loadHooker(HideMobileActivityIconForT) //隐藏移动箭头图标
                loadHooker(HideMobileTypeIconForT) //隐藏移动类型图标
                loadHooker(HideStatusBarNetworkSpeedSecondForT) //隐藏状态栏网速/s
                loadHooker(HideWifiActivityIconForT) //隐藏WIFI活动箭头图标
                loadHooker(MaximumNumberOfNotificationIconsForT) //通知图标上限
                loadHooker(StatusBarNetworkSpeedRefreshSpeedForT) //状态栏网速秒刷新
                loadHooker(StatusBarTimeCustomizationForT) //状态栏时钟自定义
                loadHooker(RemoveTheLeftSideOfTheLockScreenForT) //移除锁屏负一屏功能
                loadHooker(RemoveLockScreenCameraForT) //移除锁屏相机功能
                loadHooker(NotificationWeatherForT) //通知面板天气
                loadHooker(NewControlCenterWeatherForT) //新控制中心天气
                loadHooker(OldNotificationWeatherForT) //经典通知天气
                loadHooker(StatusBarLayoutForT) //状态栏布局
            }

            Build.VERSION_CODES.S -> {
            }
        }
    }
}