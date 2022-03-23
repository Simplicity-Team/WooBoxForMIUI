@file:Suppress("DEPRECATION")

package com.lt2333.simplicitytools.activity

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Switch
import android.widget.Toast
import cn.fkj233.ui.activity.MIUIActivity
import cn.fkj233.ui.activity.OwnSP
import cn.fkj233.ui.activity.data.MIUIPopupData
import cn.fkj233.ui.activity.view.*
import cn.fkj233.ui.dialog.MIUIDialog
import com.lt2333.simplicitytools.BuildConfig
import com.lt2333.simplicitytools.R
import com.lt2333.simplicitytools.util.SPUtils
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

    //检测LSPosed是否激活
    @SuppressLint("WorldReadableFiles")
    private fun checkLSPosed() {
        try {
            setSP(getSharedPreferences("config", MODE_WORLD_READABLE))
        } catch (exception: SecurityException) {
            isLoad = false
            MIUIDialog(this).apply {
                setTitle(R.string.Tips)
                setMessage(R.string.not_support)
                setCancelable(false)
                setRButton(R.string.Done) {
                    exitProcess(0)
                }
                show()
            }
        }
    }

    //主页标题
    override fun mainName(): String {
        return getString(R.string.app_name)
    }

    //菜单标题
    override fun menuName(): String {
        return getString(R.string.menu)
    }

    //主页面
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
                                    R.string.matters_needing_attention_context
                                )
                                setRButton(R.string.Done) {
                                    dismiss()
                                }
                                show()
                            }
                        })
                )
            )


            add(LineV())
            add(TitleTextV(resId = R.string.scope))
            add(
                TextSummaryArrowV(
                    TextSummaryV(
                        textId = R.string.scope_systemui, tipsId = R.string.scope_systemui_summary,
                        onClickListener = { showFragment(getString(R.string.scope_systemui)) }
                    )
                )
            )
            add(
                TextSummaryArrowV(
                    TextSummaryV(
                        textId = R.string.scope_android, tipsId = R.string.scope_android_summary,
                        onClickListener = { showFragment(getString(R.string.scope_android)) }
                    )
                )
            )
            add(
                TextSummaryArrowV(
                    TextSummaryV(
                        textId = R.string.scope_other, tipsId = R.string.scope_other_summary,
                        onClickListener = { showFragment(getString(R.string.scope_other)) }
                    )
                )
            )
            add(LineV())
            add(TitleTextV(resId = R.string.about))
            add(
                TextSummaryArrowV(
                    TextSummaryV(
                        textId = R.string.about_module,
                        tips = getString(R.string.about_module_summary),
                        onClickListener = { showFragment(getString(R.string.about_module)) }
                    )
                )
            )
        }
    }

    //菜单页面
    override fun menuItems(): ArrayList<BaseView> {
        return ArrayList<BaseView>().apply {
            add(
                TextSummaryArrowV(
                    TextSummaryV(textId = R.string.reboot, onClickListener = {
                        MIUIDialog(activity).apply {
                            setTitle(R.string.Tips)
                            setMessage(R.string.are_you_sure_reboot)
                            setLButton(R.string.cancel) {
                                dismiss()
                            }
                            setRButton(R.string.Done) {
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
                            setMessage(R.string.are_you_sure_reboot_scope)
                            setLButton(R.string.cancel) {
                                dismiss()
                            }
                            setRButton(R.string.Done) {
                                val command = arrayOf(
                                    "killall com.android.systemui",
                                    "killall com.miui.home",
                                    "killall com.miui.securitycenter ",
                                    "killall com.android.settings",
                                    "killall com.miui.powerkeeper",
                                    "killall com.android.updater",
                                    "killall com.miui.mediaeditor",
                                    "killall com.miui.screenshot"
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

    //关于页面
    private fun aboutItems(): ArrayList<BaseView> {
        return ArrayList<BaseView>().apply {
            add(
                AuthorV(
                    authorHead = getDrawable(R.drawable.app_icon)!!,
                    authorName = getString(R.string.app_name),
                    authorTips = BuildConfig.VERSION_NAME + "(" + BuildConfig.BUILD_TYPE + ")",
                    onClick = {
                        try {
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("coolmarket://apk/com.lt2333.simplicitytools")
                                )
                            )
                            Toast.makeText(activity, "恳求一个五星好评，Thanks♪(･ω･)ﾉ", Toast.LENGTH_LONG)
                                .show()
                        } catch (e: Exception) {
                            val uri =
                                Uri.parse("https://github.com/Xposed-Modules-Repo/com.lt2333.simplicitytools/releases")
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            startActivity(intent)
                        }
                    })
            )
            add(LineV())
            add(
                TitleTextV(text = getString(R.string.developer))
            )
            add(
                AuthorV(
                    authorHead = getDrawable(R.drawable.lt)!!,
                    authorName = "乌堆小透明",
                    authorTips = "LittleTurtle2333",
                    onClick = {
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
            add(LineV())
            add(
                TitleTextV(text = getString(R.string.thank_list))
            )
            add(
                TextSummaryArrowV(
                    TextSummaryV(
                        textId = R.string.contributor_list,
                        onClickListener = {
                            try {
                                val uri =
                                    Uri.parse("https://github.com/LittleTurtle2333/SimplicityTools/graphs/contributors")
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
                        textId = R.string.third_party_open_source_statement,
                        onClickListener = {
                            try {
                                val uri =
                                    Uri.parse("https://github.com/LittleTurtle2333/SimplicityTools#%E7%AC%AC%E4%B8%89%E6%96%B9%E5%BC%80%E6%BA%90%E5%BC%95%E7%94%A8")
                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(activity, "访问失败", Toast.LENGTH_SHORT).show()
                            }
                        })
                )
            )
            add(LineV())
            add(
                TitleTextV(text = getString(R.string.discussions))
            )
            add(TextSummaryArrowV(TextSummaryV(textId = R.string.qq_channel, onClickListener = {
                try {
                    val uri =
                        Uri.parse("https://qun.qq.com/qqweb/qunpro/share?_wv=3&_wwv=128&inviteCode=29Mu64&from=246610&biz=ka")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(activity, "访问失败", Toast.LENGTH_SHORT).show()
                }
            })))
            add(
                TextSummaryArrowV(
                    TextSummaryV(
                        textId = R.string.tg_channel,
                        tipsId = R.string.tg_channel_summary,
                        onClickListener = {
                            try {
                                startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("tg://resolve?domain=simplicityrom")
                                    )
                                )
                            } catch (e: Exception) {
                                Toast.makeText(activity, "本机未安装Telegram应用", Toast.LENGTH_SHORT)
                                    .show()
                                val uri =
                                    Uri.parse("https://t.me/simplicityrom")
                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                startActivity(intent)
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
                                    Uri.parse("https://github.com/LittleTurtle2333/SimplicityTools/issues")
                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(activity, "访问失败", Toast.LENGTH_SHORT).show()
                            }
                        })
                )
            )
            add(LineV())
            add(
                TitleTextV(text = getString(R.string.other))
            )
            add(
                TextSummaryArrowV(
                    TextSummaryV(
                        textId = R.string.app_coolapk_url,
                        tipsId = R.string.app_coolapk_url_summary,
                        onClickListener = {
                            try {
                                startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("coolmarket://apk/com.lt2333.simplicitytools")
                                    )
                                )
                                Toast.makeText(
                                    activity,
                                    "恳求一个五星好评，Thanks♪(･ω･)ﾉ",
                                    Toast.LENGTH_LONG
                                ).show()
                            } catch (e: Exception) {
                                Toast.makeText(activity, "本机未安装酷安应用", Toast.LENGTH_SHORT).show()
                            }
                        })
                )
            )
            add(
                TextSummaryArrowV(
                    TextSummaryV(
                        textId = R.string.opensource,
                        tipsId = R.string.github_url,
                        onClickListener = {
                            try {
                                val uri =
                                    Uri.parse("https://github.com/LittleTurtle2333/SimplicityTools")
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
                        textId = R.string.participate_in_translation,
                        tipsId = R.string.participate_in_translation_summary,
                        onClickListener = {
                            try {
                                val uri =
                                    Uri.parse("https://crowdin.com/project/simplicitytools")
                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(activity, "访问失败", Toast.LENGTH_SHORT).show()
                            }
                        })
                )
            )
        }
    }

    //系统框架页面
    private fun androidItems(): ArrayList<BaseView> {
        menuButton.visibility = View.VISIBLE
        return ArrayList<BaseView>().apply {
            add(
                TextSummaryWithSwitchV(
                    TextSummaryV(
                        textId = R.string.disable_flag_secure,
                        tipsId = R.string.disable_flag_secure_summary
                    ),
                    SwitchV("disable_flag_secure")
                )
            )
            add(
                TextSummaryWithSwitchV(
                    TextSummaryV(
                        textId = R.string.corepacth,
                        tipsId = R.string.corepacth_summary
                    ),
                    SwitchV("corepatch")
                )
            )
            add(
                TextSummaryWithSwitchV(
                    TextSummaryV(
                        textId = R.string.delete_on_post_notification,
                        tipsId = R.string.delete_on_post_notification_summary
                    ),
                    SwitchV("delete_on_post_notification")
                )
            )
            add(
                TextSummaryWithSwitchV(
                    TextSummaryV(
                        textId = R.string.remove_small_window_restrictions,
                        tipsId = R.string.remove_small_window_restrictions_summary
                    ),
                    SwitchV("remove_small_window_restrictions")
                )
            )
            add(
                TextSummaryArrowV(
                    TextSummaryV(
                        textId = R.string.max_wallpaper_scale,
                        onClickListener = {
                            MIUIDialog(activity).apply {
                                setTitle(R.string.max_wallpaper_scale)
                                setEditText(
                                    "",
                                    "${activity.getString(R.string.def)}1.1, ${activity.getString(R.string.current)}${
                                        OwnSP.ownSP.getFloat(
                                            "max_wallpaper_scale",
                                            1.1f
                                        )
                                    }"
                                )
                                setLButton(textId = R.string.cancel) {
                                    dismiss()
                                }
                                setRButton(textId = R.string.Done) {
                                    if (getEditText() != "") {
                                        OwnSP.ownSP.edit().run {
                                            putFloat("max_wallpaper_scale", getEditText().toFloat())
                                            apply()
                                        }
                                    }
                                    dismiss()
                                }
                                show()
                            }
                        })
                )
            )
        }
    }

    //系统界面页面
    private fun systemuiItems(): ArrayList<BaseView> {
        menuButton.visibility = View.VISIBLE
        return ArrayList<BaseView>().apply {
            add(TitleTextV(resId = R.string.statusbar))
            add(
                TextSummaryWithSwitchV(
                    TextSummaryV(
                        textId = R.string.big_mobile_type_icon
                    ),
                    SwitchV("big_mobile_type_icon")
                )
            )
            add(
                TextSummaryWithSwitchV(
                    TextSummaryV(
                        textId = R.string.hide_battery_percentage_icon,
                        tipsId = R.string.hide_battery_percentage_icon_summary
                    ),
                    SwitchV("hide_battery_percentage_icon")
                )
            )
            add(
                TextSummaryWithSwitchV(
                    TextSummaryV(
                        textId = R.string.remove_the_maximum_number_of_notification_icons,
                        tipsId = R.string.remove_the_maximum_number_of_notification_icons_summary
                    ),
                    SwitchV("remove_the_maximum_number_of_notification_icons")
                )
            )
            add(
                TextSummaryWithSwitchV(
                    TextSummaryV(
                        textId = R.string.double_tap_to_sleep
                    ), SwitchV("status_bar_double_tap_to_sleep")
                )
            )
            add(LineV())
            add(TitleTextV(resId = R.string.status_bar_layout))
            add(
                TextSummaryWithSwitchV(
                    TextSummaryV(
                        textId = R.string.status_bar_time_center,
                        tipsId = R.string.status_bar_layout_summary
                    ),
                    SwitchV("status_bar_time_center")
                )
            )
            val layout_compatibility_mode_binding = getDataBinding(
                SPUtils.getBoolean(
                    activity,
                    "layout_compatibility_mode",
                    false
                )
            ) { view, flags, data ->
                when (flags) {
                    1 -> (view as Switch).isEnabled = data as Boolean
                    2 -> view.visibility = if (data as Boolean) View.VISIBLE else View.GONE
                }
            }
            add(
                TextSummaryWithSwitchV(
                    TextSummaryV(
                        textId = R.string.layout_compatibility_mode,
                        tipsId = R.string.layout_compatibility_mode_summary
                    ),
                    SwitchV(
                        "layout_compatibility_mode",
                        dataBindingSend = layout_compatibility_mode_binding.bindingSend
                    )
                )
            )


            add(
                TextV(
                    resId = R.string.left_margin,
                    dataBindingRecv = layout_compatibility_mode_binding.binding.getRecv(2)
                )
            )
            add(
                SeekBarWithTextV(
                    "status_bar_left_margin",
                    0,
                    300,
                    0,
                    dataBindingRecv = layout_compatibility_mode_binding.binding.getRecv(2)
                )
            )

            add(
                TextV(
                    resId = R.string.right_margin,
                    dataBindingRecv = layout_compatibility_mode_binding.binding.getRecv(2)
                )
            )
            add(
                SeekBarWithTextV(
                    "status_bar_right_margin",
                    0,
                    300,
                    0,
                    dataBindingRecv = layout_compatibility_mode_binding.binding.getRecv(2)
                )
            )


            add(LineV())
            add(TitleTextV(resId = R.string.status_bar_clock_format))

            val custom_clock_binding = getDataBinding(
                SPUtils.getBoolean(
                    activity,
                    "custom_clock_switch",
                    false
                )
            ) { view, flags, data ->
                when (flags) {
                    1 -> (view as Switch).isEnabled = data as Boolean
                    2 -> view.visibility = if (data as Boolean) View.VISIBLE else View.GONE
                }
            }


            add(
                TextWithSwitchV(
                    TextV(resId = R.string.custom_clock_switch, colorId = R.color.purple_700),
                    SwitchV(
                        "custom_clock_switch",
                        dataBindingSend = custom_clock_binding.bindingSend
                    )
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.status_bar_time_year),
                    SwitchV("status_bar_time_year"),
                    dataBindingRecv = custom_clock_binding.binding.getRecv(2)
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.status_bar_time_month),
                    SwitchV("status_bar_time_month"),
                    dataBindingRecv = custom_clock_binding.binding.getRecv(2)
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.status_bar_time_day),
                    SwitchV("status_bar_time_day"),
                    dataBindingRecv = custom_clock_binding.binding.getRecv(2)
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.status_bar_time_week),
                    SwitchV("status_bar_time_week"),
                    dataBindingRecv = custom_clock_binding.binding.getRecv(2)
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.status_bar_time_double_hour),
                    SwitchV("status_bar_time_double_hour"),
                    dataBindingRecv = custom_clock_binding.binding.getRecv(2)
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.status_bar_time_period),
                    SwitchV("status_bar_time_period", true),
                    dataBindingRecv = custom_clock_binding.binding.getRecv(2)
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.status_bar_time_seconds),
                    SwitchV("status_bar_time_seconds"),
                    dataBindingRecv = custom_clock_binding.binding.getRecv(2)
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.status_bar_time_hide_space),
                    SwitchV("status_bar_time_hide_space"),
                    dataBindingRecv = custom_clock_binding.binding.getRecv(2)
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.status_bar_time_double_line),
                    SwitchV("status_bar_time_double_line"),
                    dataBindingRecv = custom_clock_binding.binding.getRecv(2)
                )
            )
            add(
                TextV(
                    resId = R.string.status_bar_clock_size,
                    dataBindingRecv = custom_clock_binding.binding.getRecv(2)
                )
            )
            add(
                SeekBarWithTextV(
                    "status_bar_clock_size", 0, 18, 0,
                    dataBindingRecv = custom_clock_binding.binding.getRecv(2)
                )
            )
            add(
                TextV(
                    resId = R.string.status_bar_clock_double_line_size,
                    dataBindingRecv = custom_clock_binding.binding.getRecv(2)
                )
            )
            add(
                SeekBarWithTextV(
                    "status_bar_clock_double_line_size", 0, 9, 0,
                    dataBindingRecv = custom_clock_binding.binding.getRecv(2)
                )
            )

            add(LineV())
            add(TitleTextV(resId = R.string.status_bar_icon))
            add(
                TextSummaryArrowV(
                    TextSummaryV(
                        textId = R.string.hide_icon,
                        onClickListener = { showFragment(getString(R.string.hide_icon)) }
                    )
                )
            )
            add(LineV())
            add(TitleTextV(resId = R.string.status_bar_network_speed))
            add(
                TextSummaryWithSwitchV(
                    TextSummaryV(
                        textId = R.string.status_bar_network_speed_refresh_speed,
                        tipsId = R.string.status_bar_network_speed_refresh_speed_summary
                    ),
                    SwitchV("status_bar_network_speed_refresh_speed")
                )
            )
            add(
                TextSummaryWithSwitchV(
                    TextSummaryV(
                        textId = R.string.hide_status_bar_network_speed_second,
                        tipsId = R.string.hide_status_bar_network_speed_second_summary
                    ),
                    SwitchV("hide_status_bar_network_speed_second")
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.hide_network_speed_splitter),
                    SwitchV("hide_network_speed_splitter")
                )
            )

            val status_bar_dual_row_network_speed_binding = getDataBinding(
                SPUtils.getBoolean(
                    activity,
                    "status_bar_dual_row_network_speed",
                    false
                )
            ) { view, flags, data ->
                when (flags) {
                    1 -> (view as Switch).isEnabled = data as Boolean
                    2 -> view.visibility = if (data as Boolean) View.VISIBLE else View.GONE
                }
            }
            add(
                TextSummaryWithSwitchV(
                    TextSummaryV(
                        textId = R.string.status_bar_dual_row_network_speed,
                        tipsId = R.string.status_bar_dual_row_network_speed_summary
                    ),
                    SwitchV(
                        "status_bar_dual_row_network_speed",
                        dataBindingSend = status_bar_dual_row_network_speed_binding.bindingSend
                    )
                )
            )
            val align: HashMap<Int, String> = hashMapOf()
            align[0] = getString(R.string.left)
            align[1] = getString(R.string.right)
            add(
                TextWithSpinnerV(
                    TextV(resId = R.string.status_bar_network_speed_dual_row_gravity),
                    SpinnerV(
                        arrayListOf<MIUIPopupData>().apply {
                            add(MIUIPopupData(getString(R.string.left)) {
                                OwnSP.ownSP.edit().run {
                                    putInt(
                                        "status_bar_network_speed_dual_row_gravity",
                                        0
                                    )
                                    apply()
                                }
                            })
                            add(MIUIPopupData(getString(R.string.right)) {
                                OwnSP.ownSP.edit().run {
                                    putInt(
                                        "status_bar_network_speed_dual_row_gravity",
                                        1
                                    )
                                    apply()
                                }
                            })
                        }, currentValue = align[OwnSP.ownSP.getInt(
                            "status_bar_network_speed_dual_row_gravity",
                            0
                        )].toString()
                    ),
                    dataBindingRecv = status_bar_dual_row_network_speed_binding.binding.getRecv(2)
                )
            )
            add(
                TextWithSpinnerV(
                    TextV(resId = R.string.status_bar_network_speed_dual_row_icon),
                    SpinnerV(
                        arrayListOf<MIUIPopupData>().apply {
                            add(MIUIPopupData(getString(R.string.none)) {
                                OwnSP.ownSP.edit().run {
                                    putString(
                                        "status_bar_network_speed_dual_row_icon",
                                        getString(R.string.none)
                                    )
                                    apply()
                                }
                            })
                            add(MIUIPopupData("▲▼") {
                                OwnSP.ownSP.edit().run {
                                    putString("status_bar_network_speed_dual_row_icon", "▲▼")
                                    apply()
                                }
                            })
                            add(MIUIPopupData("△▽") {
                                OwnSP.ownSP.edit().run {
                                    putString("status_bar_network_speed_dual_row_icon", "△▽")
                                    apply()
                                }
                            })
                            add(MIUIPopupData("↑↓") {
                                OwnSP.ownSP.edit().run {
                                    putString("status_bar_network_speed_dual_row_icon", "↑↓")
                                    apply()
                                }
                            })
                        }, "${
                            OwnSP.ownSP.getString(
                                "status_bar_network_speed_dual_row_icon",
                                getString(R.string.none)
                            )
                        }"
                    ),
                    dataBindingRecv = status_bar_dual_row_network_speed_binding.binding.getRecv(2)
                )
            )
            add(
                TextV(
                    resId = R.string.status_bar_network_speed_dual_row_size,
                    dataBindingRecv = status_bar_dual_row_network_speed_binding.binding.getRecv(2)
                )
            )
            add(
                SeekBarWithTextV(
                    "status_bar_network_speed_dual_row_size",
                    0,
                    9,
                    0,
                    dataBindingRecv = status_bar_dual_row_network_speed_binding.binding.getRecv(2)
                )
            )


            add(LineV())
            add(TitleTextV(resId = R.string.notification_center))
            val show_weather_main_switch_binding = getDataBinding(
                SPUtils.getBoolean(
                    activity,
                    "notification_weather",
                    false
                )
            ) { view, flags, data ->
                when (flags) {
                    1 -> (view as Switch).isEnabled = data as Boolean
                    2 -> view.visibility = if (data as Boolean) View.VISIBLE else View.GONE
                }
            }
            add(
                TextSummaryWithSwitchV(
                    TextSummaryV(
                        textId = R.string.show_weather_main_switch, colorId = R.color.purple_700
                    ),
                    SwitchV(
                        "notification_weather",
                        dataBindingSend = show_weather_main_switch_binding.bindingSend
                    )
                )
            )
            add(
                TextSummaryWithSwitchV(
                    TextSummaryV(
                        textId = R.string.show_city,
                    ),
                    SwitchV("notification_weather_city"),
                    dataBindingRecv = show_weather_main_switch_binding.binding.getRecv(2)
                )
            )
            add(
                TextSummaryWithSwitchV(
                    TextSummaryV(
                        textId = R.string.can_notification_slide,
                    ),
                    SwitchV("can_notification_slide")
                )
            )

            add(LineV())
            add(TitleTextV(resId = R.string.control_center))
            val control_center_weather_binding = getDataBinding(
                SPUtils.getBoolean(
                    activity,
                    "control_center_weather",
                    false
                )
            ) { view, flags, data ->
                when (flags) {
                    1 -> (view as Switch).isEnabled = data as Boolean
                    2 -> view.visibility = if (data as Boolean) View.VISIBLE else View.GONE
                }
            }
            add(
                TextSummaryWithSwitchV(
                    TextSummaryV(
                        textId = R.string.show_weather_main_switch,
                        colorId = R.color.purple_700,
                        tipsId = R.string.control_center_weather_summary
                    ),
                    SwitchV(
                        "control_center_weather",
                        dataBindingSend = control_center_weather_binding.bindingSend
                    )
                )
            )
            add(
                TextSummaryWithSwitchV(
                    TextSummaryV(
                        textId = R.string.show_city,
                    ),
                    SwitchV("control_center_weather_city"),
                    dataBindingRecv = control_center_weather_binding.binding.getRecv(2)
                )
            )

            add(LineV())
            add(TitleTextV(resId = R.string.lock_screen))
            add(
                TextSummaryWithSwitchV(
                    TextSummaryV(
                        textId = R.string.remove_the_left_side_of_the_lock_screen,
                        tipsId = R.string.only_official_default_themes_are_supported
                    ),
                    SwitchV("remove_the_left_side_of_the_lock_screen")
                )
            )
            add(
                TextSummaryWithSwitchV(
                    TextSummaryV(
                        textId = R.string.remove_lock_screen_camera,
                        tipsId = R.string.only_official_default_themes_are_supported
                    ),
                    SwitchV("remove_lock_screen_camera")
                )
            )
            add(
                TextSummaryWithSwitchV(
                    TextSummaryV(
                        textId = R.string.enable_wave_charge_animation
                    ),
                    SwitchV("enable_wave_charge_animation")
                )
            )
            add(
                TextSummaryWithSwitchV(
                    TextSummaryV(
                        textId = R.string.lock_screen_charging_current,
                        tipsId = R.string.only_official_default_themes_are_supported
                    ),
                    SwitchV("lock_screen_charging_current")
                )
            )
            add(
                TextSummaryWithSwitchV(
                    TextSummaryV(
                        textId = R.string.double_tap_to_sleep,
                        tipsId = R.string.home_double_tap_to_sleep_summary
                    ), SwitchV("lock_screen_double_tap_to_sleep")
                )
            )

            add(LineV())
            add(TitleTextV(resId = R.string.old_quick_settings_panel))
            val old_qs_custom_switch_binding = getDataBinding(
                SPUtils.getBoolean(
                    activity,
                    "old_qs_custom_switch",
                    false
                )
            ) { view, flags, data ->
                when (flags) {
                    1 -> (view as Switch).isEnabled = data as Boolean
                    2 -> view.visibility = if (data as Boolean) View.VISIBLE else View.GONE
                }
            }
            add(
                TextSummaryWithSwitchV(
                    TextSummaryV(
                        textId = R.string.old_qs_custom_switch,
                        colorId = R.color.purple_700
                    ),
                    SwitchV(
                        "old_qs_custom_switch",
                        dataBindingSend = old_qs_custom_switch_binding.bindingSend
                    )
                )
            )

            add(
                TextV(
                    resId = R.string.qs_custom_rows,
                    dataBindingRecv = old_qs_custom_switch_binding.binding.getRecv(2)
                )
            )
            add(
                SeekBarWithTextV(
                    "qs_custom_rows",
                    1,
                    6,
                    3,
                    dataBindingRecv = old_qs_custom_switch_binding.binding.getRecv(2)
                )
            )


            add(
                TextV(
                    resId = R.string.qs_custom_rows_horizontal,
                    dataBindingRecv = old_qs_custom_switch_binding.binding.getRecv(2)
                )
            )
            add(
                SeekBarWithTextV(
                    "qs_custom_rows_horizontal",
                    1,
                    3,
                    2,
                    dataBindingRecv = old_qs_custom_switch_binding.binding.getRecv(2)
                )
            )

            add(
                TextV(
                    resId = R.string.qs_custom_columns,
                    dataBindingRecv = old_qs_custom_switch_binding.binding.getRecv(2)
                )
            )
            add(
                SeekBarWithTextV(
                    "qs_custom_columns",
                    1,
                    7,
                    4,
                    dataBindingRecv = old_qs_custom_switch_binding.binding.getRecv(2)
                )
            )
            add(
                TextV(
                    resId = R.string.qs_custom_columns_unexpanded,
                    dataBindingRecv = old_qs_custom_switch_binding.binding.getRecv(2)
                )
            )
            add(
                SeekBarWithTextV(
                    "qs_custom_columns_unexpanded",
                    1,
                    7,
                    5,
                    dataBindingRecv = old_qs_custom_switch_binding.binding.getRecv(2)
                )
            )
        }
    }

    //其他页面
    private fun otherItems(): ArrayList<BaseView> {
        menuButton.visibility = View.VISIBLE
        return ArrayList<BaseView>().apply {
            add(TitleTextV(resId = R.string.scope_miuihome))
            add(
                TextSummaryWithSwitchV(
                    TextSummaryV(
                        textId = R.string.home_time,
                        tipsId = R.string.home_time_summary
                    ), SwitchV("home_time")
                )
            )
            add(
                TextSummaryWithSwitchV(
                    TextSummaryV(
                        textId = R.string.double_tap_to_sleep,
                        tipsId = R.string.home_double_tap_to_sleep_summary
                    ), SwitchV("double_tap_to_sleep")
                )
            )
            add(LineV())
            add(TitleTextV(resId = R.string.scope_powerkeeper))
            add(
                TextSummaryWithSwitchV(
                    TextSummaryV(
                        textId = R.string.lock_max_fps,
                        tipsId = R.string.lock_max_fps_summary
                    ),
                    SwitchV("lock_max_fps")
                )
            )

            add(
                TextSummaryWithSwitchV(
                    TextSummaryV(
                        textId = R.string.prevent_recovery_of_battery_optimization_white_list,
                        tipsId = R.string.failed_after_restart
                    ),
                    SwitchV("prevent_recovery_of_battery_optimization_white_list")
                )
            )
            add(
                TextSummaryArrowV(
                    TextSummaryV(
                        textId = R.string.battery_optimization,
                        tipsId = R.string.battery_optimization_summary,
                        onClickListener = {
                            try {
                                val intent = Intent()
                                val comp = ComponentName(
                                    "com.android.settings",
                                    "com.android.settings.Settings\$HighPowerApplicationsActivity"
                                )
                                intent.component = comp
                                startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(activity, "启动失败，可能是不支持", Toast.LENGTH_LONG)
                                    .show()
                            }
                        })
                )
            )
            add(LineV())
            add(TitleTextV(resId = R.string.scope_securitycenter))
            add(
                TextSummaryWithSwitchV(
                    TextSummaryV(
                        textId = R.string.skip_waiting_time,
                        tipsId = R.string.skip_waiting_time_summary
                    ),
                    SwitchV("skip_waiting_time")
                )
            )
            add(
                TextSummaryWithSwitchV(
                    TextSummaryV(
                        textId = R.string.remove_open_app_confirmation_popup,
                        tipsId = R.string.remove_open_app_confirmation_popup_summary
                    ),
                    SwitchV("remove_open_app_confirmation_popup")
                )
            )
            add(
                TextSummaryWithSwitchV(
                    TextSummaryV(
                        textId = R.string.lock_one_hundred,
                        tipsId = R.string.lock_one_hundred_summary
                    ),
                    SwitchV("lock_one_hundred")
                )
            )
            add(
                TextSummaryWithSwitchV(
                    TextSummaryV(
                        textId = R.string.remove_macro_blacklist
                    ),
                    SwitchV("remove_macro_blacklist")
                )
            )
            add(
                TextSummaryWithSwitchV(
                    TextSummaryV(
                        textId = R.string.battery_life_function
                    ),
                    SwitchV("battery_life_function")
                )
            )
            add(LineV())
            add(TitleTextV(resId = R.string.scope_mediaeditor))
            add(
                TextSummaryWithSwitchV(
                    TextSummaryV(
                        textId = R.string.unlock_unlimited_cropping,
                        tipsId = R.string.unlock_unlimited_cropping_summary
                    ),
                    SwitchV("unlock_unlimited_cropping")
                )
            )

            add(LineV())
            add(TitleTextV(resId = R.string.updater))
            add(
                TextSummaryWithSwitchV(
                    TextSummaryV(
                        textId = R.string.remove_ota_validate,
                        tipsId = R.string.remove_ota_validate_summary
                    ),
                    SwitchV("remove_ota_validate")
                )
            )

            add(LineV())
            add(TitleTextV(resId = R.string.settings))
            add(
                TextSummaryWithSwitchV(
                    TextSummaryV(
                        textId = R.string.show_notification_importance,
                        tipsId = R.string.show_notification_importance_summary
                    ),
                    SwitchV("show_notification_importance")
                )
            )
            add(LineV())
            add(TitleTextV(resId = R.string.remove_ad))
            add(
                TextSummaryWithSwitchV(
                    TextSummaryV(
                        textId = R.string.remove_thememanager_ads
                    ),
                    SwitchV("remove_thememanager_ads")
                )
            )
        }
    }

    //隐藏图标页面
    private fun hideIconItems(): ArrayList<BaseView> {
        menuButton.visibility = View.VISIBLE
        return ArrayList<BaseView>().apply {
            add(TitleTextV(resId = R.string.status_bar_icon))
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.hide_battery_icon),
                    SwitchV("hide_battery_icon")
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.hide_battery_charging_icon),
                    SwitchV("hide_battery_charging_icon")
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.hide_gps_icon),
                    SwitchV("hide_gps_icon")
                )
            )
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
                    TextV(resId = R.string.hide_no_sim_icon),
                    SwitchV("hide_no_sim_icon")
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
                    TextV(resId = R.string.hide_mobile_activity_icon),
                    SwitchV("hide_mobile_activity_icon")
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.hide_mobile_type_icon),
                    SwitchV("hide_mobile_type_icon")
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.hide_wifi_icon),
                    SwitchV("hide_wifi_icon")
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.hide_wifi_activity_icon),
                    SwitchV("hide_wifi_activity_icon")
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.hide_wifi_standard_icon),
                    SwitchV("hide_wifi_standard_icon")
                )
            )
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
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.hide_vpn_icon),
                    SwitchV("hide_vpn_icon")
                )
            )
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
                    TextV(resId = R.string.hide_volume_icon),
                    SwitchV("hide_volume_icon")
                )
            )
            add(
                TextWithSwitchV(
                    TextV(resId = R.string.hide_zen_icon),
                    SwitchV("hide_zen_icon")
                )
            )
        }
    }

    override fun getItems(item: String): ArrayList<BaseView> {
        return when (item) {
            getString(R.string.scope_android) -> androidItems()
            getString(R.string.scope_systemui) -> systemuiItems()
            getString(R.string.scope_other) -> otherItems()
            getString(R.string.about_module) -> aboutItems()
            getString(R.string.hide_icon) -> hideIconItems()
            menuName() -> menuItems()
            else -> mainItems()
        }
    }
}