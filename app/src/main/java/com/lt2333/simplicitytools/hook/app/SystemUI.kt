package com.lt2333.simplicitytools.hook.app

import com.lt2333.simplicitytools.hook.app.systemui.*
import com.lt2333.simplicitytools.util.xposed.base.AppRegister
import de.robv.android.xposed.callbacks.XC_LoadPackage

object SystemUI: AppRegister() {
    override val packageName: String = "com.android.systemui"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        autoInitHooks(lpparam,
            HideStatusBarIcon, //隐藏状态栏图标
            HideBatteryIcon, //隐藏电池
            HideHDIcon, //隐藏HD图标
            HideSimIcon, //隐藏SIM卡图标
            HideMobileActivityIcon, //隐藏移动箭头图标
            HideMobileTypeIcon, //隐藏移动类型图标
            HideStatusBarNetworkSpeedSecond, //隐藏状态栏网速/s
            HideWifiActivityIcon, //隐藏WIFI活动箭头图标
            MaximumNumberOfNotificationIcons, //通知图标上限
            StatusBarNetworkSpeedRefreshSpeed, //状态栏网速秒刷新
            StatusBarTimeCustomization, //状态栏时钟自定义
            RemoveTheLeftSideOfTheLockScreen, //移除锁屏负一屏功能
            RemoveLockScreenCamera, //移除锁屏相机功能
            NotificationWeather, //通知面板天气
            NewNotificationWeather, // 新控制中心天气
            OldNotificationWeather,
            ControlCenterWeather, //控制中心天气
            //StatusBarCurrent, //TODO：状态栏电流
            StatusBarLayout, //状态栏布局
            HideNetworkSpeedSplitter, //隐藏时钟与实时网速之间的分隔符
            WaveCharge, //Alpha充电动画
            LockScreenCurrent, //锁屏电流
            LockScreenDoubleTapToSleep, //锁屏下双击锁屏
            StatusBarDoubleTapToSleep, //双击状态栏锁屏
            OldQSCustom, //旧版快速设置自定义
            DoubleLineNetworkSpeed, //双排网速
            StatusBarBigMobileTypeIcon, //大移动类型
            BatteryPercentage, //电量百分比
            CustomMobileTypeText, //自定义移动类型文本
            CanNotificationSlide, //允许下滑通知打开小窗
            LockScreenClockDisplaySeconds, // 锁屏时钟显示秒
            ShowWifiStandard, // 显示 WIFI 角标
            NoPasswordHook, // 显示 WIFI 角标
        )
    }
}