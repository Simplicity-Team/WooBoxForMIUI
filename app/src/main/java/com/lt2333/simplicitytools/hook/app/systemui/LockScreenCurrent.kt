package com.lt2333.simplicitytools.hook.app.systemui

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.graphics.Typeface
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import cn.fkj233.ui.activity.dp2px
import com.github.kyuubiran.ezxhelper.init.InitFields
import com.lt2333.simplicitytools.R
import com.lt2333.simplicitytools.util.findClass
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookAfterAllMethods
import com.lt2333.simplicitytools.util.hookAfterMethod
import com.microsoft.appcenter.utils.HandlerUtils.runOnUiThread
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.io.BufferedReader
import java.io.FileReader
import java.lang.reflect.Method
import java.util.*
import kotlin.math.roundToInt

class LockScreenCurrent(private var battery: String = "") : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        hasEnable("lock_screen_current") {
            val miuiPhoneStatusBarViewClass =
                "com.android.systemui.statusbar.phone.KeyguardBottomAreaView".findClass(lpparam.classLoader)
            miuiPhoneStatusBarViewClass.hookAfterMethod("onFinishInflate") { param ->
                val viewGroup = param.thisObject as ViewGroup
                battery = getBatteryTemperature(viewGroup.context).toString()
                val textView = TextView(viewGroup.context).also {
                    it.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                    it.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 14f)
                    it.gravity = Gravity.CENTER or Gravity.BOTTOM
                    it.setPadding(0, 0, 0, dp2px(viewGroup.context, 5f))
                }
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        runOnUiThread {
                            textView.text = getCurrent()
                        }
                    }

                }, 0, 1000)
                val darkIconDispatcherClass =
                    "com.android.systemui.plugins.DarkIconDispatcher".findClass(lpparam.classLoader)
                darkIconDispatcherClass.hookAfterAllMethods("getTint") {
                    val areaTint = it.args[2] as Int
                    textView.setTextColor(areaTint)
                }
                viewGroup.addView(textView)
            }
        }
    }

    /**
     * 原始代码来自 CSDN
     * https://blog.csdn.net/zhangyongfeiyong/article/details/53641809
     */
    @SuppressLint("PrivateApi")
    private fun getCurrent(): String {
        var result = ""
        try {
            val systemProperties = Class.forName("android.os.SystemProperties")
            val get = systemProperties.getDeclaredMethod("get", String::class.java) as Method
            val platName = get.invoke(null, "ro.hardware") as String
            if (platName.startsWith("mt") || platName.startsWith("MT")) {
                val filePath =
                    "/sys/class/power_supply/battery/device/FG_Battery_CurrentConsumption"
                result = "${(getMeanCurrentVal(filePath, 5, 0) / 1000.0f).roundToInt()} mA"
            } else if (platName.startsWith("qcom")) {
                val filePath = "/sys/class/power_supply/battery/current_now"
                val current = (getMeanCurrentVal(filePath, 5, 0) / 1000.0f).roundToInt()
                result = if (current < 0) {
                    "${InitFields.moduleRes.getString(R.string.current_current)}$current mA，${
                        InitFields.moduleRes.getString(
                            R.string.temp
                        )
                    }$battery℃".replace("-", "")
                } else {
                    "${InitFields.moduleRes.getString(R.string.current_current)}-$current mA"
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    /**
     * 获取平均电流值
     * 获取 filePath 文件 totalCount 次数的平均值，每次采样间隔 intervalMs 时间
     */
    private fun getMeanCurrentVal(filePath: String, totalCount: Int, intervalMs: Int): Float {
        var meanVal = 0.0f
        if (totalCount <= 0) {
            return 0.0f
        }
        for (i in 0 until totalCount) {
            try {
                val f: Float = readFile(filePath, 0).toFloat()
                meanVal += f / totalCount
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (intervalMs <= 0) {
                continue
            }
            try {
                Thread.sleep(intervalMs.toLong())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return meanVal
    }

    private fun readFile(path: String, defaultValue: Int): Int {
        try {
            val bufferedReader = BufferedReader(FileReader(path))
            val i: Int = bufferedReader.readLine().toInt(10)
            bufferedReader.close()
            return i
        } catch (localException: java.lang.Exception) {
        }
        return defaultValue
    }

    private fun getBatteryTemperature(context: Context): Int {
        return context.registerReceiver(
            null as BroadcastReceiver?,
            IntentFilter("android.intent.action.BATTERY_CHANGED")
        )!!.getIntExtra("temperature", 0) / 10
    }
}