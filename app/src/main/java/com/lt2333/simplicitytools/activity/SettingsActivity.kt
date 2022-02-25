@file:Suppress("DEPRECATION")

package com.lt2333.simplicitytools.activity

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import cn.fkj233.ui.activity.MIUIActivity
import cn.fkj233.ui.activity.view.*
import cn.fkj233.ui.dialog.MIUIDialog
import com.lt2333.simplicitytools.BuildConfig
import com.lt2333.simplicitytools.R
import com.lt2333.simplicitytools.util.ShellUtils
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import kotlin.system.exitProcess

class SettingsActivity : MIUIActivity() {
    private val activity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        if (BuildConfig.BUILD_TYPE != "debug") {
            AppCenter.start(
                application, "ae2037d3-9914-4e0c-b02b-f9b2bb2574e5",
                Analytics::class.java, Crashes::class.java
            )
        }
        checkLSPosed()
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("WorldReadableFiles")
    private fun checkLSPosed() {
        try {
            setSP(getSharedPreferences("config", MODE_WORLD_READABLE))
        } catch (exception: SecurityException) {
            isLoad = false
            MIUIDialog(this).apply {
                setTitle(R.string.Tips)
                setMessage("您似乎正在使用过时的 LSPosed 版本或 LSPosed 未激活，请更新 LSPosed 或者激活后再试。")
                setCancelable(false)
                setRButton("确定") {
                    exitProcess(0)
                }
                show()
            }
        }
    }

    override fun mainName(): String {
        return getString(R.string.app_name)
    }

