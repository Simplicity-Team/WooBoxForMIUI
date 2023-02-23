package com.lt2333.simplicitytools.activity.pages.t

import android.view.View
import android.widget.Switch
import cn.fkj233.ui.activity.MIUIActivity
import cn.fkj233.ui.activity.annotation.BMPage
import cn.fkj233.ui.activity.data.BasePage
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import cn.fkj233.ui.dialog.MIUIDialog
import com.lt2333.simplicitytools.R


@BMPage("scope_android","Android", hideMenu = false)
class AndroidPageForT : BasePage() {

    override fun onCreate() {
        TitleText(textId = R.string.corepacth)

        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.downgr,
                tipsId = R.string.downgr_summary
            ), SwitchV("downgrade")
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.authcreak,
                tipsId = R.string.authcreak_summary
            ), SwitchV("authcreak")
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.digestCreak,
                tipsId = R.string.digestCreak_summary
            ), SwitchV("digestCreak")
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.UsePreSig,
                tipsId = R.string.UsePreSig_summary
            ), SwitchV("UsePreSig")
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.enhancedMode,
                tipsId = R.string.enhancedMode_summary
            ), SwitchV("enhancedMode")
        )
        Line()
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.disable_flag_secure,
                tipsId = R.string.disable_flag_secure_summary
            ), SwitchV("disable_flag_secure")
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.delete_on_post_notification,
                tipsId = R.string.delete_on_post_notification_summary
            ), SwitchV("delete_on_post_notification")
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.remove_small_window_restrictions,
                tipsId = R.string.remove_small_window_restrictions_summary
            ), SwitchV("remove_small_window_restrictions")
        )
        TextSummaryWithArrow(
            TextSummaryV(
                textId = R.string.max_wallpaper_scale,
                onClickListener = {
                    MIUIDialog(activity) {
                        setTitle(R.string.max_wallpaper_scale)
                        setEditText(
                            "",
                            "${activity.getString(R.string.def)}1.2, ${activity.getString(R.string.current)}${
                                MIUIActivity.safeSP.getFloat("max_wallpaper_scale", 1.2f)
                            }"
                        )
                        setLButton(textId = R.string.cancel) {
                            dismiss()
                        }
                        setRButton(textId = R.string.Done) {
                            if (getEditText() != "") {
                                MIUIActivity.safeSP.putAny(
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
            ), SwitchV("allow_untrusted_touches")
        )
        Line()
        TitleText(textId = R.string.sound)
        val mediaVolumeStepsSwitchBinding = GetDataBinding({
            MIUIActivity.safeSP.getBoolean(
                "media_volume_steps_switch",
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
            15,
            dataBindingRecv = mediaVolumeStepsSwitchBinding.binding.getRecv(2)
        )
    }

}
