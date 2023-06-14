package com.lt2333.simplicitytools.hooks.rules.all.deskclock

import android.app.Activity
import android.content.Context
import android.media.AudioAttributes
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Switch
import cn.fkj233.ui.activity.dp2px
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.github.kyuubiran.ezxhelper.utils.invokeMethod
import com.lt2333.simplicitytools.hooks.apps.Deskclock
import com.lt2333.simplicitytools.utils.getObjectField
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.hookAfterMethod
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister

object VibrationGraduallyStrongerForAll : HookRegister() {

    /**
     * 增加的开关控件id
     */
    private const val SWITCH_ID = "_switch_vgs_enabled"

    /**
     * 存放每个闹钟的震动渐强配置
     */
    private const val SP_NAME = "_vibration_gradually_stronger_config"

    // region CLZ_NAME_ALARM_KLAXON 临时变量
    private var graduallyStrongerEnabled: Boolean = false
    // endregion

    const val TAG = "震动渐强"

    override fun init() {
        hasEnable(Deskclock.KEY_ENABLE_VARIATION_GRADUALLY_STRONGER) {
            // 1. 修改UI界面，增加选项
            Deskclock.CLZ_NAME_SET_ALARM_ACTIVITY.hookAfterMethod("initAll", Bundle::class.java) {
                // Log.d(TAG, "after initAll")

                val activity = it.thisObject as Activity

                val mId = activity.getObjectField("mId")!!
                // mOriginalAlarmId，创建时为-1
                val mOriginalAlarmId =
                    activity.getObjectField("mOriginalAlarm")!!.getObjectField("id")!!
                val mAlarmChangedId = activity.getObjectField("mAlarmChanged")?.getObjectField("id")
                Log.d(
                    TAG,
                    "mId${mId}, mOriginalAlarmId${mOriginalAlarmId}, mAlarmChangedId:${mAlarmChangedId}"
                )

                val sp = activity.getSharedPreferences(
                    SP_NAME,
                    Context.MODE_PRIVATE
                )

                val scrollHolderLayoutID =
                    activity.resources.getIdentifier("scroll_holder", "id", activity.packageName)
                // Log.d(TAG, "scrollHolderLayoutID=${scrollHolderLayoutID}")

                activity.runOnUiThread {
                    val scrollHolder = activity.findViewById<ScrollView>(scrollHolderLayoutID)
                    val linearLayout = scrollHolder.getChildAt(0) as LinearLayout
                    val switch = Switch(activity).apply {
                        id = context.resources.getIdentifier(
                            SWITCH_ID,
                            "id",
                            context.packageName
                        )
                        setText("震动渐强")
                        setTextAppearance(
                            android.R.style.TextAppearance_Material_Title
                        )
                        setTextSize(18F)
                        layoutParams.apply {
                            setPadding(
                                dp2px(context, 30F),
                                dp2px(context, 15F),
                                dp2px(context, 30F),
                                dp2px(context, 15F)
                            )
                        }
                        isChecked = sp.getBoolean("$mOriginalAlarmId", false)
                    }
                    linearLayout.addView(switch)
                }
            }

            // 2. 保存闹钟后保存自定义配置
            findMethod(Deskclock.CLZ_NAME_SET_ALARM_ACTIVITY) {
                name == "saveAlarm" && parameterCount == 1
            }.hookAfter {
                // Log.i(TAG, "after saveAlarm")

                val activity = (it.thisObject as Activity)
                val sp = activity.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
                val alarmEnabled = activity.findViewById<Switch>(
                    activity.resources.getIdentifier(SWITCH_ID, "id", activity.packageName)
                )?.isChecked ?: false

                val mId = activity.getObjectField("mId")!!
                val mOriginalAlarmId =
                    activity.getObjectField("mOriginalAlarm")!!.getObjectField("id")!!
                val mAlarmChangedId = activity.getObjectField("mAlarmChanged")?.getObjectField("id")
                Log.d(
                    TAG,
                    "mId${mId}, mOriginalAlarmId${mOriginalAlarmId}, mAlarmChangedId:${mAlarmChangedId}"
                )

                // mId
                sp.edit().putBoolean("$mId", alarmEnabled).apply()
                // Log.d(TAG, "sp路径${(XSPUtils.findFieldObject { name == "prefs" } as XSharedPreferences).file.absolutePath}")
            }

            // 3. 使用配置，覆盖闹钟震动模式
            // 3.1 获取当前生效的闹钟
            findMethod(Deskclock.CLZ_NAME_ALARM_KLAXON) {
                name == "start" && parameterCount == 2
            }.hookBefore {
                val id = it.args[1].getObjectField("id") as Int
                Log.d(TAG, "before start, id=${id}")
                graduallyStrongerEnabled = (it.args[0] as Context).getSharedPreferences(
                    SP_NAME, Context.MODE_PRIVATE
                ).getBoolean("$id", false)
                Log.d(TAG, "$id, $graduallyStrongerEnabled")
            }

            // 3.2 读取闹钟配置
            Deskclock.CLZ_NAME_ALARM_KLAXON.hookAfterMethod(
                "vibrateLOrLater",
                Vibrator::class.java
            ) {
                Log.d(
                    TAG,
                    "after vibrateLOrLater, graduallyStrongerEnabled=${graduallyStrongerEnabled}"
                )
                if (!graduallyStrongerEnabled) {
                    return@hookAfterMethod
                }

                val vibrator = it.args[0] as Vibrator
                vibrator.cancel()

                // 照搬原来的
                val audioAttributes = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()

                // 重点是这里
                val vibratorEffect = VibrationEffect.createWaveform(
                    LongArray(100) { index ->
                        // 等待100ms后，震动100ms，周期200ms
                        100L
                    },
                    IntArray(100) { index ->
                        // 震动由弱至强，共255（1-255）个等级
                        (index + 1) * 2
                    },

                    // 到最强后从最弱重复
                    0
                )

                vibrator.vibrate(vibratorEffect, audioAttributes)
            }

            // 4 删除闹钟时删除对应的配置
            findMethod("com.android.deskclock.alarm.AlarmClockFragment") {
                name == "access\$2902" && parameterCount == 2
            }.hookAfter {
                Log.d(TAG, "after access\$2902")
                Log.d(TAG, "checkedItems: ${(it.args[1] as IntArray?)?.joinToString(",")}")
                // 删除闹钟对应的震动渐强配置
                (it.args[1] as IntArray?)?.let { checkedItems ->
                    (it.args[0].invokeMethod("getContext") as Context)
                        .getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
                        .edit().apply {
                            for (id in checkedItems) {
                                this.remove("$id")
                            }
                        }
                        .apply()
                }
            }
        }
    }

}