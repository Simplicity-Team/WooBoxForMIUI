package com.lt2333.simplicitytools.hook.app.settings

import android.app.NotificationChannel
import com.lt2333.simplicitytools.util.*
import com.lt2333.simplicitytools.util.xposed.base.HookRegister
import de.robv.android.xposed.XposedHelpers

object ShowNotificationImportance : HookRegister() {

    override fun init() {
        val channelNotificationSettingsClass = "com.android.settings.notification.ChannelNotificationSettings".findClass(getDefaultClassLoader())
        channelNotificationSettingsClass.hookBeforeMethod("removeDefaultPrefs") {
            if (!XSPUtils.getBoolean("show_notification_importance", false)) return@hookBeforeMethod
            val importance = it.thisObject.callMethod("findPreference", "importance") ?: return@hookBeforeMethod
            val mChannel = it.thisObject.getObjectField("mChannel") as NotificationChannel
            val index = importance.callMethod("findSpinnerIndexOfValue", mChannel.importance.toString()) as Int
            if (index < 0) return@hookBeforeMethod
            importance.callMethod("setValueIndex", index)
            XposedHelpers.setAdditionalInstanceField(importance, "channelNotificationSettings", it.thisObject)
            it.result = null
        }

        val dropDownPreferenceClass = XposedHelpers.findClass("androidx.preference.Preference", getDefaultClassLoader())
        dropDownPreferenceClass.hookAfterMethod("callChangeListener", Any::class.java) {
            val channelNotificationSettings = XposedHelpers.getAdditionalInstanceField(it.thisObject, "channelNotificationSettings") ?: return@hookAfterMethod
            val mChannel = channelNotificationSettings.getObjectField("mChannel") as NotificationChannel
            mChannel.callMethod("setImportance", (it.args[0] as String).toInt())
            val mBackend = channelNotificationSettings.getObjectField("mBackend") ?: return@hookAfterMethod
            val mPkg = channelNotificationSettings.getObjectField("mPkg") as String
            val mUid = channelNotificationSettings.getIntField("mUid")
            mBackend.callMethod("updateChannel", mPkg, mUid, mChannel)
        }
    }

}