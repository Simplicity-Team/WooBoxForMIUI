package com.lt2333.simplicitytools.hook.app.systemui

import android.content.Context
import android.net.TrafficStats
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import com.lt2333.simplicitytools.util.*
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.text.DecimalFormat

class DoubleLineNetworkSpeed : IXposedHookLoadPackage {

    private var mLastTotalUp: Long = 0
    private var mLastTotalDown: Long = 0

    private var lastTimeStampTotalUp: Long = 0
    private var lastTimeStampTotalDown: Long = 0

    private var upIcon = ""
    private var downIcon = ""

    private val getDualSize = XSPUtils.getInt("status_bar_network_speed_dual_row_size", 0)

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {

        if (XSPUtils.getString("status_bar_network_speed_dual_row_icon", "None") != "None") {
            upIcon = XSPUtils.getString("status_bar_network_speed_dual_row_icon", "None")
                ?.firstOrNull()?.plus(" ") ?: String()
            downIcon = XSPUtils.getString("status_bar_network_speed_dual_row_icon", "None")
                ?.lastOrNull()?.plus(" ") ?: String()
        }

        hasEnable("status_bar_dual_row_network_speed") {
            "com.android.systemui.statusbar.views.NetworkSpeedView".findClass(lpparam.classLoader)
                .hookAfterConstructor(Context::class.java, AttributeSet::class.java) {
                    val mView = it.thisObject as TextView
                    mView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 7f)
                    if (getDualSize!=0){
                        mView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getDualSize.toFloat())
                    }
                    mView.isSingleLine = false
                    mView.setLineSpacing(0F, 0.8F)
                    mView.gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
                }

            "com.android.systemui.statusbar.policy.NetworkSpeedController".hookBeforeMethod(
                lpparam.classLoader,
                "formatSpeed",
                Context::class.java,
                Long::class.java
            ) {
                it.result = "${upIcon}${getTotalUpSpeed()}\n${downIcon}${getTotalDownloadSpeed()}"
            }
        }
    }

    //获取总的上行速度
    fun getTotalUpSpeed(): String {
        var totalUpSpeed = 0F

        val currentTotalTxBytes = TrafficStats.getTotalTxBytes()
        val nowTimeStampTotalUp = System.currentTimeMillis()

        //计算上传速度
        val bytes =
            (currentTotalTxBytes - mLastTotalUp) * 1000 / (nowTimeStampTotalUp - lastTimeStampTotalUp).toFloat()
        var unit = ""

        if (bytes >= 1048576) {
            totalUpSpeed =
                DecimalFormat("0.0").format(bytes / 1048576).toFloat()
            unit = "MB/s"
        } else {
            totalUpSpeed =
                DecimalFormat("0.0").format(bytes / 1024).toFloat()
            unit = "KB/s"
        }

        //保存当前的流量总和和上次的时间戳
        mLastTotalUp = currentTotalTxBytes
        lastTimeStampTotalUp = nowTimeStampTotalUp

        if (totalUpSpeed >= 100) {
            return "" + totalUpSpeed.toInt() + unit
        } else {
            return "" + totalUpSpeed + unit
        }
    }

    //获取总的下行速度
    fun getTotalDownloadSpeed(): String {
        var totalDownSpeed = 0F
        val currentTotalRxBytes = TrafficStats.getTotalRxBytes()
        val nowTimeStampTotalDown = System.currentTimeMillis()

        //计算下行速度
        val bytes =
            (currentTotalRxBytes - mLastTotalDown) * 1000 / (nowTimeStampTotalDown - lastTimeStampTotalDown).toFloat()

        var unit = ""

        if (bytes >= 1048576) {
            totalDownSpeed =
                DecimalFormat("0.0").format(bytes / 1048576).toFloat()
            unit = "MB/s"
        } else {
            totalDownSpeed =
                DecimalFormat("0.0").format(bytes / 1024).toFloat()
            unit = "KB/s"
        }
        //保存当前的流量总和和上次的时间戳
        mLastTotalDown = currentTotalRxBytes
        lastTimeStampTotalDown = nowTimeStampTotalDown

        if (totalDownSpeed >= 100) {
            return "" + totalDownSpeed.toInt() + unit
        } else {
            return "" + totalDownSpeed + unit
        }

    }


}