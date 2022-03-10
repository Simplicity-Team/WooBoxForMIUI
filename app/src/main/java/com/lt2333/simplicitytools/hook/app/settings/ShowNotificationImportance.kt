package com.lt2333.simplicitytools.hook.app.settings

import android.app.NotificationChannel
import com.lt2333.simplicitytools.util.XSPUtils
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class ShowNotificationImportance: IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (!XSPUtils.getBoolean("show_notification_importance", false)) return

        val channelNotificationSettingsClass = XposedHelpers.findClass(
            "com.android.settings.notification.ChannelNotificationSettings",
            lpparam.classLoader
        )

        XposedHelpers.findAndHookMethod(channelNotificationSettingsClass, "removeDefaultPrefs",
            object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                val importance =
                    XposedHelpers.callMethod(param.thisObject, "findPreference", "importance")
                val mChannel =
                    XposedHelpers.getObjectField(param.thisObject, "mChannel") as NotificationChannel
                val index = XposedHelpers.callMethod(
                    importance,
                    "findSpinnerIndexOfValue",
                    mChannel.importance.toString()
                ) as Int
                if (index < 0) return@beforeHookedMethod

                XposedHelpers.callMethod(importance, "setValueIndex", index)

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

        XposedHelpers.findAndHookMethod(dropDownPreferenceClass, "callChangeListener",
            Any::class.java, object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                val channelNotificationSettings = XposedHelpers.getAdditionalInstanceField(
                    param.thisObject,
                    "channelNotificationSettings"
                )
                    ?: return@afterHookedMethod

                val mChannel = XposedHelpers.getObjectField(
                    channelNotificationSettings,
                    "mChannel"
                ) as NotificationChannel
                XposedHelpers.callMethod(mChannel, "setImportance", (param.args[0] as String).toInt())

                val mBackend = XposedHelpers.getObjectField(channelNotificationSettings, "mBackend")
                val mPkg =
                    XposedHelpers.getObjectField(channelNotificationSettings, "mPkg") as String
                val mUid = XposedHelpers.getObjectField(channelNotificationSettings, "mUid") as Int
                XposedHelpers.callMethod(mBackend, "updateChannel", mPkg, mUid, mChannel)
            }
        })
    }
}