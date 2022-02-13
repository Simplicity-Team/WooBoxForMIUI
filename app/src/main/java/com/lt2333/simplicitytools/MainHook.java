package com.lt2333.simplicitytools;

import android.telephony.SubscriptionInfo;
import android.util.Log;

import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {

    XSharedPreferences prefs = new XSharedPreferences(BuildConfig.APPLICATION_ID, "config");
    String TAG = "MainHook";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {


        //电量与性能
        if (lpparam.packageName.equals("com.miui.powerkeeper")) {
            //锁定最高刷新率
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
        }
        //系统界面
        if (lpparam.packageName.equals("com.android.systemui")) {
            //隐藏无SIM卡图标
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
                            Log.d(TAG, "beforeHookedMethod: "+size);
                            if (size == 2 && prefs.getBoolean("hide_sim_two_icon", false) == true) {
                                list.remove(1);
                            }

                            if (size >= 1 && prefs.getBoolean("hide_sim_one_icon", false) == true) {
                                list.remove(0);
                            }
                        }
                    });


        }
        //MIUI桌面
        if (lpparam.packageName.equals("com.miui.home")) {
            //始终显示时钟
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
        }
        //Andorid 框架
        if (lpparam.packageName.equals("android")) {
            //允许截图
            XposedHelpers.findAndHookMethod("com.android.server.wm.WindowState", lpparam.classLoader,
                    "isSecureLocked",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            if (prefs.hasFileChanged()) {
                                prefs.reload();
                            }
                            if (prefs.getBoolean("disable_flag_secure", false)) {
                                param.setResult(false);
                            }
                        }
                    });

            //去除上层显示
            XposedHelpers.findAndHookMethod(
                    "com.android.server.wm.AlertWindowNotification", lpparam.classLoader,
                    "onPostNotification",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            if (prefs.hasFileChanged()) {
                                prefs.reload();
                            }
                            if (prefs.getBoolean("delete_on_post_notification", false)) {
                                param.setResult(false);
                            }
                        }
                    });
        }
    }

}
