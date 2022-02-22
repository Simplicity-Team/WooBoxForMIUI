package com.lt2333.simplicitytools.hook.app.systemui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.provider.Settings
import android.util.AttributeSet
import android.widget.TextView
import com.lt2333.simplicitytools.util.XSPUtils
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.lang.reflect.Method
import java.text.SimpleDateFormat
import java.util.*

class StatusBarTimeSeconds :IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (XSPUtils.getBoolean("status_bar_time_seconds", false)) {
            var c: Context? = null
            val classIfExists = XposedHelpers.findClassIfExists(
                "com.android.systemui.statusbar.views.MiuiClock",
                lpparam.classLoader
            )
            XposedHelpers.findAndHookConstructor(classIfExists,
                Context::class.java,
                AttributeSet::class.java,
                Integer.TYPE,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        c = param.args[0] as Context
                        val textV = param.thisObject as TextView
                        val d: Method = textV.javaClass.getDeclaredMethod("updateTime")
                        val r = Runnable {
                            d.isAccessible = true
                            d.invoke(textV)
                        }

                        class T : TimerTask() {
                            override fun run() {
                                Handler(textV.context.mainLooper).post(r)
                            }
                        }
                        if (textV.resources.getResourceEntryName(textV.id) == "clock")
                            Timer().scheduleAtFixedRate(
                                T(),
                                1000 - System.currentTimeMillis() % 1000,
                                1000
                            )
                    }
                }
            )
            XposedHelpers.findAndHookMethod(classIfExists, "updateTime",
                object : XC_MethodHook() {
                    @SuppressLint("SetTextI18n", "SimpleDateFormat")
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val textV = param.thisObject as TextView
                        if (textV.resources.getResourceEntryName(textV.id) == "clock") {
                            val t = Settings.System.getString(
                                c!!.contentResolver,
                                Settings.System.TIME_12_24
                            )
                            if (t == "24")
                                textV.text =
                                    SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().time)
                            else
                                textV.text =
                                    textV.text.toString() + SimpleDateFormat(":ss").format(Calendar.getInstance().time)
                        }
                    }
                })

        }
    }
}