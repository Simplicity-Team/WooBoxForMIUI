package com.lt2333.simplicitytools.hooks.rules.t.systemui

import android.content.Context
import android.content.res.Resources
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.kyuubiran.ezxhelper.utils.*
import com.lt2333.simplicitytools.utils.XSPUtils
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister


object StatusBarBigMobileTypeIconForT : HookRegister() {

    private val getLocation = XSPUtils.getInt("big_mobile_type_location", 1)
    private val upAndDownPosition = XSPUtils.getInt("big_mobile_type_icon_up_and_down_position", 0)
    private val leftAndRightMargin = XSPUtils.getInt("big_mobile_type_icon_left_and_right_margins", 0)
    private val isBold = XSPUtils.getBoolean("big_mobile_type_icon_bold", true)
    private val size = XSPUtils.getFloat("big_mobile_type_icon_size", 12.5f)
    private val isOnlyShowNetwork = XSPUtils.getBoolean("big_mobile_type_only_show_network_card", false)

    override fun init() = hasEnable("big_mobile_type_icon") {
        //使网络类型单独显示
        findMethod("com.android.systemui.statusbar.StatusBarMobileView") {
            name == "applyMobileState"
        }.hookBefore {
            val mobileIconState = it.args[0]
            mobileIconState.putObject("showMobileDataTypeSingle", true)
        }

        findMethod("com.android.systemui.statusbar.StatusBarMobileView") {
            name == "initViewState"
        }.hookAfter {
            val mobileIconState = it.args[0]
            val statusBarMobileView = it.thisObject as ViewGroup
            val context: Context = statusBarMobileView.context
            val res: Resources = context.resources

            //获取组件
            val mobileContainerLeftId: Int = res.getIdentifier("mobile_container_left", "id", "com.android.systemui")
            val mobileContainerLeft = statusBarMobileView.findViewById<ViewGroup>(mobileContainerLeftId)

            val mobileGroupId: Int = res.getIdentifier("mobile_group", "id", "com.android.systemui")
            val mobileGroup = statusBarMobileView.findViewById<ViewGroup>(mobileGroupId)

            val mobileTypeSingleId: Int = res.getIdentifier("mobile_type_single", "id", "com.android.systemui")
            val mobileTypeSingle = statusBarMobileView.findViewById<TextView>(mobileTypeSingleId)

            //更改顺序
            if (getLocation == 1) {
                mobileGroup.removeView(mobileTypeSingle)
                mobileGroup.addView(mobileTypeSingle)
                mobileGroup.removeView(mobileContainerLeft)
                mobileGroup.addView(mobileContainerLeft)
            }

            //更改样式
            mobileTypeSingle.textSize = size
            if (isBold) {
                mobileTypeSingle.typeface = Typeface.DEFAULT_BOLD
            }
            mobileTypeSingle.setPadding(
                leftAndRightMargin, upAndDownPosition, leftAndRightMargin, 0
            )

            it.thisObject.invokeMethodAuto("updateState",mobileIconState)
        }
        //显示非上网卡的大图标
        if (!isOnlyShowNetwork) {
            findMethod("com.android.systemui.statusbar.StatusBarMobileView") {
                name == "updateState"
            }.hookAfter {
                val mobileIconState = it.args[0]
                val statusBarMobileView = it.thisObject as ViewGroup
                val context: Context = statusBarMobileView.context
                val res: Resources = context.resources

                val mobileTypeSingleId: Int = res.getIdentifier("mobile_type_single", "id", "com.android.systemui")
                val mobileTypeSingle = statusBarMobileView.findViewById<TextView>(mobileTypeSingleId)

                if (!mobileIconState.getObjectAs<Boolean>("dataConnected") && !mobileIconState.getObjectAs<Boolean>("wifiAvailable")) {
                    mobileTypeSingle.visibility = View.VISIBLE
                }
            }
        }
    }
}