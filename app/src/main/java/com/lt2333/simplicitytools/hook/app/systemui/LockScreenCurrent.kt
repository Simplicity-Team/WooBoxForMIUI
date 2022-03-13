package com.lt2333.simplicitytools.hook.app.systemui

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.github.kyuubiran.ezxhelper.init.InitFields
import com.lt2333.simplicitytools.R
import com.lt2333.simplicitytools.util.findClass
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookAfterMethod
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.io.BufferedReader
import java.io.FileReader
import java.lang.reflect.Method
import kotlin.math.roundToInt

class LockScreenCurrent : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        hasEnable("lock_screen_current") {
            "com.android.keyguard.charge.ChargeUtils".findClass(lpparam.classLoader)
                .hookAfterMethod(
                    "getChargingHintText",
                    Context::class.java,
                    Boolean::class.java,
                    Int::class.java
                ) {
                    it.result = getCurrent() + "\n" + it.result
                }
            "com.android.systemui.statusbar.phone.KeyguardBottomAreaView".findClass(lpparam.classLoader)
                .hookAfterMethod(
                    "onFinishInflate"
                ) {
                    (XposedHelpers.getObjectField(
                        it.thisObject,
                        "mIndicationText"
                    ) as TextView).isSingleLine = false
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
                val current = (-getMeanCurrentVal(filePath, 5, 0) / 1000.0f).roundToInt()
                result = "${InitFields.moduleRes.getString(R.string.current_current)} ${current}mA"
            } else if (platName.startsWith("qcom")) {
                val filePath = "/sys/class/power_supply/battery/current_now"
                val current = (-getMeanCurrentVal(filePath, 5, 0) / 1000.0f).roundToInt()
                result = "${InitFields.moduleRes.getString(R.string.current_current)} ${current}mA"
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
}