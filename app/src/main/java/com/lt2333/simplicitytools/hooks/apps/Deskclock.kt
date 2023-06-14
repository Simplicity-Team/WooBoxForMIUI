package com.lt2333.simplicitytools.hooks.apps

import android.os.Build
import com.lt2333.simplicitytools.hooks.rules.all.deskclock.VibrationGraduallyStrongerForAll
import com.lt2333.simplicitytools.utils.xposed.base.AppRegister
import de.robv.android.xposed.callbacks.XC_LoadPackage

object Deskclock : AppRegister() {

    const val KEY_ENABLE_VARIATION_GRADUALLY_STRONGER = "deskclock_vibration_gradually_stronger"
    const val CLZ_NAME_SET_ALARM_ACTIVITY = "com.android.deskclock.alarm.SetAlarmActivity"
    const val CLZ_NAME_ALARM_KLAXON = "com.android.deskclock.alarm.alert.AlarmKlaxon"

    override val packageName: String = "com.android.deskclock"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        when (Build.VERSION.SDK_INT) {
            Build.VERSION_CODES.TIRAMISU -> {
                autoInitHooks(
                    lpparam,
                    VibrationGraduallyStrongerForAll, // 闹钟震动渐强
                )
            }

            Build.VERSION_CODES.S -> {
                autoInitHooks(
                    lpparam,
                    VibrationGraduallyStrongerForAll, // 闹钟震动渐强
                )
            }
        }
    }
}