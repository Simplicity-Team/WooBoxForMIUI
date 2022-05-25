package com.lt2333.simplicitytools.hook.app.systemui

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.getObjectAs
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.github.kyuubiran.ezxhelper.utils.putObject
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object ShowWifiStandard : HookRegister() {

    override fun init() {
        findMethod("com.android.systemui.statusbar.StatusBarWifiView") {
            name == "initViewState" && parameterCount == 1
        }.hookBefore {
            hasEnable("show_wifi_standard") {
                findMethod("com.android.systemui.statusbar.phone.StatusBarSignalPolicy\$WifiIconState") {
                    name == "copyTo" && parameterCount == 1
                }.hookBefore {
                    val wifiStandard = it.thisObject.getObjectAs<Int>("wifiStandard")
                    it.thisObject.putObject("showWifiStandard", wifiStandard != 0)
                }
            }
        }

        findMethod("com.android.systemui.statusbar.StatusBarWifiView") {
            name == "updateState" && parameterCount == 1
        }.hookBefore {
            hasEnable("show_wifi_standard") {
                findMethod("com.android.systemui.statusbar.phone.StatusBarSignalPolicy\$WifiIconState") {
                    name == "copyTo" && parameterCount == 1
                }.hookBefore {
                    val wifiStandard = it.thisObject.getObjectAs<Int>("wifiStandard")
                    it.thisObject.putObject("showWifiStandard", wifiStandard != 0)
                }
            }
        }
    }

}
