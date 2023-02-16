package com.lt2333.simplicitytools.activity.pages.s

import android.content.ComponentName
import android.content.pm.PackageManager
import cn.fkj233.ui.activity.annotation.BMMainPage
import cn.fkj233.ui.activity.data.BasePage
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import cn.fkj233.ui.dialog.MIUIDialog
import com.lt2333.simplicitytools.BuildConfig
import com.lt2333.simplicitytools.R


@BMMainPage("WooBox[MIUI13]")
class MainPageForS : BasePage() {

    override fun onCreate() {
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.main_switch,
                colorId = R.color.blue
            ), SwitchV("main_switch", true)
        )
        TextSummaryWithSwitch(
            TextSummaryV(textId = R.string.HideLauncherIcon),
            SwitchV("hLauncherIcon", onClickListener = {
                activity.packageManager.setComponentEnabledSetting(
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
        TextSummaryWithArrow(
            TextSummaryV(
                textId = R.string.matters_needing_attention,
                colorId = R.color.red,
                onClickListener = {
                    MIUIDialog(activity) {
                        setTitle(R.string.matters_needing_attention)
                        setMessage(R.string.matters_needing_attention_context)
                        setRButton(R.string.Done) {
                            dismiss()
                        }
                    }.show()
                })
        )
        Line()
        TitleText(textId = R.string.scope)
        TextSummaryWithArrow(
            TextSummaryV(
                textId = R.string.scope_systemui,
                tipsId = R.string.scope_systemui_summary,
                onClickListener = { showFragment("scope_systemui") })
        )
        TextSummaryWithArrow(
            TextSummaryV(
                textId = R.string.scope_android,
                tipsId = R.string.scope_android_summary,
                onClickListener = { showFragment("scope_android") })
        )
        TextSummaryWithArrow(
            TextSummaryV(
                textId = R.string.scope_other,
                tipsId = R.string.scope_other_summary,
                onClickListener = { showFragment("scope_other") })
        )
        Line()
        TitleText(textId = R.string.about)
        TextSummaryWithArrow(
            TextSummaryV(
                textId = R.string.about_module,
                tips = getString(R.string.about_module_summary),
                onClickListener = { showFragment("about_module") })
        )
    }

}
