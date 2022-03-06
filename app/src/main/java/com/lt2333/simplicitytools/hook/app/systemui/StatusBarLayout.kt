package com.lt2333.simplicitytools.hook.app.systemui

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class StatusBarLayout : IXposedHookLoadPackage {

    private var mLeftLayout: LinearLayout? = null

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val classIfExists = XposedHelpers.findClassIfExists(
            "com.android.systemui.statusbar.phone.CollapsedStatusBarFragment",
            lpparam.classLoader
        )

        XposedHelpers.findAndHookMethod(
            classIfExists,
            "onViewCreated",
            View::class.java,
            Bundle::class.java,
            object : XC_MethodHook() {
                @Throws(Throwable::class)
                override fun afterHookedMethod(param: MethodHookParam) {
                    super.afterHookedMethod(param)
                    val MiuiPhoneStatusBarView: ViewGroup =
                        XposedHelpers.getObjectField(param.thisObject, "mStatusBar") as ViewGroup
                    val context: Context = MiuiPhoneStatusBarView.context
                    val res: Resources = MiuiPhoneStatusBarView.resources
                    val status_bar_ID: Int =
                        res.getIdentifier("status_bar", "id", "com.android.systemui")
                    val status_bar_contents_ID: Int =
                        res.getIdentifier("status_bar_contents", "id", "com.android.systemui")
                    val system_icon_area_ID: Int =
                        res.getIdentifier("system_icon_area", "id", "com.android.systemui")

                    val clock_ID: Int = res.getIdentifier("clock", "id", "com.android.systemui")
                    val phone_status_bar_left_container_ID: Int = res.getIdentifier(
                        "phone_status_bar_left_container",
                        "id",
                        "com.android.systemui"
                    )
                    val fullscreen_notification_icon_area_ID: Int = res.getIdentifier(
                        "fullscreen_notification_icon_area",
                        "id",
                        "com.android.systemui"
                    )
                    val status_bar: ViewGroup = MiuiPhoneStatusBarView.findViewById(status_bar_ID)
                    val status_bar_contents: ViewGroup =
                        MiuiPhoneStatusBarView.findViewById(status_bar_contents_ID)
                    if (status_bar == null) return
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
//                    (fullscreen_notification_icon_area.parent as ViewGroup).removeView(
//                        fullscreen_notification_icon_area
//                    )
                    (system_icon_area.parent as ViewGroup).removeView(system_icon_area)

                    //增加一个左对齐布局
                    mLeftLayout = LinearLayout(context)
                    val LeftLp: LinearLayout.LayoutParams =
                        LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f)
                    mLeftLayout!!.layoutParams = LeftLp
                    mLeftLayout!!.gravity = Gravity.START or Gravity.CENTER_VERTICAL

                    //增加一个居中布局
                    val mCenterLayout = LinearLayout(context)
                    val CenterLp: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                    )
                    mCenterLayout.layoutParams = CenterLp
                    mCenterLayout.gravity = Gravity.CENTER or Gravity.CENTER_VERTICAL
                    val mRightLayout = LinearLayout(context)
                    val RightLp: LinearLayout.LayoutParams =
                        LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f)
                    mRightLayout.layoutParams = RightLp
                    mRightLayout.gravity = Gravity.END or Gravity.CENTER_VERTICAL
                    mLeftLayout!!.addView(phone_status_bar_left_container)
//                    mLeftLayout!!.addView(fullscreen_notification_icon_area)

                    mCenterLayout.addView(Clock)
                    mRightLayout.addView(system_icon_area)
                    status_bar_contents.addView(mLeftLayout, 0)
                    status_bar_contents.addView(mCenterLayout)
                    status_bar_contents.addView(mRightLayout)
                }
            })
//
//        val classIfExists2 = XposedHelpers.findClassIfExists(
//            "com.android.systemui.statusbar.phone.MiuiCollapsedStatusBarFragment",
//            lpparam.classLoader
//        )
//
//        XposedHelpers.findAndHookMethod(
//            classIfExists2,
//            "showClock",
//            Boolean::class.javaPrimitiveType,
//            object : XC_MethodHook() {
//                @Throws(Throwable::class)
//                override fun afterHookedMethod(param: MethodHookParam) {
//                    val MiuiPhoneStatusBarView: ViewGroup =
//                        XposedHelpers.getObjectField(param.thisObject, "mStatusBar") as ViewGroup
//                    val context: Context = MiuiPhoneStatusBarView.context
//                    val res: Resources = MiuiPhoneStatusBarView.resources
//                    val mConfiguration: Configuration = res.configuration //获取设置的配置信息
//                    val status_bar_ID: Int =
//                        res.getIdentifier("status_bar", "id", "com.android.systemui")
//                    val status_bar: ViewGroup = MiuiPhoneStatusBarView.findViewById(status_bar_ID)
//                    //非锁屏下整个状态栏布局
//                    val keyguardMgr: KeyguardManager = status_bar.context
//                        .getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
//                    if (keyguardMgr.isKeyguardLocked) {
//                        mLeftLayout!!.visibility = View.GONE
//                    } else {
//                        mLeftLayout!!.visibility = View.VISIBLE
//                    }
//                }
//            })

    }
}