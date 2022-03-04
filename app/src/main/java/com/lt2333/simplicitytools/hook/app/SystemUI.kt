package com.lt2333.simplicitytools.hook.app

import com.lt2333.simplicitytools.hook.app.systemui.*
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class SystemUI : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        XposedBridge.log("成功Hook: " + javaClass.simpleName)
        //隐藏飞行模式图标
        HideAirplaneIcon().handleLoadPackage(lpparam)
        //隐藏闹钟图标
        HideAlarmIcon().handleLoadPackage(lpparam)
        //隐藏电池
        HideBatteryIcon().handleLoadPackage(lpparam)
        //隐藏电池/百分号
        HideBatteryPercentageIcon().handleLoadPackage(lpparam)
        //隐藏蓝牙电量
        HideBluetoothHandsfreeBatteryIcon().handleLoadPackage(lpparam)
        //隐藏蓝牙图标
        HideBluetoothIcon().handleLoadPackage(lpparam)
        //隐藏GPS图标
        HideGPSIcon().handleLoadPackage(lpparam)
        //隐藏HD图标
        HideHDIcon().handleLoadPackage(lpparam)
        //隐藏耳机图标
        HideHeadsetIcon().handleLoadPackage(lpparam)
        //隐藏热点图标
        HideHotspotIcon().handleLoadPackage(lpparam)
        //隐藏无SIM卡图标
        HideNoSimIcon().handleLoadPackage(lpparam)
        //隐藏SIM卡图标
        HideSimIcon().handleLoadPackage(lpparam)
        //隐藏移动箭头图标
        HideMobileActivityIcon().handleLoadPackage(lpparam)
        //隐藏移动类型图标
        HideMobileTypeIcon().handleLoadPackage(lpparam)
        //隐藏辅助WIFI图标
        HideSlaveWifiIcon().handleLoadPackage(lpparam)
        //隐藏状态栏网速/s
        HideStatusBarNetworkSpeedSecond().handleLoadPackage(lpparam)
        //隐藏音量勿扰图标
        HideVolumeZenIcon().handleLoadPackage(lpparam)
        //隐藏VPN图标
        HideVpnIcon().handleLoadPackage(lpparam)
        //隐藏WIFI图标
        HideWifiIcon().handleLoadPackage(lpparam)
        //隐藏WIFI活动箭头图标
        HideWifiActivityIcon().handleLoadPackage(lpparam)
        //移除通知图标上限
        RemoveTheMaximumNumberOfNotificationIcons().handleLoadPackage(lpparam)
        //状态栏网速秒刷新
        StatusBarNetworkSpeedRefreshSpeed().handleLoadPackage(lpparam)
        //状态栏时钟自定义
        StatusBarTimeCustomization().handleLoadPackage(lpparam)
        //移除锁屏负一屏功能
        RemoveTheLeftSideOfTheLockScreen().handleLoadPackage(lpparam)
        //移除锁屏相机功能
        RemoveLockScreenCamera().handleLoadPackage(lpparam)

        //TODO:状态栏天气
        //NotificationWeather().handleLoadPackage(lpparam)
        //TODO：状态栏电流
        //StatusBarCurrent().handleLoadPackage(lpparam)
    }

}
