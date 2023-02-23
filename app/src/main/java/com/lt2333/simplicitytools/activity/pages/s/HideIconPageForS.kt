package com.lt2333.simplicitytools.activity.pages.s

import android.view.View
import android.widget.Switch
import cn.fkj233.ui.activity.MIUIActivity
import cn.fkj233.ui.activity.annotation.BMPage
import cn.fkj233.ui.activity.data.BasePage
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import cn.fkj233.ui.activity.view.TextV
import cn.fkj233.ui.dialog.MIUIDialog
import com.lt2333.simplicitytools.R


@BMPage("hide_icon","Hide Icon", hideMenu = false)
class HideIconPageForS : BasePage() {

    override fun onCreate() {
        TitleText(textId = R.string.status_bar_icon)
        TextWithSwitch(
            TextV(textId = R.string.hide_battery_icon),
            SwitchV("hide_battery_icon")
        )
        TextWithSwitch(
            TextV(textId = R.string.hide_battery_charging_icon),
            SwitchV("hide_battery_charging_icon")
        )
        TextWithSwitch(TextV(textId = R.string.hide_gps_icon), SwitchV("hide_gps_icon"))
        TextWithSwitch(
            TextV(textId = R.string.hide_bluetooth_icon),
            SwitchV("hide_bluetooth_icon")
        )

        TextWithSwitch(TextV(textId = R.string.hide_nfc_icon), SwitchV("hide_nfc_icon"))
        TextWithSwitch(
            TextV(textId = R.string.hide_bluetooth_battery_icon),
            SwitchV("hide_bluetooth_battery_icon")
        )
        TextWithSwitch(
            TextV(textId = R.string.hide_small_hd_icon),
            SwitchV("hide_small_hd_icon")
        )
        TextWithSwitch(
            TextV(textId = R.string.hide_big_hd_icon),
            SwitchV("hide_big_hd_icon")
        )
        TextWithSwitch(
            TextV(textId = R.string.hide_new_hd_icon),
            SwitchV("hide_new_hd_icon")
        )
        TextWithSwitch(
            TextV(textId = R.string.hide_hd_no_service_icon),
            SwitchV("hide_hd_no_service_icon")
        )
        TextWithSwitch(
            TextV(textId = R.string.hide_no_sim_icon),
            SwitchV("hide_no_sim_icon")
        )
        TextWithSwitch(
            TextV(textId = R.string.hide_sim_one_icon),
            SwitchV("hide_sim_one_icon")
        )
        TextWithSwitch(
            TextV(textId = R.string.hide_sim_two_icon),
            SwitchV("hide_sim_two_icon")
        )
        TextWithSwitch(
            TextV(textId = R.string.hide_mobile_activity_icon),
            SwitchV("hide_mobile_activity_icon")
        )
        TextWithSwitch(
            TextV(textId = R.string.hide_mobile_type_icon),
            SwitchV("hide_mobile_type_icon")
        )
        TextWithSwitch(TextV(textId = R.string.hide_wifi_icon), SwitchV("hide_wifi_icon"))
        TextWithSwitch(
            TextV(textId = R.string.hide_wifi_activity_icon),
            SwitchV("hide_wifi_activity_icon")
        )
        TextWithSwitch(
            TextV(textId = R.string.hide_wifi_standard_icon),
            SwitchV("hide_wifi_standard_icon")
        )
        TextWithSwitch(
            TextV(textId = R.string.hide_slave_wifi_icon),
            SwitchV("hide_slave_wifi_icon")
        )
        TextWithSwitch(
            TextV(textId = R.string.hide_hotspot_icon),
            SwitchV("hide_hotspot_icon")
        )
        TextWithSwitch(TextV(textId = R.string.hide_vpn_icon), SwitchV("hide_vpn_icon"))
        TextWithSwitch(
            TextV(textId = R.string.hide_airplane_icon),
            SwitchV("hide_airplane_icon")
        )
        TextWithSwitch(TextV(textId = R.string.hide_alarm_icon), SwitchV("hide_alarm_icon"))
        TextWithSwitch(
            TextV(textId = R.string.hide_headset_icon),
            SwitchV("hide_headset_icon")
        )
        TextWithSwitch(
            TextV(textId = R.string.hide_volume_icon),
            SwitchV("hide_volume_icon")
        )
        TextWithSwitch(TextV(textId = R.string.hide_zen_icon), SwitchV("hide_zen_icon"))
    }

}
