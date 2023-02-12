package com.lt2333.simplicitytools.hooks.rules.all.settings

import android.app.NotificationChannel
import com.github.kyuubiran.ezxhelper.utils.*
import com.lt2333.simplicitytools.utils.XSPUtils
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister
import de.robv.android.xposed.XposedHelpers

object ShowNotificationImportanceForAll : HookRegister() {

    override fun init() {
        findMethod("com.android.settings.notification.ChannelNotificationSettings") {
            name == "removeDefaultPrefs"
        }.hookBefore {
            if (!XSPUtils.getBoolean("show_notification_importance", false)) return@hookBefore
            val importance = it.thisObject.invokeMethodAuto("findPreference", "importance") ?: return@hookBefore
            val mChannel = it.thisObject.getObject("mChannel") as NotificationChannel
            val index = importance.invokeMethodAutoAs<Int>("findSpinnerIndexOfValue", mChannel.importance.toString())!!
            if (index < 0) return@hookBefore
            importance.invokeMethodAuto("setValueIndex", index)
            XposedHelpers.setAdditionalInstanceField(importance, "channelNotificationSettings", it.thisObject)
            it.result = null
        }

        findMethod("androidx.preference.Preference") {
            name == "callChangeListener" && parameterTypes[0] == Any::class.java
        }.hookAfter {
            val channelNotificationSettings = XposedHelpers.getAdditionalInstanceField(it.thisObject, "channelNotificationSettings") ?: return@hookAfter
            val mChannel = channelNotificationSettings.getObject("mChannel") as NotificationChannel
            mChannel.invokeMethodAuto("setImportance", (it.args[0] as String).toInt())
            val mBackend = channelNotificationSettings.getObjectOrNull("mBackend") ?: return@hookAfter
            val mPkg = channelNotificationSettings.getObjectAs<String>("mPkg")
            val mUid = channelNotificationSettings.getObjectAs<Int>("mUid")
            mBackend.invokeMethodAuto("updateChannel", mPkg, mUid, mChannel)
        }
    }

}