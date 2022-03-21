package com.lt2333.simplicitytools.hook.app.systemui

import android.content.Context
import android.content.res.Resources
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import cn.fkj233.ui.activity.dp2px
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookAfterMethod
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage


class StatusBarBigMobileTypeIcon : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        hasEnable("big_mobile_type_icon") {
            "com.android.systemui.statusbar.StatusBarMobileView".hookAfterMethod(
                lpparam.classLoader,
                "init"
            ) {
                val StatusBarMobileView = it.thisObject as ViewGroup
                val context: Context = StatusBarMobileView.context
                val res: Resources = context.resources
                val mobile_container_left_ID: Int =
                    res.getIdentifier("mobile_container_left", "id", "com.android.systemui")
                val mobile_left_mobile_inout_ID: Int = res.getIdentifier(
                    "mobile_left_mobile_inout",
                    "id",
                    "com.android.systemui"
                )
                val mobile_container_right_ID: Int = res.getIdentifier(
                    "mobile_container_right",
                    "id",
                    "com.android.systemui"
                )
                val mobile_type_ID: Int =
                    res.getIdentifier("mobile_type", "id", "com.android.systemui")
                val TextAppearance_StatusBar_Carrier_ID: Int = res.getIdentifier(
                    "TextAppearance.StatusBar.Carrier",
                    "style",
                    "com.android.systemui"
                )

                //获取mobile_container_left并在父布局中删除
                val mobile_container_left =
                    StatusBarMobileView.findViewById<ViewGroup>(mobile_container_left_ID)
                val mobile_container_left_fl =
                    mobile_container_left.layoutParams as LinearLayout.LayoutParams
                (mobile_container_left.parent as ViewGroup).removeView(mobile_container_left)
                //获取mobile_container_right的父布局，并在mobile_container_right前添加mobile_container_left
                val mobile_container_right =
                    StatusBarMobileView.findViewById<ViewGroup>(mobile_container_right_ID)
                val RightParentLayout = mobile_container_right.parent as ViewGroup
                val mobile_container_right_Index =
                    RightParentLayout.indexOfChild(mobile_container_left)
                RightParentLayout.addView(
                    mobile_container_left,
                    mobile_container_right_Index - 1
                )
                mobile_container_left_fl.topMargin = dp2px(context, 1.5f)
                mobile_container_left.layoutParams = mobile_container_left_fl
                val mobile_type = StatusBarMobileView.findViewById<TextView>(mobile_type_ID)
                mobile_type.setTextAppearance(TextAppearance_StatusBar_Carrier_ID)
                val mobile_type_fl = mobile_type.layoutParams as FrameLayout.LayoutParams
                mobile_type_fl.width = dp2px(context, 25f)
                mobile_type_fl.gravity = Gravity.CENTER or Gravity.START
                mobile_type.layoutParams = mobile_type_fl
                val mobile_left_mobile_inout =
                    StatusBarMobileView.findViewById<ImageView>(mobile_left_mobile_inout_ID)
                val mobile_left_mobile_inout_fl =
                    mobile_left_mobile_inout.layoutParams as FrameLayout.LayoutParams
                mobile_left_mobile_inout_fl.width = dp2px(context, 8f)
                mobile_left_mobile_inout_fl.marginStart = dp2px(context, 16f)
                mobile_left_mobile_inout_fl.bottomMargin = dp2px(context, 3f)
                mobile_left_mobile_inout.layoutParams = mobile_left_mobile_inout_fl
            }
        }
    }
}