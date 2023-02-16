package com.lt2333.simplicitytools.activity.pages.t

import android.content.ComponentName
import android.content.Intent
import android.view.View
import android.widget.Switch
import android.widget.Toast
import cn.fkj233.ui.activity.MIUIActivity
import cn.fkj233.ui.activity.annotation.BMPage
import cn.fkj233.ui.activity.data.BasePage
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import com.lt2333.simplicitytools.R


@BMPage("scope_other", "Other", hideMenu = false)
class OtherPageForT : BasePage() {

    override fun onCreate() {
        TitleText(textId = R.string.scope_miuihome)
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
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.miuihome_recentwiew_wallpaper_darkening, tipsId = R.string.miuihome_recentwiew_wallpaper_darkening_summary
            ), SwitchV("miuihome_recentwiew_wallpaper_darkening", false)
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.miuihome_recentview_remove_card_animation, tipsId = R.string.miuihome_recentview_remove_card_animation_summary
            ), SwitchV("miuihome_recentview_remove_card_animation", false)
        )
        TextSummaryWithSwitch(
            TextSummaryV(textId = R.string.miuihome_shortcut_add_small_window, tipsId = R.string.miuihome_shortcut_add_small_window_summary),
            SwitchV("miuihome_shortcut_add_small_window", false)
        )
        TextSummaryWithSwitch(
            TextSummaryV(textId = R.string.miuihome_scroll_icon_name, tipsId = R.string.miuihome_scroll_icon_name_summary),
            SwitchV("miuihome_scroll_icon_name", false)
        )
        Line()
        TitleText(textId = R.string.scope_powerkeeper)
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.lock_max_fps,
                tipsId = R.string.lock_max_fps_summary
            ), SwitchV("lock_max_fps")
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.prevent_recovery_of_battery_optimization_white_list,
                tipsId = R.string.failed_after_restart
            ), SwitchV("prevent_recovery_of_battery_optimization_white_list")
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.do_not_clear_app,
                tipsId = R.string.do_not_clear_app_summary
            ), SwitchV("do_not_clear_app")
        )
        val makeMilletMoreAggressiveSwitchBinding = GetDataBinding({
            MIUIActivity.safeSP.getBoolean(
                "make_millet_more_aggressive",
                false
            )
        }) { view, flags, data ->
            when (flags) {
                1 -> (view as Switch).isEnabled = data as Boolean
                2 -> view.visibility = if (data as Boolean) View.VISIBLE else View.GONE
            }
        }
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.make_millet_more_aggressive,
                tipsId = R.string.make_millet_more_aggressive_summary
            ),
            SwitchV(
                "make_millet_more_aggressive",
                dataBindingSend = makeMilletMoreAggressiveSwitchBinding.bindingSend
            )
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.make_millet_ignore_active,
                tipsId = R.string.make_millet_ignore_active_summary
            ),
            SwitchV("make_millet_ignore_active"),
            dataBindingRecv = makeMilletMoreAggressiveSwitchBinding.binding.getRecv(2)
        )
        TextSummaryWithArrow(
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
                        activity.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(activity, "启动失败，可能是不支持", Toast.LENGTH_LONG).show()
                    }
                })
        )
        Line()
        TitleText(textId = R.string.scope_securitycenter)
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.skip_waiting_time,
                tipsId = R.string.skip_waiting_time_summary
            ), SwitchV("skip_waiting_time")
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.remove_open_app_confirmation_popup,
                tipsId = R.string.remove_open_app_confirmation_popup_summary
            ), SwitchV("remove_open_app_confirmation_popup")
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.lock_one_hundred,
                tipsId = R.string.lock_one_hundred_summary
            ), SwitchV("lock_one_hundred")
        )
        TextSummaryWithSwitch(
            TextSummaryV(textId = R.string.remove_macro_blacklist),
            SwitchV("remove_macro_blacklist")
        )
        TextSummaryWithSwitch(
            TextSummaryV(textId = R.string.battery_life_function),
            SwitchV("battery_life_function")
        )
        Line()
        TitleText(textId = R.string.scope_mediaeditor)
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.unlock_unlimited_cropping,
                tipsId = R.string.unlock_unlimited_cropping_summary
            ), SwitchV("unlock_unlimited_cropping")
        )
        Line()
        TitleText(textId = R.string.updater)
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.remove_ota_validate,
                tipsId = R.string.remove_ota_validate_summary
            ), SwitchV("remove_ota_validate")
        )
        Line()
        TitleText(textId = R.string.settings)
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.show_notification_importance,
                tipsId = R.string.show_notification_importance_summary
            ), SwitchV("show_notification_importance")
        )
        Line()
        TitleText(textId = R.string.cast)
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.force_support_send_app,
            ), SwitchV("force_support_send_app")
        )
        Line()
        TitleText(textId = R.string.rear_display)
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.show_weather_main_switch,
            ), SwitchV("rear_show_weather")
        )
        Line()
        TitleText(textId = R.string.remove_ad)
        TextSummaryWithSwitch(
            TextSummaryV(textId = R.string.remove_thememanager_ads),
            SwitchV("remove_thememanager_ads")
        )
        Line()
        TitleText(textId = R.string.pkg_installer)
        TextSummaryWithSwitch(
            TextSummaryV(textId = R.string.pkg_installer_summary),
            SwitchV("pkg_installer_count_checking")
        )
    }

}
