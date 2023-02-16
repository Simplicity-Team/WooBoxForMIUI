package com.lt2333.simplicitytools.activity.pages.all

import cn.fkj233.ui.activity.annotation.BMMenuPage
import cn.fkj233.ui.activity.data.BasePage
import cn.fkj233.ui.activity.view.TextSummaryV
import cn.fkj233.ui.dialog.MIUIDialog
import com.lt2333.simplicitytools.R
import com.lt2333.simplicitytools.utils.ShellUtils
import java.util.*


@BMMenuPage("Menu")
class MenuPage : BasePage() {

    override fun onCreate() {
        TextSummaryWithArrow(TextSummaryV(textId = R.string.reboot, onClickListener = {
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
        }))

        TextSummaryWithArrow(TextSummaryV(textId = R.string.reboot_host, onClickListener = {
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
                        "killall com.miui.packageinstaller"
                    )
                    ShellUtils.execCommand(command, true)
                    dismiss()
                }
            }.show()
        }))
    }

}
