package com.lt2333.simplicitytools.hook.app.systemui

import android.content.Context
import android.content.res.Resources
import android.graphics.Typeface
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.lt2333.simplicitytools.util.XSPUtils
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookAfterMethod
import com.lt2333.simplicitytools.util.hookBeforeMethod
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage


class StatusBarBigMobileTypeIcon : IXposedHookLoadPackage {
    val upAndDownPosition = XSPUtils.getInt("big_mobile_type_icon_up_and_down_position", 0)
    val leftAndRightMargin = XSPUtils.getInt("big_mobile_type_icon_left_and_right_margins", 0)
    val isBold = XSPUtils.getBoolean("big_mobile_type_icon_bold", true)
    val size = XSPUtils.getFloat("big_mobile_type_icon_size", 12.5f)

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        hasEnable("big_mobile_type_icon") {
            "com.android.systemui.statusbar.StatusBarMobileView".hookAfterMethod(
                lpparam.classLoader,
                "init"
            ) {
                val StatusBarMobileView = it.thisObject as ViewGroup
                val context: Context = StatusBarMobileView.context
                val res: Resources = context.resources

                //获取组件
                val mobile_container_left_ID: Int =
                    res.getIdentifier("mobile_container_left", "id", "com.android.systemui")
                val mobile_container_left =
                    StatusBarMobileView.findViewById<ViewGroup>(mobile_container_left_ID)

                val mobile_type_ID: Int =
                    res.getIdentifier("mobile_type", "id", "com.android.systemui")
                val mobile_type = StatusBarMobileView.findViewById<TextView>(mobile_type_ID)

                val mobile_left_mobile_inout_ID: Int = res.getIdentifier(
                    "mobile_left_mobile_inout",
                    "id",
                    "com.android.systemui"
                )
                val mobile_left_mobile_inout =
                    StatusBarMobileView.findViewById<ImageView>(mobile_left_mobile_inout_ID)


                //获取插入位置
                val mobile_container_right_ID: Int = res.getIdentifier(
                    "mobile_container_right",
                    "id",
                    "com.android.systemui"
                )
                val mobile_container_right =
                    StatusBarMobileView.findViewById<ViewGroup>(mobile_container_right_ID)
                val RightParentLayout = mobile_container_right.parent as ViewGroup
                val mobile_container_right_Index =
                    RightParentLayout.indexOfChild(mobile_container_right)

                //创建新布局
                val newLinearLayoutLP = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                ).also {

                }
                val newLinearlayout = LinearLayout(context).also {
                    it.layoutParams = newLinearLayoutLP
                    it.id = mobile_container_left_ID
                    it.setPadding(leftAndRightMargin, 0, leftAndRightMargin, 0)
                }
                XposedHelpers.setObjectField(it.thisObject, "mMobileLeftContainer", newLinearlayout)
                RightParentLayout.addView(
                    newLinearlayout,
                    mobile_container_right_Index
                )

                //将组件插入新的布局
                (mobile_type.parent as ViewGroup).removeView(mobile_type)
                (mobile_left_mobile_inout.parent as ViewGroup).removeView(mobile_left_mobile_inout)
                (mobile_container_left.parent as ViewGroup).removeView(mobile_container_left)


                newLinearlayout.addView(mobile_type)    //类型
                val mobile_type_lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).also {
                    it.gravity = Gravity.CENTER_VERTICAL
                    it.topMargin = upAndDownPosition
                }
                mobile_type.also {
                    it.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size)
                    if (isBold) {
                        it.typeface = Typeface.DEFAULT_BOLD
                    }
                    it.layoutParams = mobile_type_lp
                }


                newLinearlayout.addView(mobile_left_mobile_inout)   //箭头
                val mobile_left_mobile_inout_lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
                mobile_left_mobile_inout.also {
                    it.layoutParams = mobile_left_mobile_inout_lp
                }

                //屏蔽更新布局
                "com.android.systemui.statusbar.StatusBarMobileView".hookBeforeMethod(
                    lpparam.classLoader,
                    "updateMobileTypeLayout", String::class.java
                ) {
                    it.result = null
                }
            }
        }
    }
}