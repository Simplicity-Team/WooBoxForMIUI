package com.lt2333.simplicitytools.hook.app.systemui

import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.content.Context
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import com.lt2333.simplicitytools.util.findClass
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookBeforeMethod
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class LockScreenDoubleTapToSleep : IXposedHookLoadPackage {
    @SuppressLint("ClickableViewAccessibility")
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        hasEnable("lock_screen_double_tap_to_sleep") {
            "com.android.systemui.statusbar.phone.NotificationsQuickSettingsContainer".findClass(
                lpparam.classLoader
            ).hookBeforeMethod("onFinishInflate") {
                val view = it.thisObject as View
                XposedHelpers.setAdditionalInstanceField(view, "currentTouchTime", 0L)
                XposedHelpers.setAdditionalInstanceField(view, "currentTouchX", 0f)
                XposedHelpers.setAdditionalInstanceField(view, "currentTouchY", 0f)
                view.setOnTouchListener(OnTouchListener { v, event ->
                    if (event.action != MotionEvent.ACTION_DOWN) return@OnTouchListener false
                    var currentTouchTime =
                        XposedHelpers.getAdditionalInstanceField(view, "currentTouchTime") as Long
                    var currentTouchX =
                        XposedHelpers.getAdditionalInstanceField(view, "currentTouchX") as Float
                    var currentTouchY =
                        XposedHelpers.getAdditionalInstanceField(view, "currentTouchY") as Float
                    val lastTouchTime = currentTouchTime
                    val lastTouchX = currentTouchX
                    val lastTouchY = currentTouchY
                    currentTouchTime = System.currentTimeMillis()
                    currentTouchX = event.x
                    currentTouchY = event.y
                    if (currentTouchTime - lastTouchTime < 250L && Math.abs(currentTouchX - lastTouchX) < 100f && Math.abs(
                            currentTouchY - lastTouchY
                        ) < 100f
                    ) {
                        val keyguardMgr =
                            v.context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
                        if (keyguardMgr.isKeyguardLocked) {
                            XposedHelpers.callMethod(
                                v.context.getSystemService(Context.POWER_SERVICE),
                                "goToSleep",
                                SystemClock.uptimeMillis()
                            )
                        }
                        currentTouchTime = 0L
                        currentTouchX = 0f
                        currentTouchY = 0f
                    }
                    XposedHelpers.setAdditionalInstanceField(
                        view,
                        "currentTouchTime",
                        currentTouchTime
                    )
                    XposedHelpers.setAdditionalInstanceField(view, "currentTouchX", currentTouchX)
                    XposedHelpers.setAdditionalInstanceField(view, "currentTouchY", currentTouchY)
                    false
                })
            }
        }
    }
}