package com.lt2333.simplicitytools.hooks.rules.t.systemui

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook

object HideStatusBarIconForT : HookRegister() {

    override fun init() {
        findMethod("com.android.systemui.statusbar.phone.StatusBarIconControllerImpl") {
            name == "setIconVisibility" && parameterCount == 2
        }.hookBefore {
            hideIcon(it)
        }

        findMethod("com.android.systemui.statusbar.phone.MiuiDripLeftStatusBarIconControllerImpl") {
            name == "setIconVisibility" && parameterCount == 2
        }.hookBefore {
            hideIcon(it)
        }
    }

    private fun hideIcon(it: XC_MethodHook.MethodHookParam) {
        //Log.d("图标名字", "handleLoadPackage: " + it.args[0])
        when (it.args[0] as String) {
            //隐藏蓝牙
            "bluetooth" -> hasEnable("hide_bluetooth_icon") {
                it.args[1] = false
            }
            //隐藏蓝牙电量
            "bluetooth_handsfree_battery" -> hasEnable("hide_bluetooth_battery_icon") {
                it.args[1] = false
            }
            //隐藏勿扰
            "zen" -> hasEnable("hide_zen_icon") {
                it.args[1] = false
            }
            //隐藏声音
            "volume" -> hasEnable("hide_volume_icon") {
                it.args[1] = false
            }
            //隐藏WIFI
            "wifi" -> hasEnable("hide_wifi_icon") {
                it.args[1] = false
            }
            //隐藏WIFI辅助
            "slave_wifi" -> hasEnable("hide_slave_wifi_icon") {
                it.args[1] = false
            }
            //隐藏飞行模式
            "airplane" -> hasEnable("hide_airplane_icon") {
                it.args[1] = false
            }
            //隐藏闹钟
            "alarm_clock" -> hasEnable("hide_alarm_icon") {
                it.args[1] = false
            }
            //隐藏定位
            "location" -> hasEnable("hide_gps_icon") {
                it.args[1] = false
            }
            //隐藏热点
            "hotspot" -> hasEnable("hide_hotspot_icon") {
                it.args[1] = false
            }
            //隐藏耳机
            "headset" -> hasEnable("hide_headset_icon") {
                it.args[1] = false
            }
            //隐藏VPN
            "vpn" -> hasEnable("hide_vpn_icon") {
                it.args[1] = false
            }
            //隐藏无SIM卡
            "no_sim" -> hasEnable("hide_no_sim_icon") {
                it.args[1] = false
            }
            //隐藏NFC图标
            "nfc" -> hasEnable("hide_nfc_icon") {
                it.args[1] = false
            }
        }
    }

}