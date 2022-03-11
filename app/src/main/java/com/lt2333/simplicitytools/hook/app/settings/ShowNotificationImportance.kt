package com.lt2333.simplicitytools.hook.app.settings

import android.app.NotificationChannel
import com.lt2333.simplicitytools.util.*
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class ShowNotificationImportance : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val channelNotificationSettingsClass = XposedHelpers.findClass(
            "com.android.settings.notification.ChannelNotificationSettings",
            lpparam.classLoader
        )

        channelNotificationSettingsClass.hookMethod("removeDefaultPrefs",
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    if (!XSPUtils.getBoolean("show_notification_importance", false)) return

                    val importance =
                        param.thisObject.callMethod("findPreference", "importance") ?: return
                    val mChannel =
                        param.thisObject.getObjectField("mChannel") as NotificationChannel
                    val index = importance.callMethod(
                        "findSpinnerIndexOfValue",
                        mChannel.importance.toString()
                    ) as Int
                    if (index < 0) return

                    importance.callMethod("setValueIndex", index)

                    XposedHelpers.setAdditionalInstanceField(
                        importance,
                        "channelNotificationSettings",
                        param.thisObject
                    )

                    param.result = null
                }
            })

        val dropDownPreferenceClass = XposedHelpers.findClass(
            "androidx.preference.Preference",
            lpparam.classLoader
        )

        dropDownPreferenceClass.hookMethod("callChangeListener",
            Any::class.java, object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val channelNotificationSettings = XposedHelpers.getAdditionalInstanceField(
                        param.thisObject,
                        "channelNotificationSettings"
                    ) ?: return@afterHookedMethod

                    val mChannel = XposedHelpers.getObjectField(
                        channelNotificationSettings,
                        "mChannel"
                    ) as NotificationChannel

                    mChannel.callMethod(
                        "setImportance", (param.args[0] as String).toInt()
                    )

                    val mBackend = channelNotificationSettings.getObjectField("mBackend") ?: return
                    val mPkg = channelNotificationSettings.getObjectField("mPkg") as String
                    val mUid = channelNotificationSettings.getIntField("mUid")
                    mBackend.callMethod("updateChannel", mPkg, mUid, mChannel)
                }
            })
    }
}