package com.lt2333.simplicitytools.hooks.rules.all.miuihome

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.github.kyuubiran.ezxhelper.utils.Log
import com.github.kyuubiran.ezxhelper.utils.args
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.getObject
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.github.kyuubiran.ezxhelper.utils.invokeMethod
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister

object ScrollIconNameForAll : HookRegister() {
    @SuppressLint("DiscouragedApi")
    override fun init() = hasEnable("miuihome_scroll_icon_name") {
        try {
            findMethod("com.miui.home.launcher.ItemIcon") {
                name == "onFinishInflate"
            }.hookAfter {
                val mTitle = it.thisObject.getObject("mTitle") as TextView
                mTitleScrolling(mTitle)
            }
            findMethod("com.miui.home.launcher.maml.MaMlWidgetView") {
                name == "onFinishInflate"
            }.hookAfter {
                val mTitle = it.thisObject.getObject("mTitle") as TextView
                mTitleScrolling(mTitle)
            }
            findMethod("com.miui.home.launcher.LauncherMtzGadgetView") {
                name == "onFinishInflate"
            }.hookAfter {
                val mTitle = it.thisObject.getObject("mTitle") as TextView
                mTitleScrolling(mTitle)
            }
            findMethod("com.miui.home.launcher.LauncherWidgetView") {
                name == "onFinishInflate"
            }.hookAfter {
                val mTitle = it.thisObject.getObject("mTitle") as TextView
                mTitleScrolling(mTitle)
            }
            findMethod("com.miui.home.launcher.ShortcutIcon") {
                name == "fromXml" && parameterCount == 4
            }.hookAfter {
                val buddyIconView = it.args[3].invokeMethod("getBuddyIconView", args(it.args[2])) as View
                val mTitle = buddyIconView.getObject("mTitle") as TextView
                mTitleScrolling(mTitle)
            }
            findMethod("com.miui.home.launcher.ShortcutIcon") {
                name == "createShortcutIcon" && parameterCount == 3
            }.hookAfter {
                val buddyIcon = it.result as View
                val mTitle = buddyIcon.getObject("mTitle") as TextView
                mTitleScrolling(mTitle)
            }
            findMethod("com.miui.home.launcher.common.Utilities") {
                name == "adaptTitleStyleToWallpaper" && parameterCount == 4
            }.hookAfter {
                val mTitle = it.args[1] as TextView
                if (mTitle.id == mTitle.resources.getIdentifier("icon_title", "id", "com.miui.home")) {
                    mTitleScrolling(mTitle)
                }
            }
        } catch (e: Throwable) {
            Log.ex(e)
        }
    }

    private fun mTitleScrolling(mTitle: TextView) {
        mTitle.ellipsize = TextUtils.TruncateAt.MARQUEE
        mTitle.isHorizontalFadingEdgeEnabled = true
        mTitle.setSingleLine()
        mTitle.marqueeRepeatLimit = -1
        mTitle.isSelected = true
        mTitle.setHorizontallyScrolling(true)
    }

}
