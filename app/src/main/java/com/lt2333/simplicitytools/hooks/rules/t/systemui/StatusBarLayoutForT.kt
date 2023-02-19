package com.lt2333.simplicitytools.hooks.rules.t.systemui

import android.app.KeyguardManager
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import cn.fkj233.ui.activity.dp2px
import com.github.kyuubiran.ezxhelper.utils.*
import com.lt2333.simplicitytools.utils.XSPUtils
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister


object StatusBarLayoutForT : HookRegister() {

    private val getMode = XSPUtils.getInt("status_bar_layout_mode", 0)
    private val isCompatibilityMode = XSPUtils.getBoolean("layout_compatibility_mode", false)

    private var statusBarLeft = 0
    private var statusBarTop = 0
    private var statusBarRight = 0
    private var statusBarBottom = 0

    override fun init() {
        var mLeftLayout: LinearLayout? = null
        var mRightLayout: LinearLayout? = null
        var mCenterLayout: LinearLayout?
        var statusBar: ViewGroup? = null

        //判断屏幕状态更新布局 mode: 1正常布局 2居中布局
        fun updateLayout(context: Context,mode: Int) {
            when(mode){
                1->{
                    val mConfiguration: Configuration = context.resources.configuration
                    if (mConfiguration.orientation == Configuration.ORIENTATION_PORTRAIT) { //横屏
                        statusBar!!.setPadding(statusBarLeft, statusBarTop, statusBarRight, statusBarBottom)
                    }
                }
                2->{
                    val mConfiguration: Configuration = context.resources.configuration
                    if (mConfiguration.orientation == Configuration.ORIENTATION_PORTRAIT) { //横屏
                        mLeftLayout!!.setPadding(statusBarLeft, 0, 0, 0)
                        mRightLayout!!.setPadding(0, 0, statusBarRight, 0)
                        statusBar!!.setPadding(0, statusBarTop, 0, statusBarBottom)
                    }else{ //竖屏
                        mLeftLayout!!.setPadding(0, 0, 0, 0)
                        mRightLayout!!.setPadding(0, 0, 0, 0)
                    }
                }
            }
        }

        //判断是否开启挖孔兼容模式
        if (isCompatibilityMode) {
            findMethod("com.android.systemui.ScreenDecorations") {
                name == "boundsFromDirection" && parameterCount == 3 && isStatic
            }.hookBefore {
                it.args[1] = 0
            }
        }

        //修改对应布局
        when (getMode) {
            //默认
            0 -> {

                findMethod("com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragment") {
                    name == "onViewCreated" && parameterCount == 2
                }.hookAfter { param ->
                    val miuiPhoneStatusBarView = param.thisObject.getObjectAs<ViewGroup>("mStatusBar")
                    val context: Context = miuiPhoneStatusBarView.context
                    val res: Resources = miuiPhoneStatusBarView.resources
                    val statusBarId: Int = res.getIdentifier("status_bar", "id", "com.android.systemui")
                    statusBar = miuiPhoneStatusBarView.findViewById(statusBarId)
                    if (statusBar == null) return@hookAfter

                    statusBarLeft = statusBar!!.paddingLeft
                    statusBarTop = statusBar!!.paddingTop
                    statusBarRight = statusBar!!.paddingRight
                    statusBarBottom = statusBar!!.paddingBottom


                    if (isCompatibilityMode) {
                        val customLeftMargin = XSPUtils.getInt("status_bar_left_margin", 0)
                        if (customLeftMargin != 0) {
                            statusBarLeft = customLeftMargin
                        }

                        val customRightMargin = XSPUtils.getInt("status_bar_right_margin", 0)
                        if (customRightMargin != 0) {
                            statusBarRight = customRightMargin
                        }
                        updateLayout(context,1)
                    }
                }


                //兼容模式
                findMethod("com.android.systemui.statusbar.phone.PhoneStatusBarView") {
                    name == "updateLayoutForCutout"
                }.hookAfter {
                    if (isCompatibilityMode) {
                        val context = (it.thisObject as ViewGroup).context
                        updateLayout(context,1)
                    }
                }
            }
            //时钟居中
            1 -> {
                findMethod("com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragment") {
                    name == "onViewCreated" && parameterCount == 2
                }.hookAfter { param ->
                    val miuiPhoneStatusBarView = param.thisObject.getObjectAs<ViewGroup>("mStatusBar")
                    val context: Context = miuiPhoneStatusBarView.context
                    val res: Resources = miuiPhoneStatusBarView.resources
                    val statusBarId: Int = res.getIdentifier("status_bar", "id", "com.android.systemui")
                    val statusBarContentsId: Int = res.getIdentifier(
                        "status_bar_contents", "id", "com.android.systemui"
                    )
                    val systemIconAreaId: Int = res.getIdentifier("system_icon_area", "id", "com.android.systemui")
                    val clockId: Int = res.getIdentifier("clock", "id", "com.android.systemui")
                    val phoneStatusBarLeftContainerId: Int = res.getIdentifier(
                        "phone_status_bar_left_container", "id", "com.android.systemui"
                    )
                    val notificationIconAreaInnerId: Int = res.getIdentifier(
                        "notification_icon_area_inner", "id", "com.android.systemui"
                    )
                    statusBar = miuiPhoneStatusBarView.findViewById(statusBarId)
                    val statusBarContents: ViewGroup = miuiPhoneStatusBarView.findViewById(statusBarContentsId)
                    if (statusBar == null) return@hookAfter
                    val clock: TextView = miuiPhoneStatusBarView.findViewById(clockId)
                    val phoneStatusBarLeftContainer: ViewGroup = miuiPhoneStatusBarView.findViewById(
                        phoneStatusBarLeftContainerId
                    )
                    val notificationIconAreaInner: ViewGroup = miuiPhoneStatusBarView.findViewById(
                        notificationIconAreaInnerId
                    )
                    val systemIconArea: ViewGroup = miuiPhoneStatusBarView.findViewById(systemIconAreaId)

                    (clock.parent as ViewGroup).removeView(clock)
                    (phoneStatusBarLeftContainer.parent as ViewGroup).removeView(
                        phoneStatusBarLeftContainer
                    )
                    (notificationIconAreaInner.parent as ViewGroup).removeView(
                        notificationIconAreaInner
                    )
                    (systemIconArea.parent as ViewGroup).removeView(systemIconArea)

                    val mConstraintLayout = ConstraintLayout(context).also {
                        it.layoutParams = ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT
                        )
                    }

                    mConstraintLayout.addView(notificationIconAreaInner)

                    val fullscreenNotificationIconAreaLp = LinearLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT
                    )

                    notificationIconAreaInner.layoutParams = fullscreenNotificationIconAreaLp

                    //增加一个左对齐布局
                    mLeftLayout = LinearLayout(context)
                    val leftLp: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f
                    )
                    mLeftLayout!!.layoutParams = leftLp
                    mLeftLayout!!.gravity = Gravity.START or Gravity.CENTER_VERTICAL

                    //增加一个居中布局
                    mCenterLayout = LinearLayout(context)
                    val centerLp: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT
                    )
                    mCenterLayout!!.layoutParams = centerLp
                    mCenterLayout!!.gravity = Gravity.CENTER or Gravity.CENTER_VERTICAL
                    mRightLayout = LinearLayout(context)
                    val rightLp: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f
                    )
                    mRightLayout!!.layoutParams = rightLp
                    mRightLayout!!.gravity = Gravity.END or Gravity.CENTER_VERTICAL
                    mLeftLayout!!.addView(phoneStatusBarLeftContainer)
                    mLeftLayout!!.addView(mConstraintLayout)

                    mCenterLayout!!.addView(clock)
                    mRightLayout!!.addView(systemIconArea)
                    statusBarContents.addView(mLeftLayout, 0)
                    statusBarContents.addView(mCenterLayout)
                    statusBarContents.addView(mRightLayout)

                    statusBarLeft = statusBar!!.paddingLeft
                    statusBarTop = statusBar!!.paddingTop
                    statusBarRight = statusBar!!.paddingRight
                    statusBarBottom = statusBar!!.paddingBottom


                    if (isCompatibilityMode) {
                        val customLeftMargin = XSPUtils.getInt("status_bar_left_margin", 0)
                        if (customLeftMargin != 0) {
                            statusBarLeft = customLeftMargin
                        }

                        val customRightMargin = XSPUtils.getInt("status_bar_right_margin", 0)
                        if (customRightMargin != 0) {
                            statusBarRight = customRightMargin
                        }
                        updateLayout(context,2)
                    }
                }

                findMethod("com.android.systemui.statusbar.phone.PhoneStatusBarView") {
                    name == "updateLayoutForCutout"
                }.hookAfter {
                    hasEnable("layout_compatibility_mode") {
                        val context = (it.thisObject as ViewGroup).context
                        updateLayout(context,2)
                    }
                }
            }
            //时钟居右
            2 -> {
                findMethod("com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragment") {
                    name == "onViewCreated" && parameterCount == 2
                }.hookAfter { param ->
                    val miuiPhoneStatusBarView = param.thisObject.getObjectAs<ViewGroup>("mStatusBar")
                    val context: Context = miuiPhoneStatusBarView.context
                    val res: Resources = miuiPhoneStatusBarView.resources

                    //组件ID
                    val statusBarId: Int = res.getIdentifier("status_bar", "id", "com.android.systemui")
                    val clockId: Int = res.getIdentifier("clock", "id", "com.android.systemui")
                    val batteryId: Int = res.getIdentifier("battery", "id", "com.android.systemui")

                    //查找组件
                    statusBar = miuiPhoneStatusBarView.findViewById(statusBarId)
                    if (statusBar == null) return@hookAfter
                    val clock: TextView = miuiPhoneStatusBarView.findViewById(clockId)
                    val battery: ViewGroup = miuiPhoneStatusBarView.findViewById(batteryId)

                    //新建布局
                    val rightLp = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT
                    ).also {
                        it.marginStart = dp2px(context, 5f)
                    }
                    mRightLayout = LinearLayout(context).also {
                        it.layoutParams = rightLp
                    }

                    //添加布局与组件
                    battery.addView(mRightLayout)
                    (clock.parent as ViewGroup).removeView(clock)
                    mRightLayout!!.addView(clock)


                    statusBarLeft = statusBar!!.paddingLeft
                    statusBarTop = statusBar!!.paddingTop
                    statusBarRight = statusBar!!.paddingRight
                    statusBarBottom = statusBar!!.paddingBottom


                    if (isCompatibilityMode) {
                        val customLeftMargin = XSPUtils.getInt("status_bar_left_margin", 0)
                        if (customLeftMargin != 0) {
                            statusBarLeft = customLeftMargin
                        }

                        val customRightMargin = XSPUtils.getInt("status_bar_right_margin", 0)
                        if (customRightMargin != 0) {
                            statusBarRight = customRightMargin
                        }
                        updateLayout(context,1)
                    }
                }

                //兼容模式
                findMethod("com.android.systemui.statusbar.phone.PhoneStatusBarView") {
                    name == "updateLayoutForCutout"
                }.hookAfter {
                    if (isCompatibilityMode) {
                        val context = (it.thisObject as ViewGroup).context
                        updateLayout(context,1)
                    }
                }
            }
            //时钟居中+图标居左
            3 -> {
                findMethod("com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragment") {
                    name == "onViewCreated" && parameterCount == 2
                }.hookAfter { param ->
                    val miuiPhoneStatusBarView = param.thisObject.getObjectAs<ViewGroup>("mStatusBar")
                    val context: Context = miuiPhoneStatusBarView.context
                    val res: Resources = miuiPhoneStatusBarView.resources
                    val statusBarId: Int = res.getIdentifier("status_bar", "id", "com.android.systemui")
                    val statusBarContentsId: Int = res.getIdentifier(
                        "status_bar_contents", "id", "com.android.systemui"
                    )
                    val systemIconAreaId: Int = res.getIdentifier("system_icon_area", "id", "com.android.systemui")
                    val clockId: Int = res.getIdentifier("clock", "id", "com.android.systemui")
                    val phoneStatusBarLeftContainerId: Int = res.getIdentifier(
                        "phone_status_bar_left_container", "id", "com.android.systemui"
                    )
                    val fullscreenNotificationIconAreaId: Int = res.getIdentifier(
                        "fullscreen_notification_icon_area", "id", "com.android.systemui"
                    )
                    val statusIconsId: Int = res.getIdentifier(
                        "statusIcons", "id", "com.android.systemui"
                    )
                    val systemIconsId: Int = res.getIdentifier(
                        "system_icons", "id", "com.android.systemui"
                    )
                    val batteryId: Int = res.getIdentifier(
                        "battery", "id", "com.android.systemui"
                    )

                    statusBar = miuiPhoneStatusBarView.findViewById(statusBarId)
                    val statusBarContents: ViewGroup = miuiPhoneStatusBarView.findViewById(statusBarContentsId)
                    if (statusBar == null) return@hookAfter
                    val clock: TextView = miuiPhoneStatusBarView.findViewById(clockId)
                    val phoneStatusBarLeftContainer: ViewGroup = miuiPhoneStatusBarView.findViewById(
                        phoneStatusBarLeftContainerId
                    )
                    val fullscreenNotificationIconArea: ViewGroup = miuiPhoneStatusBarView.findViewById(
                        fullscreenNotificationIconAreaId
                    )
                    val systemIconArea: ViewGroup = miuiPhoneStatusBarView.findViewById(systemIconAreaId)
                    val statusIcons: ViewGroup = miuiPhoneStatusBarView.findViewById(statusIconsId)
                    val systemIcons: ViewGroup = miuiPhoneStatusBarView.findViewById(systemIconsId)
                    val battery: ViewGroup = miuiPhoneStatusBarView.findViewById(batteryId)

                    (clock.parent as ViewGroup).removeView(clock)
                    (phoneStatusBarLeftContainer.parent as ViewGroup).removeView(
                        phoneStatusBarLeftContainer
                    )
                    (systemIconArea.parent as ViewGroup).removeView(systemIconArea)
                    (statusIcons.parent as ViewGroup).removeView(statusIcons)
                    (systemIcons.parent as ViewGroup).removeView(systemIcons)
                    (battery.parent as ViewGroup).removeView(battery)
                    (fullscreenNotificationIconArea.parent as ViewGroup).removeView(
                        fullscreenNotificationIconArea
                    )

                    val mConstraintLayout = ConstraintLayout(context).also {
                        it.layoutParams = ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT
                        )
                    }

                    mConstraintLayout.addView(fullscreenNotificationIconArea)
                    mConstraintLayout.addView(battery)


                    battery.layoutParams = ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.MATCH_PARENT
                    ).also {
                        it.endToEnd = 0
                    }


                    fullscreenNotificationIconArea.layoutParams = ConstraintLayout.LayoutParams(
                        0, ConstraintLayout.LayoutParams.MATCH_PARENT
                    ).also {
                        it.startToEnd = batteryId
                        it.endToEnd = 0
                    }
                    fullscreenNotificationIconArea.layoutDirection = View.LAYOUT_DIRECTION_RTL


                    //增加一个左对齐布局
                    mLeftLayout = LinearLayout(context)
                    val leftLp: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f
                    )
                    mLeftLayout!!.layoutParams = leftLp
                    mLeftLayout!!.gravity = Gravity.START or Gravity.CENTER_VERTICAL

                    //增加一个居中布局
                    mCenterLayout = LinearLayout(context)
                    val centerLp: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT
                    )
                    mCenterLayout!!.layoutParams = centerLp
                    mCenterLayout!!.gravity = Gravity.CENTER or Gravity.CENTER_VERTICAL

                    //增加一个右布局
                    mRightLayout = LinearLayout(context)
                    val rightLp: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f
                    )
                    mRightLayout!!.layoutParams = rightLp
                    mRightLayout!!.gravity = Gravity.END or Gravity.CENTER_VERTICAL


                    mLeftLayout!!.addView(phoneStatusBarLeftContainer)
                    mLeftLayout!!.addView(statusIcons)
                    statusIcons.layoutDirection = View.LAYOUT_DIRECTION_RTL

                    mCenterLayout!!.addView(clock)

                    mRightLayout!!.addView(mConstraintLayout)
                    fullscreenNotificationIconArea.layoutDirection = View.LAYOUT_DIRECTION_RTL


                    statusBarContents.addView(mLeftLayout, 0)
                    statusBarContents.addView(mCenterLayout)
                    statusBarContents.addView(mRightLayout)



                    statusBarLeft = statusBar!!.paddingLeft
                    statusBarTop = statusBar!!.paddingTop
                    statusBarRight = statusBar!!.paddingRight
                    statusBarBottom = statusBar!!.paddingBottom


                    if (isCompatibilityMode) {
                        val customLeftMargin = XSPUtils.getInt("status_bar_left_margin", 0)
                        if (customLeftMargin != 0) {
                            statusBarLeft = customLeftMargin
                        }

                        val customRightMargin = XSPUtils.getInt("status_bar_right_margin", 0)
                        if (customRightMargin != 0) {
                            statusBarRight = customRightMargin
                        }
                        updateLayout(context,2)
                    }
                }
                //兼容模式
                findMethod("com.android.systemui.statusbar.phone.PhoneStatusBarView") {
                    name == "updateLayoutForCutout"
                }.hookAfter {
                    if (isCompatibilityMode) {
                        val context = (it.thisObject as ViewGroup).context
                        updateLayout(context,2)
                    }
                }

                //解决重叠
                findMethod("com.android.systemui.statusbar.phone.MiuiCollapsedStatusBarFragment") {
                    name == "showClock" && parameterTypes[0] == Boolean::class.java
                }.hookAfter {
                    val miuiPhoneStatusBarView = it.thisObject.getObjectAs<ViewGroup>("mStatusBar")
                    val res = miuiPhoneStatusBarView.resources
                    val statusBarId = res.getIdentifier("status_bar", "id", "com.android.systemui")
                    val statusBar1 = miuiPhoneStatusBarView.findViewById<ViewGroup>(statusBarId)
                    //非锁屏下整个状态栏布局
                    val keyguardMgr = statusBar1.context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
                    if (keyguardMgr.isKeyguardLocked) {
                        statusBar1!!.visibility = View.GONE
                    } else {
                        statusBar1!!.visibility = View.VISIBLE
                    }
                }


                //TODO: 修改图标的顺序
                /*findConstructor("com.android.systemui.statusbar.phone.StatusBarIconList") {
                    parameterCount == 1
                }.hookBefore {

                }*/
            }
        }
    }
}