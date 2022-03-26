package com.lt2333.simplicitytools.hook.app.miuihome

import android.content.Context
import android.content.Intent
import android.view.MotionEvent
import com.lt2333.simplicitytools.util.*
import com.lt2333.simplicitytools.util.xposed.base.HookRegister
import com.yuk.miuihome.module.DoubleTapController
import de.robv.android.xposed.XposedHelpers

object DoubleTapToSleep : HookRegister() {

    override fun init() {
        hasEnable("double_tap_to_sleep") {
            "com.miui.home.launcher.Workspace".findClass(getDefaultClassLoader())
                .hookAfterAllConstructors {
                    var mDoubleTapControllerEx =
                        XposedHelpers.getAdditionalInstanceField(
                            it.thisObject,
                            "mDoubleTapControllerEx"
                        )
                    if (mDoubleTapControllerEx != null) return@hookAfterAllConstructors
                    mDoubleTapControllerEx = DoubleTapController((it.args[0] as Context))
                    XposedHelpers.setAdditionalInstanceField(
                        it.thisObject,
                        "mDoubleTapControllerEx",
                        mDoubleTapControllerEx
                    )
                }

            "com.miui.home.launcher.Workspace".findClass(getDefaultClassLoader()).hookBeforeMethod(
                "dispatchTouchEvent", MotionEvent::class.java
            ) {
                val mDoubleTapControllerEx = XposedHelpers.getAdditionalInstanceField(
                    it.thisObject,
                    "mDoubleTapControllerEx"
                ) as DoubleTapController
                if (!mDoubleTapControllerEx.isDoubleTapEvent(it.args[0] as MotionEvent)) return@hookBeforeMethod
                val mCurrentScreenIndex = it.thisObject.getIntField("mCurrentScreenIndex")
                val cellLayout = it.thisObject.callMethod("getCellLayout", mCurrentScreenIndex)
                if (cellLayout != null) if (cellLayout.callMethod("lastDownOnOccupiedCell") as Boolean) return@hookBeforeMethod
                if (it.thisObject.callMethod("isInNormalEditingMode") as Boolean) return@hookBeforeMethod
                val context = it.thisObject.callMethod("getContext") as Context
                context.sendBroadcast(
                    Intent("com.miui.app.ExtraStatusBarManager.action_TRIGGER_TOGGLE").putExtra(
                        "com.miui.app.ExtraStatusBarManager.extra_TOGGLE_ID",
                        10
                    )
                )
            }
        }
    }

}