    override fun mainItems(): ArrayList<BaseView> {
        return arrayListOf<BaseView>().apply {
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.main_switch, colorId = R.color.purple_700),
                    SwitchV("main_switch", true)
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.HideLauncherIcon),
                    SwitchV("hLauncherIcon", customOnCheckedChangeListener = {
                        packageManager.setComponentEnabledSetting(
                            ComponentName(activity, "${BuildConfig.APPLICATION_ID}.launcher"),
                            if (it) {
                                PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                            } else {
                                PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                            },
                            PackageManager.DONT_KILL_APP
                        )
                    })
                )
            )
            add(
                TextSummaryArrowV(
                    TextSummaryV(
                        textId = R.string.matters_needing_attention,
                        colorId = R.color.red,
                        onClickListener = {
                            MIUIDialog(activity).apply {
                                setTitle(R.string.matters_needing_attention)
                                setMessage(
                                    """首次激活或更新后建议重启手机
                            |绝大部分功能更改后需要在右上角重启作用域后生效
                        """.trimMargin()
                                )
                                setRButton(R.string.Done) {
                                    dismiss()
                                }
                                show()
                            }
                        })
                )
            )
            add(TitleTextV(resId = R.string.ui))
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.delete_on_post_notification),
                    SwitchV("delete_on_post_notification")
                )
            )
            add(LineV())
            add(TitleTextV(resId = R.string.statusbar))
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.status_bar_network_speed_refresh_speed),
                    SwitchV("status_bar_network_speed_refresh_speed")
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.hide_battery_percentage_icon),
                    SwitchV("hide_battery_percentage_icon")
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.hide_status_bar_network_speed_second),
                    SwitchV("hide_status_bar_network_speed_second")
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.remove_the_maximum_number_of_notification_icons),
                    SwitchV("remove_the_maximum_number_of_notification_icons")
                )
            )
            add(LineV())
            add(TitleTextV(resId = R.string.status_bar_clock_format))
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.custom_clock_switch, colorId = R.color.purple_700),
                    SwitchV("custom_clock_switch")
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.status_bar_time_year),
                    SwitchV("status_bar_time_year")
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.status_bar_time_month),
                    SwitchV("status_bar_time_month")
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.status_bar_time_day),
                    SwitchV("status_bar_time_day")
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.status_bar_time_week),
                    SwitchV("status_bar_time_week")
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.status_bar_time_double_hour),
                    SwitchV("status_bar_time_double_hour")
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.status_bar_time_period),
                    SwitchV("status_bar_time_period", true)
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.status_bar_time_seconds),
                    SwitchV("status_bar_time_seconds")
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.status_bar_time_hide_space),
                    SwitchV("status_bar_time_hide_space")
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.status_bar_time_double_line),
                    SwitchV("status_bar_time_double_line")
                )
            )
            add(TextV(resId = R.string.status_bar_clock_size))
            add(SeekBarWithTextV("status_bar_clock_size", 0, 18, 0))
            add(TextV(resId = R.string.status_bar_clock_double_line_size))
            add(SeekBarWithTextV("status_bar_clock_double_line_size", 0, 8, 0))
            add(LineV())
            add(TitleTextV(resId = R.string.status_bar_icon))
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.hide_battery_icon),
                    SwitchV("hide_battery_icon")
                )
            )
            add(TextWithSwitchV(TextV(resId = R.string.hide_gps_icon), SwitchV("hide_gps_icon")))
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.hide_bluetooth_icon),
                    SwitchV("hide_bluetooth_icon")
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.hide_bluetooth_battery_icon),
                    SwitchV("hide_bluetooth_battery_icon")
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.hide_small_hd_icon),
                    SwitchV("hide_small_hd_icon")
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.hide_big_hd_icon),
                    SwitchV("hide_big_hd_icon")
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.hide_hd_no_service_icon),
                    SwitchV("hide_hd_no_service_icon")
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.hide_sim_one_icon),
                    SwitchV("hide_sim_one_icon")
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.hide_sim_two_icon),
                    SwitchV("hide_sim_two_icon")
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.hide_no_sim_icon),
                    SwitchV("hide_no_sim_icon")
                )
            )
            add(TextWithSwitchV(TextV(resId = R.string.hide_wifi_icon), SwitchV("hide_wifi_icon")))
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.hide_slave_wifi_icon),
                    SwitchV("hide_slave_wifi_icon")
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.hide_hotspot_icon),
                    SwitchV("hide_hotspot_icon")
                )
            )
            add(TextWithSwitchV(TextV(resId = R.string.hide_vpn_icon), SwitchV("hide_vpn_icon")))
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.hide_airplane_icon),
                    SwitchV("hide_airplane_icon")
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.hide_alarm_icon),
                    SwitchV("hide_alarm_icon")
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.hide_headset_icon),
                    SwitchV("hide_headset_icon")
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.hide_volume_zen_icon),
                    SwitchV("hide_volume_zen_icon")
                )
            )
            add(LineV())
            add(TitleTextV(resId = R.string.home))
            add(TextWithSwitchV(TextV(resId = R.string.home_time), SwitchV("home_time")))
            add(LineV())
            add(TitleTextV(resId = R.string.performance))
            add(
                TextSummaryWithSwitchV(
                    TextSummaryV(textId = R.string.lock_max_fps, tips = "支持添加下拉控制中心快速开关，实时切换"),
                    SwitchV("lock_max_fps")
                )
            )
            add(LineV())
            add(TitleTextV(resId = R.string.other))
            add(
                TextSummaryWithSwitchV(
                    TextSummaryV(
                        textId = R.string.disable_flag_secure,
                        tips = "支持添加下拉控制中心快速开关，保障安全"
                    ),
                    SwitchV("disable_flag_secure")
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.skip_waiting_time),
                    SwitchV("skip_waiting_time")
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.unlock_unlimited_cropping),
                    SwitchV("unlock_unlimited_cropping")
                )
            )
            add(LineV())
            add(TitleTextV(resId = R.string.about))
            add(
                TextSummaryArrowV(
                    TextSummaryV(
                        textId = R.string.opensource,
                        tipsId = R.string.github_url,
                        onClickListener = {
                            try {
                                val uri =
                                    Uri.parse("https://github.com/LittleTurtle2333/Simplicity_Tools_Xposed")
                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(activity, "访问失败", Toast.LENGTH_SHORT).show()
                            }
                        })
                )
            )
            add(
                TextSummaryArrowV(
                    TextSummaryV(
                        textId = R.string.issues,
                        tipsId = R.string.issues_url,
                        onClickListener = {
                            try {
                                val uri =
                                    Uri.parse("https://github.com/LittleTurtle2333/Simplicity_Tools_Xposed/issues")
                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(activity, "访问失败", Toast.LENGTH_SHORT).show()
                            }
                        })
                )
            )
            add(
                TextSummaryArrowV(
                    TextSummaryV(
                        textId = R.string.dev_coolapk,
                        tipsId = R.string.dev_coolapk_name,
                        onClickListener = {
                            try {
                                startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("coolmarket://u/883441")
                                    )
                                )
                                Toast.makeText(activity, "乌堆小透明：靓仔，点个关注吧！", Toast.LENGTH_SHORT)
                                    .show()
                            } catch (e: Exception) {
                                Toast.makeText(activity, "本机未安装酷安应用", Toast.LENGTH_SHORT).show()
                                val uri = Uri.parse("http://www.coolapk.com/u/883441")
                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                startActivity(intent)
                            }
                        })
                )
            )
            add(

                TextSummaryArrowV(
                    TextSummaryV(
                        textId = R.string.verison,
                        tips = "${BuildConfig.VERSION_NAME}(${BuildConfig.BUILD_TYPE})"
                    )
                )
            )
        }
    }

    override fun menuName(): String {
        return getString(R.string.menu)
    }

    override fun menuItems(): ArrayList<BaseView> {
        return ArrayList<BaseView>().apply {
            add(
                TextSummaryArrowV(
                    TextSummaryV(textId = R.string.reboot, onClickListener = {
                        MIUIDialog(activity).apply {
                            setTitle(R.string.Tips)
                            setMessage("确定重启系统？")
                            setLButton("取消") {
                                dismiss()
                            }
                            setRButton("确定") {
                                val command = arrayOf("reboot")
                                ShellUtils.execCommand(command, true)
                                dismiss()
                            }
                            show()
                        }
                    })
                )
            )
            add(
                TextSummaryArrowV(
                    TextSummaryV(textId = R.string.reboot_host, onClickListener = {
                        MIUIDialog(activity).apply {
                            setTitle(R.string.Tips)
                            setMessage("确定重启作用域？")
                            setLButton("取消") {
                                dismiss()
                            }
                            setRButton("确定") {
                                val command = arrayOf(
                                    "killall com.miui.home",
                                    "killall com.miui.securitycenter ",
                                    "killall com.miui.powerkeeper",
                                    "killall com.miui.mediaeditor",
                                    "killall com.android.systemui"
                                )
                                ShellUtils.execCommand(command, true)
                                dismiss()
                            }
                            show()
                        }
                    })
                )
            )

        }
    }

    override fun getItems(item: String): ArrayList<BaseView> {
        return when (item) {
            /** 必须写这两个 不然会出错 */
            menuName() -> menuItems()
            else -> mainItems()
        }
    }
}