package com.lt2333.simplicitytools;

import android.content.Intent;
import android.util.Log;

import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {

    XSharedPreferences prefs = new XSharedPreferences(BuildConfig.APPLICATION_ID, "config");
    String TAG = "MainHook";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {

        //电量与性能
        if (lpparam.packageName.equals("com.miui.powerkeeper")) {

            //锁定最高刷新率
            try {
                XposedHelpers.findAndHookMethod(
                        "com.miui.powerkeeper.statemachine.DisplayFrameSetting", lpparam.classLoader,
                        "setScreenEffect",
                        String.class, int.class, int.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) {
                                if (prefs.hasFileChanged()) {
                                    prefs.reload();
                                }
                                if (prefs.getBoolean("lock_max_fps", false)) {
                                    param.setResult(null);
                                }
                            }
                        });
            } catch (Exception e) {
                XposedBridge.log(e);
            }
        }

        //系统界面
        if (lpparam.packageName.equals("com.android.systemui")) {

            //隐藏无SIM卡图标
            try {
                XposedHelpers.findAndHookMethod(
                        "com.android.systemui.statusbar.phone.MiuiStatusBarSignalPolicy", lpparam.classLoader,
                        "setNoSims", boolean.class, boolean.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) {
                                if (prefs.hasFileChanged()) {
                                    prefs.reload();
                                }
                                if (prefs.getBoolean("hide_no_sim_icon", false)) {
                                    param.setResult(null);
                                }
                            }
                        });
            } catch (Exception e) {
                XposedBridge.log(e);
            }
            //隐藏VPN图标
            try {
                XposedHelpers.findAndHookMethod(
                        "com.android.systemui.statusbar.phone.StatusBarSignalPolicy", lpparam.classLoader,
                        "updateVpn",
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) {
                                if (prefs.hasFileChanged()) {
                                    prefs.reload();
                                }
                                if (prefs.getBoolean("hide_vpn_icon", false)) {
                                    param.setResult(null);
                                }
                            }
                        });
            } catch (Exception e) {
                XposedBridge.log(e);
            }
            //隐藏飞行模式图标
            try {
                XposedHelpers.findAndHookMethod(
                        "com.android.systemui.statusbar.phone.StatusBarSignalPolicy", lpparam.classLoader,
                        "setIsAirplaneMode", XposedHelpers.findClass("com.android.systemui.statusbar.policy.NetworkController$IconState", lpparam.classLoader),
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) {
                                if (prefs.hasFileChanged()) {
                                    prefs.reload();
                                }
                                if (prefs.getBoolean("hide_airplane_icon", false)) {
                                    param.setResult(null);
                                }
                            }
                        });
            } catch (Exception e) {
                XposedBridge.log(e);
            }
            //隐藏WIFI图标
            try {
                XposedHelpers.findAndHookMethod(
                        "com.android.systemui.statusbar.phone.MiuiStatusBarSignalPolicy", lpparam.classLoader,
                        "updateWifiIconWithState", XposedHelpers.findClass("com.android.systemui.statusbar.phone.StatusBarSignalPolicy$WifiIconState", lpparam.classLoader),
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) {
                                if (prefs.hasFileChanged()) {
                                    prefs.reload();
                                }
                                if (prefs.getBoolean("hide_wifi_icon", false)) {
                                    param.setResult(null);
                                }
                            }
                        });
            } catch (Exception e) {
                XposedBridge.log(e);
            }

            //隐藏蓝牙图标
            try {
                XposedHelpers.findAndHookMethod(
                        "com.android.systemui.statusbar.phone.MiuiPhoneStatusBarPolicy", lpparam.classLoader,
                        "updateBluetooth", String.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) {
                                if (prefs.hasFileChanged()) {
                                    prefs.reload();
                                }
                                if (prefs.getBoolean("hide_bluetooth_icon", false)) {
                                    param.setResult(null);
                                }
                            }
                        });
            } catch (Exception e) {
                XposedBridge.log(e);
            }
            //隐藏 声音、勿扰 图标
            try {
                XposedHelpers.findAndHookMethod(
                        "com.android.systemui.statusbar.phone.MiuiPhoneStatusBarPolicy", lpparam.classLoader,
                        "updateVolumeZen",
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) {
                                if (prefs.hasFileChanged()) {
                                    prefs.reload();
                                }
                                if (prefs.getBoolean("hide_volume_zen_icon", false)) {
                                    param.setResult(null);
                                }
                            }
                        });
            } catch (Exception e) {
                XposedBridge.log(e);
            }
            //隐藏 闹钟 图标
            try {
                XposedHelpers.findAndHookMethod(
                        "com.android.systemui.statusbar.phone.MiuiPhoneStatusBarPolicy", lpparam.classLoader,
                        "onMiuiAlarmChanged",
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) {
                                if (prefs.hasFileChanged()) {
                                    prefs.reload();
                                }
                                if (prefs.getBoolean("hide_alarm_icon", false)) {
                                    param.setResult(null);
                                }
                            }
                        });
            } catch (Exception e) {
                XposedBridge.log(e);
            }
            //隐藏 耳机 图标
            try {
                XposedHelpers.findAndHookMethod(
                        "com.android.systemui.statusbar.phone.MiuiPhoneStatusBarPolicy", lpparam.classLoader,
                        "updateHeadsetPlug", Intent.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) {
                                if (prefs.hasFileChanged()) {
                                    prefs.reload();
                                }
                                if (prefs.getBoolean("hide_headset_icon", false)) {
                                    param.setResult(null);
                                }
                            }
                        });
            } catch (Exception e) {
                XposedBridge.log(e);
            }
            //隐藏 蓝牙电量 图标
            try {
                XposedHelpers.findAndHookMethod(
                        "com.android.systemui.statusbar.phone.MiuiPhoneStatusBarPolicy", lpparam.classLoader,
                        "updateBluetoothHandsfreeBattery", Intent.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) {
                                if (prefs.hasFileChanged()) {
                                    prefs.reload();
                                }
                                if (prefs.getBoolean("hide_bluetooth_battery_icon", false)) {
                                    param.setResult(null);
                                }
                            }
                        });
            } catch (Exception e) {
                XposedBridge.log(e);
            }
            //隐藏卡一卡二
            try {
                XposedHelpers.findAndHookMethod(
                        "com.android.systemui.statusbar.phone.StatusBarSignalPolicy", lpparam.classLoader,
                        "hasCorrectSubs", List.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) {
                                if (prefs.hasFileChanged()) {
                                    prefs.reload();
                                }
                                List list = ((List<?>) param.args[0]);
                                int size = list.size();
                                Log.d(TAG, "beforeHookedMethod: " + size);
                                if (size == 2 && prefs.getBoolean("hide_sim_two_icon", false)) {
                                    list.remove(1);
                                }

                                if (size >= 1 && prefs.getBoolean("hide_sim_one_icon", false)) {
                                    list.remove(0);
                                }
                            }
                        });
            } catch (Exception e) {
                XposedBridge.log(e);
            }

        }

        //MIUI桌面
        if (lpparam.packageName.equals("com.miui.home")) {
            //始终显示时钟
            try {
                XposedHelpers.findAndHookMethod(
                        "com.miui.home.launcher.Workspace", lpparam.classLoader,
                        "isScreenHasClockGadget", long.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) {
                                if (prefs.hasFileChanged()) {
                                    prefs.reload();
                                }
                                if (prefs.getBoolean("home_time", false)) {
                                    param.setResult(false);
                                }
                            }
                        });
            } catch (Exception e) {
                XposedBridge.log(e);
            }
        }

        //Andorid 框架
        if (lpparam.packageName.equals("android")) {
            //允许截图
            try {
                XposedHelpers.findAndHookMethod("com.android.server.wm.WindowState", lpparam.classLoader,
                        "isSecureLocked",
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) {
                                if (prefs.hasFileChanged()) {
                                    prefs.reload();
                                }
                                if (prefs.getBoolean("disable_flag_secure", false)) {
                                    param.setResult(false);
                                }
                            }
                        });
            } catch (Exception e) {
                XposedBridge.log(e);
            }
        }

    }

}
