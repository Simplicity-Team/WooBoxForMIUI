package com.lt2333.simplicitytools.hook.app.systemui

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.lt2333.simplicitytools.util.XSPUtils
import com.lt2333.simplicitytools.util.findClass
import com.lt2333.simplicitytools.util.getObjectField
import com.lt2333.simplicitytools.util.hookAfterMethod
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

class StatusBarLayout : IXposedHookLoadPackage {

    private var mLeftLayout: LinearLayout? = null
    private var mRightLayout: LinearLayout? = null
    private var mCenterLayout: LinearLayout? = null
    private var status_bar: ViewGroup? = null

    private var status_bar_left = 0
    private var status_bar_top = 0
    private var status_bar_right = 0
    private var status_bar_bottom = 0

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (!XSPUtils.getBoolean("status_bar_time_center", false)) return

        val collapsedStatusBarFragmentClass =
            "com.android.systemui.statusbar.phone.CollapsedStatusBarFragment".findClass(lpparam.classLoader)

        collapsedStatusBarFragmentClass.hookAfterMethod(
            "onViewCreated",
            View::class.java,
            Bundle::class.java
        ) { param ->
            val MiuiPhoneStatusBarView: ViewGroup =
                param.thisObject.getObjectField("mStatusBar") as ViewGroup
            val context: Context = MiuiPhoneStatusBarView.context
            val res: Resources = MiuiPhoneStatusBarView.resources
            val status_bar_ID: Int = res.getIdentifier("status_bar", "id", "com.android.systemui")
            val status_bar_contents_ID: Int =
                res.getIdentifier("status_bar_contents", "id", "com.android.systemui")
            val system_icon_area_ID: Int =
                res.getIdentifier("system_icon_area", "id", "com.android.systemui")
            val clock_ID: Int = res.getIdentifier("clock", "id", "com.android.systemui")
            val phone_status_bar_left_container_ID: Int =
                res.getIdentifier("phone_status_bar_left_container", "id", "com.android.systemui")
            val fullscreen_notification_icon_area_ID: Int =
                res.getIdentifier("fullscreen_notification_icon_area", "id", "com.android.systemui")
            status_bar = MiuiPhoneStatusBarView.findViewById(status_bar_ID)
            val status_bar_contents: ViewGroup =
                MiuiPhoneStatusBarView.findViewById(status_bar_contents_ID)
            if (status_bar == null) return@hookAfterMethod
            val Clock: View = MiuiPhoneStatusBarView.findViewById(clock_ID)
            val phone_status_bar_left_container: ViewGroup =
                MiuiPhoneStatusBarView.findViewById(phone_status_bar_left_container_ID)
            val fullscreen_notification_icon_area: ViewGroup =
                MiuiPhoneStatusBarView.findViewById(fullscreen_notification_icon_area_ID)
            val system_icon_area: ViewGroup =
                MiuiPhoneStatusBarView.findViewById(system_icon_area_ID)

            (Clock.parent as ViewGroup).removeView(Clock)
            (phone_status_bar_left_container.parent as ViewGroup).removeView(
                phone_status_bar_left_container
            )
            (fullscreen_notification_icon_area.parent as ViewGroup).removeView(
                fullscreen_notification_icon_area
            )
            (system_icon_area.parent as ViewGroup).removeView(system_icon_area)

            val mConstraintLayoutLp = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )

            val mConstraintLayout =
                ConstraintLayout(context).also { it.layoutParams = mConstraintLayoutLp }

            mConstraintLayout.addView(fullscreen_notification_icon_area)

            val fullscreen_notification_icon_area_lp = FrameLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )

            fullscreen_notification_icon_area.layoutParams = fullscreen_notification_icon_area_lp

            //增加一个左对齐布局
            mLeftLayout = LinearLayout(context)
            val LeftLp: LinearLayout.LayoutParams =
                LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f)
            mLeftLayout!!.layoutParams = LeftLp
            mLeftLayout!!.gravity = Gravity.START or Gravity.CENTER_VERTICAL

            //增加一个居中布局
            mCenterLayout = LinearLayout(context)
            val CenterLp: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            mCenterLayout!!.layoutParams = CenterLp
            mCenterLayout!!.gravity = Gravity.CENTER or Gravity.CENTER_VERTICAL
            mRightLayout = LinearLayout(context)
            val RightLp: LinearLayout.LayoutParams =
                LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f)
            mRightLayout!!.layoutParams = RightLp
            mRightLayout!!.gravity = Gravity.END or Gravity.CENTER_VERTICAL
            mLeftLayout!!.addView(phone_status_bar_left_container)
            mLeftLayout!!.addView(mConstraintLayout)

            mCenterLayout!!.addView(Clock)
            mRightLayout!!.addView(system_icon_area)
            status_bar_contents.addView(mLeftLayout, 0)
            status_bar_contents.addView(mCenterLayout)
            status_bar_contents.addView(mRightLayout)

            status_bar_left = status_bar!!.paddingLeft
            status_bar_top = status_bar!!.paddingTop
            status_bar_right = status_bar!!.paddingRight
            status_bar_bottom = status_bar!!.paddingBottom


            if (XSPUtils.getBoolean("layout_compatibility_mode", false)) {
                val custom_left_margin = XSPUtils.getInt("status_bar_left_margin", 0)
                if (custom_left_margin != 0) {
                    status_bar_left = custom_left_margin
                }

                val custom_right_margin = XSPUtils.getInt("status_bar_right_margin", 0)
                if (custom_right_margin != 0) {
                    status_bar_right = custom_right_margin
                }
                updateLayout(context)
            }

        }

        val phoneStatusBarViewClass =
            "com.android.systemui.statusbar.phone.PhoneStatusBarView".findClass(lpparam.classLoader)

        phoneStatusBarViewClass.hookAfterMethod("updateLayoutForCutout") {
            if (XSPUtils.getBoolean("layout_compatibility_mode", false)) {
                val context = (it.thisObject as ViewGroup).context
                updateLayout(context)
            }
        }
    }


    fun updateLayout(context: Context) {
        //判断屏幕方向
        val mConfiguration: Configuration = context.resources.configuration
        if (mConfiguration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mLeftLayout!!.setPadding(status_bar_left, 0, 0, 0)
            mRightLayout!!.setPadding(0, 0, status_bar_right, 0)
            status_bar!!.setPadding(0, status_bar_top, 0, status_bar_bottom)
        } else {
            //横屏状态
            mLeftLayout!!.setPadding(175, 0, 0, 0)
            mRightLayout!!.setPadding(0, 0, 175, 0)
            status_bar!!.setPadding(0, status_bar_top, 0, status_bar_bottom)
        }
    }

}