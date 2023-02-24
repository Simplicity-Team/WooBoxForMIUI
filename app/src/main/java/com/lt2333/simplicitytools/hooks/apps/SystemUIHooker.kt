package com.lt2333.simplicitytools.hooks.apps

import android.os.Build
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.lt2333.simplicitytools.hooks.rules.t.systemui.*

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
            }

            Build.VERSION_CODES.S -> {
            }
        }
    }
}