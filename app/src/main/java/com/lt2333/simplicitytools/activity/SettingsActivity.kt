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
import cn.fkj233.ui.activity.data.DefValue
import cn.fkj233.ui.activity.view.SpinnerV
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import cn.fkj233.ui.activity.view.TextV
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

    //检测LSPosed是否激活
    @SuppressLint("WorldReadableFiles")
    private fun checkLSPosed() {
        try {
            setSP(getSharedPreferences("config", MODE_WORLD_READABLE))
        } catch (exception: SecurityException) {
            isLoad = false
            MIUIDialog(this) {
                setTitle(R.string.Tips)
                setMessage(R.string.not_support)
                setCancelable(false)
                setRButton(R.string.Done) {
                    exitProcess(0)
                }
            }.show()
        }
    }

    init {
        initView {
            registerMain(getString(R.string.app_name), false) {
                TextSummaryWithSwitch(
                    TextSummaryV(textId = R.string.main_switch, colorId = R.color.purple_700),
                    SwitchV("main_switch", true)
                )
                TextSummaryWithSwitch(
                    TextSummaryV(textId = R.string.HideLauncherIcon),
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
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.matters_needing_attention,
                        colorId = R.color.red,
                        onClickListener = {
                            MIUIDialog(activity) {
                                setTitle(R.string.matters_needing_attention)
                                setMessage(
                                    R.string.matters_needing_attention_context
                                )
                                setRButton(R.string.Done) {
                                    dismiss()
                                }
                            }.show()
                        })
                )
                Line()
                TitleText(resId = R.string.scope)
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.scope_systemui,
                        tipsId = R.string.scope_systemui_summary,
                        onClickListener = { showFragment("scope_systemui") }
                    )
                )
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.scope_android,
                        tipsId = R.string.scope_android_summary,
                        onClickListener = { showFragment("scope_android") }
                    )
                )
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.scope_other,
                        tipsId = R.string.scope_other_summary,
                        onClickListener = { showFragment("scope_other") }
                    )
                )
                Line()
                TitleText(resId = R.string.about)
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.about_module,
                        tips = getString(R.string.about_module_summary),
                        onClickListener = { showFragment("about_module") }
                    )
                )

            }
            register("scope_systemui", getString(R.string.scope_systemui), false) {
                TitleText(resId = R.string.statusbar)
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.double_tap_to_sleep
                    ), SwitchV("status_bar_double_tap_to_sleep")
                )
                Line()
                TitleText(resId = R.string.status_bar_layout)
                val statusBarLayoutMode: HashMap<Int, String> = hashMapOf<Int, String>().also {
                    it[0] = getString(R.string.default1)
                    it[1] = getString(R.string.clock_center)
                    it[2] = getString(R.string.clock_right)
                    it[3] = getString(R.string.clock_center_and_icon_left)
                }
                TextWithSpinner(
                    TextV(resId = R.string.status_bar_layout_mode),
                    SpinnerV(
                        statusBarLayoutMode[safeSP.getInt(
                            "status_bar_layout_mode",
                            0
                        )].toString()
                    ) {
                        add(statusBarLayoutMode[0].toString()) {
                            safeSP.putAny("status_bar_layout_mode", 0)
                        }
                        add(statusBarLayoutMode[1].toString()) {
                            safeSP.putAny("status_bar_layout_mode", 1)
                        }
                        add(statusBarLayoutMode[2].toString()) {
                            safeSP.putAny("status_bar_layout_mode", 2)
                        }
                        add(statusBarLayoutMode[3].toString()) {
                            safeSP.putAny("status_bar_layout_mode", 3)
                        }
                    }
                )
                val layoutCompatibilityModeBinding = GetDataBinding(
                    object : DefValue {
                        override fun getValue(): Any {
                            return safeSP.getBoolean("layout_compatibility_mode", false)
                        }
                    }
                ) { view, flags, data ->
                    when (flags) {
                        1 -> (view as Switch).isEnabled = data as Boolean
                        2 -> view.visibility = if (data as Boolean) View.VISIBLE else View.GONE
                    }
                }
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.layout_compatibility_mode,
                        tipsId = R.string.layout_compatibility_mode_summary
                    ),
                    SwitchV(
                        "layout_compatibility_mode",
                        dataBindingSend = layoutCompatibilityModeBinding.bindingSend
                    )
                )
                Text(
                    resId = R.string.left_margin,
                    dataBindingRecv = layoutCompatibilityModeBinding.binding.getRecv(2)
                )
                SeekBarWithText(
                    "status_bar_left_margin",
                    0,
                    300,
                    0,
                    dataBindingRecv = layoutCompatibilityModeBinding.binding.getRecv(2)
                )
                Text(
                    resId = R.string.right_margin,
                    dataBindingRecv = layoutCompatibilityModeBinding.binding.getRecv(2)
                )
                SeekBarWithText(
                    "status_bar_right_margin",
                    0,
                    300,
                    0,
                    dataBindingRecv = layoutCompatibilityModeBinding.binding.getRecv(2)
                )
                Line()
                TitleText(resId = R.string.status_bar_clock_format)
                val customClockBinding = GetDataBinding(object : DefValue {
                    override fun getValue(): Any {
                        return safeSP.getBoolean("custom_clock_switch", false)
                    }
                }) { view, flags, data ->
                    when (flags) {
                        1 -> (view as Switch).isEnabled = data as Boolean
                        2 -> view.visibility = if (data as Boolean) View.VISIBLE else View.GONE
                    }
                }
                TextWithSwitch(
                    TextV(resId = R.string.custom_clock_switch, colorId = R.color.purple_700),
                    SwitchV(
                        "custom_clock_switch",
                        dataBindingSend = customClockBinding.bindingSend
                    )
                )
                TextWithSwitch(
                    TextV(resId = R.string.status_bar_time_year),
                    SwitchV("status_bar_time_year"),
                    dataBindingRecv = customClockBinding.binding.getRecv(2)
                )
                TextWithSwitch(
                    TextV(resId = R.string.status_bar_time_month),
                    SwitchV("status_bar_time_month"),
                    dataBindingRecv = customClockBinding.binding.getRecv(2)
                )
                TextWithSwitch(
                    TextV(resId = R.string.status_bar_time_day),
                    SwitchV("status_bar_time_day"),
                    dataBindingRecv = customClockBinding.binding.getRecv(2)
                )
                TextWithSwitch(
                    TextV(resId = R.string.status_bar_time_week),
                    SwitchV("status_bar_time_week"),
                    dataBindingRecv = customClockBinding.binding.getRecv(2)
                )
                TextWithSwitch(
                    TextV(resId = R.string.status_bar_time_double_hour),
                    SwitchV("status_bar_time_double_hour"),
                    dataBindingRecv = customClockBinding.binding.getRecv(2)
                )
                TextWithSwitch(
                    TextV(resId = R.string.status_bar_time_period),
                    SwitchV("status_bar_time_period", true),
                    dataBindingRecv = customClockBinding.binding.getRecv(2)
                )
                TextWithSwitch(
                    TextV(resId = R.string.status_bar_time_seconds),
                    SwitchV("status_bar_time_seconds"),
                    dataBindingRecv = customClockBinding.binding.getRecv(2)
                )
                TextWithSwitch(
                    TextV(resId = R.string.status_bar_time_hide_space),
                    SwitchV("status_bar_time_hide_space"),
                    dataBindingRecv = customClockBinding.binding.getRecv(2)
                )
                TextWithSwitch(
                    TextV(resId = R.string.status_bar_time_double_line),
                    SwitchV("status_bar_time_double_line"),
                    dataBindingRecv = customClockBinding.binding.getRecv(2)
                )
                TextWithSwitch(
                    TextV(resId = R.string.status_bar_time_double_line_center_align),
                    SwitchV("status_bar_time_double_line_center_align"),
                    dataBindingRecv = customClockBinding.binding.getRecv(2)
                )
                Text(
                    resId = R.string.status_bar_clock_size,
                    dataBindingRecv = customClockBinding.binding.getRecv(2)
                )
                SeekBarWithText(
                    "status_bar_clock_size", 0, 18, 0,
                    dataBindingRecv = customClockBinding.binding.getRecv(2)
                )
                Text(
                    resId = R.string.status_bar_clock_double_line_size,
                    dataBindingRecv = customClockBinding.binding.getRecv(2)
                )
                SeekBarWithText(
                    "status_bar_clock_double_line_size", 0, 9, 0,
                    dataBindingRecv = customClockBinding.binding.getRecv(2)
                )
                Line()
                TitleText(resId = R.string.status_bar_icon)
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.hide_icon,
                        onClickListener = { showFragment("hide_icon") }
                    )
                )
                TextWithSwitch(
                    TextV(resId = R.string.show_wifi_standard),
                    SwitchV("show_wifi_standard")
                )
                val customMobileTypeTextBinding = GetDataBinding(object : DefValue {
                    override fun getValue(): Any {
                        return safeSP.getBoolean("custom_mobile_type_text_switch", false)
                    }
                }) { view, flags, data ->
                    when (flags) {
                        1 -> (view as Switch).isEnabled = data as Boolean
                        2 -> view.visibility = if (data as Boolean) View.VISIBLE else View.GONE
                    }
                }
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.custom_mobile_type_text_switch
                    ),
                    SwitchV(
                        "custom_mobile_type_text_switch",
                        dataBindingSend = customMobileTypeTextBinding.bindingSend
                    )
                )
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.custom_mobile_type_text
                    ) {
                        MIUIDialog(activity) {
                            setTitle(R.string.custom_mobile_type_text)
                            setEditText(
                                safeSP.getString(
                                    "custom_mobile_type_text", "5G"
                                ),
                                ""
                            )
                            setLButton(textId = R.string.cancel) {
                                dismiss()
                            }
                            setRButton(textId = R.string.Done) {
                                if (getEditText().isNotEmpty()) {
                                    try {
                                        safeSP.putAny("custom_mobile_type_text", getEditText())
                                        dismiss()
                                        return@setRButton
                                    } catch (_: Throwable) {
                                    }
                                }
                                Toast.makeText(
                                    activity,
                                    R.string.input_error,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }.show()
                    }, dataBindingRecv = customMobileTypeTextBinding.binding.getRecv(2)
                )
                val bigMobileTypeIconBinding = GetDataBinding(object : DefValue {
                    override fun getValue(): Any {
                        return safeSP.getBoolean("big_mobile_type_icon", false)
                    }
                }) { view, flags, data ->
                    when (flags) {
                        1 -> (view as Switch).isEnabled = data as Boolean
                        2 -> view.visibility = if (data as Boolean) View.VISIBLE else View.GONE
                    }
                }
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.big_mobile_type_icon
                    ),
                    SwitchV(
                        "big_mobile_type_icon",
                        dataBindingSend = bigMobileTypeIconBinding.bindingSend
                    )
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.big_mobile_type_icon_bold
                    ),
                    SwitchV("big_mobile_type_icon_bold", true),
                    dataBindingRecv = bigMobileTypeIconBinding.binding.getRecv(2)
                )
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.big_mobile_type_icon_size,
                        onClickListener = {
                            MIUIDialog(activity) {
                                setTitle(R.string.big_mobile_type_icon_size)
                                setEditText(
                                    "",
                                    "${activity.getString(R.string.def)}12.5, ${activity.getString(R.string.current)}${
                                        safeSP.getFloat(
                                            "big_mobile_type_icon_size",
                                            12.5f
                                        )
                                    }"
                                )
                                setLButton(textId = R.string.cancel) {
                                    dismiss()
                                }
                                setRButton(textId = R.string.Done) {
                                    if (getEditText() != "") {
                                        safeSP.putAny(
                                            "big_mobile_type_icon_size",
                                            getEditText().toFloat()
                                        )
                                    }
                                    dismiss()
                                }
                            }.show()
                        }), dataBindingRecv = bigMobileTypeIconBinding.binding.getRecv(2)
                )
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.big_mobile_type_icon_up_and_down_position
                    ) {
                        MIUIDialog(activity) {
                            setTitle(R.string.big_mobile_type_icon_up_and_down_position)
                            setMessage("${activity.getString(R.string.range)} -15~15")
                            setEditText(
                                "",
                                "${activity.getString(R.string.def)}0, ${activity.getString(R.string.current)}${
                                    safeSP.getInt(
                                        "big_mobile_type_icon_up_and_down_position",
                                        0
                                    )
                                }"
                            )
                            setLButton(textId = R.string.cancel) {
                                dismiss()
                            }
                            setRButton(textId = R.string.Done) {
                                if (getEditText().isNotEmpty()) {
                                    try {
                                        val value = getEditText().toInt()
                                        if (value in (-15..15)) {
                                            safeSP.putAny(
                                                "big_mobile_type_icon_up_and_down_position",
                                                value
                                            )
                                            dismiss()
                                            return@setRButton
                                        }
                                    } catch (_: Throwable) {
                                    }
                                }
                                Toast.makeText(
                                    activity,
                                    R.string.input_error,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }.show()
                    }, dataBindingRecv = bigMobileTypeIconBinding.binding.getRecv(2)
                )
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.big_mobile_type_icon_left_and_right_margins
                    ) {
                        MIUIDialog(activity) {
                            setTitle(R.string.big_mobile_type_icon_left_and_right_margins)
                            setMessage("${activity.getString(R.string.range)} 0~30")
                            setEditText(
                                "",
                                "${activity.getString(R.string.def)}0, ${activity.getString(R.string.current)}${
                                    safeSP.getInt(
                                        "big_mobile_type_icon_left_and_right_margins",
                                        0
                                    )
                                }"
                            )
                            setLButton(textId = R.string.cancel) {
                                dismiss()
                            }
                            setRButton(textId = R.string.Done) {
                                if (getEditText().isNotEmpty()) {
                                    try {
                                        val value = getEditText().toInt()
                                        if (value in (0..30)) {
                                            safeSP.putAny(
                                                "big_mobile_type_icon_left_and_right_margins",
                                                value
                                            )
                                            dismiss()
                                            return@setRButton
                                        }
                                    } catch (_: Throwable) {
                                    }
                                }
                                Toast.makeText(
                                    activity,
                                    R.string.input_error,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }.show()
                    }, dataBindingRecv = bigMobileTypeIconBinding.binding.getRecv(2)
                )
                Text(
                    resId = R.string.maximum_number_of_notification_icons
                )
                SeekBarWithText(
                    "maximum_number_of_notification_icons",
                    1,
                    30,
                    3
                )
                Text(
                    resId = R.string.maximum_number_of_notification_dots
                )
                SeekBarWithText(
                    "maximum_number_of_notification_dots",
                    0,
                    4,
                    3
                )
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.battery_percentage_font_size,
                        onClickListener = {
                            MIUIDialog(activity) {
                                setTitle(R.string.battery_percentage_font_size)
                                setMessage(R.string.zero_do_no_change)
                                setEditText(
                                    "",
                                    "${activity.getString(R.string.current)}${
                                        safeSP.getFloat(
                                            "battery_percentage_font_size",
                                            0f
                                        )
                                    }"
                                )
                                setLButton(textId = R.string.cancel) {
                                    dismiss()
                                }
                                setRButton(textId = R.string.Done) {
                                    if (getEditText() != "") {
                                        safeSP.putAny(
                                            "battery_percentage_font_size",
                                            getEditText().toFloat()
                                        )
                                    }
                                    dismiss()
                                }
                            }.show()
                        })
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.hide_battery_percentage_icon,
                        tipsId = R.string.hide_battery_percentage_icon_summary
                    ),
                    SwitchV("hide_battery_percentage_icon")
                )
                Line()
                TitleText(resId = R.string.status_bar_network_speed)
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.status_bar_network_speed_refresh_speed,
                        tipsId = R.string.status_bar_network_speed_refresh_speed_summary
                    ),
                    SwitchV("status_bar_network_speed_refresh_speed")
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.hide_status_bar_network_speed_second,
                        tipsId = R.string.hide_status_bar_network_speed_second_summary
                    ),
                    SwitchV("hide_status_bar_network_speed_second")
                )
                TextWithSwitch(
                    TextV(resId = R.string.hide_network_speed_splitter),
                    SwitchV("hide_network_speed_splitter")
                )
                val statusBarDualRowNetworkSpeedBinding = GetDataBinding(object : DefValue {
                    override fun getValue(): Any {
                        return safeSP.getBoolean("status_bar_dual_row_network_speed", false)
                    }
                }) { view, flags, data ->
                    when (flags) {
                        1 -> (view as Switch).isEnabled = data as Boolean
                        2 -> view.visibility = if (data as Boolean) View.VISIBLE else View.GONE
                    }
                }
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.status_bar_dual_row_network_speed,
                        tipsId = R.string.status_bar_dual_row_network_speed_summary
                    ),
                    SwitchV(
                        "status_bar_dual_row_network_speed",
                        dataBindingSend = statusBarDualRowNetworkSpeedBinding.bindingSend
                    )
                )
                val align: HashMap<Int, String> = hashMapOf()
                align[0] = getString(R.string.left)
                align[1] = getString(R.string.right)
                TextWithSpinner(
                    TextV(resId = R.string.status_bar_network_speed_dual_row_gravity),
                    SpinnerV(
                        align[safeSP.getInt(
                            "status_bar_network_speed_dual_row_gravity",
                            0
                        )].toString()
                    ) {
                        add(align[0].toString()) {
                            safeSP.putAny("status_bar_network_speed_dual_row_gravity", 0)
                        }
                        add(align[1].toString()) {
                            safeSP.putAny("status_bar_network_speed_dual_row_gravity", 1)

                        }
                    },
                    dataBindingRecv = statusBarDualRowNetworkSpeedBinding.binding.getRecv(2)
                )
                TextWithSpinner(
                    TextV(resId = R.string.status_bar_network_speed_dual_row_icon),
                    SpinnerV(
                        safeSP.getString(
                            "status_bar_network_speed_dual_row_icon",
                            getString(R.string.none)
                        )
                    ) {
                        add(getString(R.string.none)) {
                            safeSP.putAny(
                                "status_bar_network_speed_dual_row_icon",
                                getString(R.string.none)
                            )
                        }
                        add("▲▼") {
                            safeSP.putAny("status_bar_network_speed_dual_row_icon", "▲▼")
                        }
                        add("△▽") {
                            safeSP.putAny("status_bar_network_speed_dual_row_icon", "△▽")
                        }
                        add("↑↓") {
                            safeSP.putAny("status_bar_network_speed_dual_row_icon", "↑↓")
                        }
                    }
                )
                Text(
                    resId = R.string.status_bar_network_speed_dual_row_size,
                    dataBindingRecv = statusBarDualRowNetworkSpeedBinding.binding.getRecv(2)
                )
                SeekBarWithText(
                    "status_bar_network_speed_dual_row_size",
                    0,
                    9,
                    0,
                    dataBindingRecv = statusBarDualRowNetworkSpeedBinding.binding.getRecv(2)
                )
                Line()
                TitleText(resId = R.string.notification_center)
                val showWeatherMainSwitchBinding = GetDataBinding(object : DefValue {
                    override fun getValue(): Any {
                        return safeSP.getBoolean("notification_weather", false)
                    }
                }) { view, flags, data ->
                    when (flags) {
                        1 -> (view as Switch).isEnabled = data as Boolean
                        2 -> view.visibility = if (data as Boolean) View.VISIBLE else View.GONE
                    }
                }
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.show_weather_main_switch, colorId = R.color.purple_700
                    ),
                    SwitchV(
                        "notification_weather",
                        dataBindingSend = showWeatherMainSwitchBinding.bindingSend
                    )
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.show_city,
                    ),
                    SwitchV("notification_weather_city"),
                    dataBindingRecv = showWeatherMainSwitchBinding.binding.getRecv(2)
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.can_notification_slide,
                        tipsId = R.string.can_notification_slide_summary,
                    ),
                    SwitchV("can_notification_slide"),
                )
                Line()
                TitleText(resId = R.string.control_center)
                val controlCenterWeatherBinding = GetDataBinding(object : DefValue {
                    override fun getValue(): Any {
                        return safeSP.getBoolean("control_center_weather", false)
                    }
                }) { view, flags, data ->
                    when (flags) {
                        1 -> (view as Switch).isEnabled = data as Boolean
                        2 -> view.visibility = if (data as Boolean) View.VISIBLE else View.GONE
                    }
                }
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.show_weather_main_switch,
                        colorId = R.color.purple_700,
                        tipsId = R.string.control_center_weather_summary
                    ),
                    SwitchV(
                        "control_center_weather",
                        dataBindingSend = controlCenterWeatherBinding.bindingSend
                    )
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.show_city,
                    ),
                    SwitchV("control_center_weather_city"),
                    dataBindingRecv = controlCenterWeatherBinding.binding.getRecv(2)
                )
                Line()
                TitleText(resId = R.string.lock_screen)
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.lock_screen_clock_display_seconds,
                        tipsId = R.string.only_official_default_themes_are_supported
                    ),
                    SwitchV("lock_screen_clock_display_seconds")
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.remove_the_left_side_of_the_lock_screen,
                        tipsId = R.string.only_official_default_themes_are_supported
                    ),
                    SwitchV("remove_the_left_side_of_the_lock_screen")
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.remove_lock_screen_camera,
                        tipsId = R.string.only_official_default_themes_are_supported
                    ),
                    SwitchV("remove_lock_screen_camera")
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.enable_wave_charge_animation
                    ),
                    SwitchV("enable_wave_charge_animation")
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.lock_screen_charging_current,
                        tipsId = R.string.only_official_default_themes_are_supported
                    ),
                    SwitchV("lock_screen_charging_current")
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.double_tap_to_sleep,
                        tipsId = R.string.home_double_tap_to_sleep_summary
                    ), SwitchV("lock_screen_double_tap_to_sleep")
                )
                Line()
                TitleText(resId = R.string.old_quick_settings_panel)
                val oldQSCustomSwitchBinding = GetDataBinding(object : DefValue {
                    override fun getValue(): Any {
                        return safeSP.getBoolean("old_qs_custom_switch", false)
                    }
                }) { view, flags, data ->
                    when (flags) {
                        1 -> (view as Switch).isEnabled = data as Boolean
                        2 -> view.visibility = if (data as Boolean) View.VISIBLE else View.GONE
                    }
                }
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.old_qs_custom_switch,
                        colorId = R.color.purple_700
                    ),
                    SwitchV(
                        "old_qs_custom_switch",
                        dataBindingSend = oldQSCustomSwitchBinding.bindingSend
                    )
                )
                Text(
                    resId = R.string.qs_custom_rows,
                    dataBindingRecv = oldQSCustomSwitchBinding.binding.getRecv(2)
                )
                SeekBarWithText(
                    "qs_custom_rows",
                    1,
                    6,
                    3,
                    dataBindingRecv = oldQSCustomSwitchBinding.binding.getRecv(2)
                )
                Text(
                    resId = R.string.qs_custom_rows_horizontal,
                    dataBindingRecv = oldQSCustomSwitchBinding.binding.getRecv(2)
                )
                SeekBarWithText(
                    "qs_custom_rows_horizontal",
                    1,
                    3,
                    2,
                    dataBindingRecv = oldQSCustomSwitchBinding.binding.getRecv(2)
                )
                Text(
                    resId = R.string.qs_custom_columns,
                    dataBindingRecv = oldQSCustomSwitchBinding.binding.getRecv(2)
                )
                SeekBarWithText(
                    "qs_custom_columns",
                    1,
                    7,
                    4,
                    dataBindingRecv = oldQSCustomSwitchBinding.binding.getRecv(2)
                )
                Text(
                    resId = R.string.qs_custom_columns_unexpanded,
                    dataBindingRecv = oldQSCustomSwitchBinding.binding.getRecv(2)
                )
                SeekBarWithText(
                    "qs_custom_columns_unexpanded",
                    1,
                    7,
                    5,
                    dataBindingRecv = oldQSCustomSwitchBinding.binding.getRecv(2)
                )
            }
            register("scope_android", getString(R.string.scope_android), false) {
                TitleText(resId = R.string.corepacth)

                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.downgr,
                        tipsId = R.string.downgr_summary
                    ),
                    SwitchV("downgrade")
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.authcreak,
                        tipsId = R.string.authcreak_summary
                    ),
                    SwitchV("authcreak")
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.digestCreak,
                        tipsId = R.string.digestCreak_summary
                    ),
                    SwitchV("digestCreak")
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.UsePreSig,
                        tipsId = R.string.UsePreSig_summary
                    ),
                    SwitchV("UsePreSig")
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.enhancedMode,
                        tipsId = R.string.enhancedMode_summary
                    ),
                    SwitchV("enhancedMode")
                )
                Line()
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.disable_flag_secure,
                        tipsId = R.string.disable_flag_secure_summary
                    ),
                    SwitchV("disable_flag_secure")
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.delete_on_post_notification,
                        tipsId = R.string.delete_on_post_notification_summary
                    ),
                    SwitchV("delete_on_post_notification")
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.remove_small_window_restrictions,
                        tipsId = R.string.remove_small_window_restrictions_summary
                    ),
                    SwitchV("remove_small_window_restrictions")
                )
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.max_wallpaper_scale,
                        onClickListener = {
                            MIUIDialog(activity) {
                                setTitle(R.string.max_wallpaper_scale)
                                setEditText(
                                    "",
                                    "${activity.getString(R.string.def)}1.1, ${activity.getString(R.string.current)}${
                                        safeSP.getFloat(
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
                                        safeSP.putAny(
                                            "max_wallpaper_scale",
                                            getEditText().toFloat()
                                        )
                                    }
                                    dismiss()
                                }
                            }.show()
                        })
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.allow_untrusted_touches,
                        tipsId = R.string.take_effect_after_reboot
                    ),
                    SwitchV("allow_untrusted_touches")
                )
                Line()
                TitleText(resId = R.string.sound)
                val mediaVolumeStepsSwitchBinding = GetDataBinding(
                    object : DefValue {
                        override fun getValue(): Any {
                            return safeSP.getBoolean("media_volume_steps_switch", false)
                        }
                    }
                ) { view, flags, data ->
                    when (flags) {
                        1 -> (view as Switch).isEnabled = data as Boolean
                        2 -> view.visibility = if (data as Boolean) View.VISIBLE else View.GONE
                    }
                }
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.media_volume_steps_switch,
                        tips = "${getString(R.string.take_effect_after_reboot)}\n${getString(R.string.media_volume_steps_summary)}"
                    ),
                    SwitchV(
                        "media_volume_steps_switch",
                        dataBindingSend = mediaVolumeStepsSwitchBinding.bindingSend
                    )
                )
                SeekBarWithText(
                    "media_volume_steps",
                    15,
                    29,
                    15, dataBindingRecv = mediaVolumeStepsSwitchBinding.binding.getRecv(2)
                )
            }
            register("scope_other", getString(R.string.scope_other), false) {
                TitleText(resId = R.string.scope_miuihome)
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.home_time,
                        tipsId = R.string.home_time_summary
                    ), SwitchV("home_time")
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.double_tap_to_sleep,
                        tipsId = R.string.home_double_tap_to_sleep_summary
                    ), SwitchV("double_tap_to_sleep")
                )
                Line()
                TitleText(resId = R.string.scope_powerkeeper)
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.lock_max_fps,
                        tipsId = R.string.lock_max_fps_summary
                    ),
                    SwitchV("lock_max_fps")
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.prevent_recovery_of_battery_optimization_white_list,
                        tipsId = R.string.failed_after_restart
                    ),
                    SwitchV("prevent_recovery_of_battery_optimization_white_list")
                )
                TextSummaryArrow(
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
                Line()
                TitleText(resId = R.string.scope_securitycenter)
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.skip_waiting_time,
                        tipsId = R.string.skip_waiting_time_summary
                    ),
                    SwitchV("skip_waiting_time")
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.remove_open_app_confirmation_popup,
                        tipsId = R.string.remove_open_app_confirmation_popup_summary
                    ),
                    SwitchV("remove_open_app_confirmation_popup")
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.lock_one_hundred,
                        tipsId = R.string.lock_one_hundred_summary
                    ),
                    SwitchV("lock_one_hundred")
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.remove_macro_blacklist
                    ),
                    SwitchV("remove_macro_blacklist")
                )
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.battery_life_function
                    ),
                    SwitchV("battery_life_function")
                )
                Line()
                TitleText(resId = R.string.scope_mediaeditor)
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.unlock_unlimited_cropping,
                        tipsId = R.string.unlock_unlimited_cropping_summary
                    ),
                    SwitchV("unlock_unlimited_cropping")
                )
                Line()
                TitleText(resId = R.string.updater)
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.remove_ota_validate,
                        tipsId = R.string.remove_ota_validate_summary
                    ),
                    SwitchV("remove_ota_validate")
                )
                Line()
                TitleText(resId = R.string.settings)
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.show_notification_importance,
                        tipsId = R.string.show_notification_importance_summary
                    ),
                    SwitchV("show_notification_importance")
                )
                Line()
                TitleText(resId = R.string.cast)
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.force_support_send_app,
                    ),
                    SwitchV("force_support_send_app")
                )
                Line()
                TitleText(resId = R.string.rear_display)
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.show_weather_main_switch,
                    ),
                    SwitchV("rear_show_weather")
                )
                Line()
                TitleText(resId = R.string.remove_ad)
                TextSummaryWithSwitch(
                    TextSummaryV(
                        textId = R.string.remove_thememanager_ads
                    ),
                    SwitchV("remove_thememanager_ads")
                )
            }
            register("hide_icon", getString(R.string.hide_icon), false) {
                TitleText(resId = R.string.status_bar_icon)
                TextWithSwitch(
                    TextV(resId = R.string.hide_battery_icon),
                    SwitchV("hide_battery_icon")
                )
                TextWithSwitch(
                    TextV(resId = R.string.hide_battery_charging_icon),
                    SwitchV("hide_battery_charging_icon")
                )
                TextWithSwitch(
                    TextV(resId = R.string.hide_gps_icon),
                    SwitchV("hide_gps_icon")
                )
                TextWithSwitch(
                    TextV(resId = R.string.hide_bluetooth_icon),
                    SwitchV("hide_bluetooth_icon")
                )

                TextWithSwitch(
                    TextV(resId = R.string.hide_nfc_icon),
                    SwitchV("hide_nfc_icon")
                )
                TextWithSwitch(
                    TextV(resId = R.string.hide_bluetooth_battery_icon),
                    SwitchV("hide_bluetooth_battery_icon")
                )
                TextWithSwitch(
                    TextV(resId = R.string.hide_small_hd_icon),
                    SwitchV("hide_small_hd_icon")
                )
                TextWithSwitch(
                    TextV(resId = R.string.hide_big_hd_icon),
                    SwitchV("hide_big_hd_icon")
                )
                TextWithSwitch(
                    TextV(resId = R.string.hide_hd_no_service_icon),
                    SwitchV("hide_hd_no_service_icon")
                )
                TextWithSwitch(
                    TextV(resId = R.string.hide_no_sim_icon),
                    SwitchV("hide_no_sim_icon")
                )
                TextWithSwitch(
                    TextV(resId = R.string.hide_sim_one_icon),
                    SwitchV("hide_sim_one_icon")
                )
                TextWithSwitch(
                    TextV(resId = R.string.hide_sim_two_icon),
                    SwitchV("hide_sim_two_icon")
                )
                TextWithSwitch(
                    TextV(resId = R.string.hide_mobile_activity_icon),
                    SwitchV("hide_mobile_activity_icon")
                )
                TextWithSwitch(
                    TextV(resId = R.string.hide_mobile_type_icon),
                    SwitchV("hide_mobile_type_icon")
                )
                TextWithSwitch(
                    TextV(resId = R.string.hide_wifi_icon),
                    SwitchV("hide_wifi_icon")
                )
                TextWithSwitch(
                    TextV(resId = R.string.hide_wifi_activity_icon),
                    SwitchV("hide_wifi_activity_icon")
                )
                TextWithSwitch(
                    TextV(resId = R.string.hide_wifi_standard_icon),
                    SwitchV("hide_wifi_standard_icon")
                )
                TextWithSwitch(
                    TextV(resId = R.string.hide_slave_wifi_icon),
                    SwitchV("hide_slave_wifi_icon")
                )
                TextWithSwitch(
                    TextV(resId = R.string.hide_hotspot_icon),
                    SwitchV("hide_hotspot_icon")
                )
                TextWithSwitch(
                    TextV(resId = R.string.hide_vpn_icon),
                    SwitchV("hide_vpn_icon")
                )
                TextWithSwitch(
                    TextV(resId = R.string.hide_airplane_icon),
                    SwitchV("hide_airplane_icon")
                )
                TextWithSwitch(
                    TextV(resId = R.string.hide_alarm_icon),
                    SwitchV("hide_alarm_icon")
                )
                TextWithSwitch(
                    TextV(resId = R.string.hide_headset_icon),
                    SwitchV("hide_headset_icon")
                )
                TextWithSwitch(
                    TextV(resId = R.string.hide_volume_icon),
                    SwitchV("hide_volume_icon")
                )
                TextWithSwitch(
                    TextV(resId = R.string.hide_zen_icon),
                    SwitchV("hide_zen_icon")
                )
            }
            register("about_module", getString(R.string.about_module), true) {
                Author(
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
                Line()
                TitleText(text = getString(R.string.developer))
                Author(
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
                Line()
                TitleText(text = getString(R.string.thank_list))
                TextSummaryArrow(
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
                TextSummaryArrow(
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
                Line()
                TitleText(text = getString(R.string.discussions))
                TextSummaryArrow(TextSummaryV(textId = R.string.qq_channel, onClickListener = {
                    try {
                        val uri =
                            Uri.parse("https://qun.qq.com/qqweb/qunpro/share?_wv=3&_wwv=128&inviteCode=29Mu64&from=246610&biz=ka")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(activity, "访问失败", Toast.LENGTH_SHORT).show()
                    }
                }))
                TextSummaryArrow(
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
                TextSummaryArrow(
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
                Line()
                TitleText(getString(R.string.other))
                TextSummaryArrow(
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
                                Toast.makeText(activity, "本机未安装酷安应用", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        })
                )
                TextSummaryArrow(
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
                TextSummaryArrow(
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
            }
            registerMenu(getString(R.string.menu)) {
                TextSummaryArrow(
                    TextSummaryV(textId = R.string.reboot, onClickListener = {
                        MIUIDialog(activity) {
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
                        }.show()
                    })
                )

                TextSummaryArrow(
                    TextSummaryV(textId = R.string.reboot_host, onClickListener = {
                        MIUIDialog(activity) {
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
                                    "killall com.miui.screenshot",
                                    "killall com.milink.service",
                                    "killall com.xiaomi.misubscreenui",
                                )
                                ShellUtils.execCommand(command, true)
                                dismiss()
                            }
                        }.show()
                    })
                )
            }
        }
    }
}