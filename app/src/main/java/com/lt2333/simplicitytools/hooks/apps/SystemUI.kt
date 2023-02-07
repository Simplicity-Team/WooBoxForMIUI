package com.lt2333.simplicitytools.hooks.apps

import android.os.Build
import com.lt2333.simplicitytools.hooks.rules.s.systemui.*
import com.lt2333.simplicitytools.utils.xposed.base.AppRegister
import de.robv.android.xposed.callbacks.XC_LoadPackage

object SystemUI: AppRegister() {
    override val packageName: String = "com.android.systemui"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        when (Build.VERSION.SDK_INT) {
            Build.VERSION_CODES.TIRAMISU -> {
                autoInitHooks(lpparam,
                    HideStatusBarIconForS, //隐藏状态栏图标
                    HideBatteryIconForS, //隐藏电池
                    HideHDIconForS, //隐藏HD图标
                    HideSimIconForS, //隐藏SIM卡图标
                    HideMobileActivityIconForS, //隐藏移动箭头图标
                    HideMobileTypeIconForS, //隐藏移动类型图标
                    HideStatusBarNetworkSpeedSecondForS, //隐藏状态栏网速/s
                    HideWifiActivityIconForS, //隐藏WIFI活动箭头图标
                    MaximumNumberOfNotificationIconsForS, //通知图标上限
                    StatusBarNetworkSpeedRefreshSpeedForS, //状态栏网速秒刷新
                    StatusBarTimeCustomizationForS, //状态栏时钟自定义
                    RemoveTheLeftSideOfTheLockScreenForS, //移除锁屏负一屏功能
                    RemoveLockScreenCameraForS, //移除锁屏相机功能
                    NotificationWeatherForS, //通知面板天气
                    NewNotificationWeatherForS, // 新控制中心天气
                    OldNotificationWeatherForS,
                    ControlCenterWeatherForS, //控制中心天气
                    //StatusBarCurrent, //TODO：状态栏电流
                    StatusBarLayoutForS, //状态栏布局
                    HideNetworkSpeedSplitterForS, //隐藏时钟与实时网速之间的分隔符
                    WaveChargeForS, //Alpha充电动画
                    LockScreenCurrentForS, //锁屏电流
                    LockScreenDoubleTapToSleepForS, //锁屏下双击锁屏
                    StatusBarDoubleTapToSleepForS, //双击状态栏锁屏
                    OldQSCustomForS, //旧版快速设置自定义
                    DoubleLineNetworkSpeedForS, //双排网速
                    StatusBarBigMobileTypeIconForS, //大移动类型
                    BatteryPercentageForS, //电量百分比
                    CustomMobileTypeTextForS, //自定义移动类型文本
                    CanNotificationSlideForS, //允许下滑通知打开小窗
                    LockScreenClockDisplaySecondsForS, // 锁屏时钟显示秒
                    ShowWifiStandardForS, // 显示 WIFI 角标
                    NoPasswordHookForS, // 显示 WIFI 角标
                )
            }
            Build.VERSION_CODES.S -> {
                autoInitHooks(lpparam,
                    HideStatusBarIconForS, //隐藏状态栏图标
                    HideBatteryIconForS, //隐藏电池
                    HideHDIconForS, //隐藏HD图标
                    HideSimIconForS, //隐藏SIM卡图标
                    HideMobileActivityIconForS, //隐藏移动箭头图标
                    HideMobileTypeIconForS, //隐藏移动类型图标
                    HideStatusBarNetworkSpeedSecondForS, //隐藏状态栏网速/s
                    HideWifiActivityIconForS, //隐藏WIFI活动箭头图标
                    MaximumNumberOfNotificationIconsForS, //通知图标上限
                    StatusBarNetworkSpeedRefreshSpeedForS, //状态栏网速秒刷新
                    StatusBarTimeCustomizationForS, //状态栏时钟自定义
                    RemoveTheLeftSideOfTheLockScreenForS, //移除锁屏负一屏功能
                    RemoveLockScreenCameraForS, //移除锁屏相机功能
                    NotificationWeatherForS, //通知面板天气
                    NewNotificationWeatherForS, // 新控制中心天气
                    OldNotificationWeatherForS,
                    ControlCenterWeatherForS, //控制中心天气
                    //StatusBarCurrent, //TODO：状态栏电流
                    StatusBarLayoutForS, //状态栏布局
                    HideNetworkSpeedSplitterForS, //隐藏时钟与实时网速之间的分隔符
                    WaveChargeForS, //Alpha充电动画
                    LockScreenCurrentForS, //锁屏电流
                    LockScreenDoubleTapToSleepForS, //锁屏下双击锁屏
                    StatusBarDoubleTapToSleepForS, //双击状态栏锁屏
                    OldQSCustomForS, //旧版快速设置自定义
                    DoubleLineNetworkSpeedForS, //双排网速
                    StatusBarBigMobileTypeIconForS, //大移动类型
                    BatteryPercentageForS, //电量百分比
                    CustomMobileTypeTextForS, //自定义移动类型文本
                    CanNotificationSlideForS, //允许下滑通知打开小窗
                    LockScreenClockDisplaySecondsForS, // 锁屏时钟显示秒
                    ShowWifiStandardForS, // 显示 WIFI 角标
                    NoPasswordHookForS, // 显示 WIFI 角标
                )
            }
        }
    }
}