package com.lt2333.simplicitytools.hook.app.systemui

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookMethod
import com.lt2333.simplicitytools.util.hasEnable
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class WaveCharge : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        hasEnable("enable_wave_charge_animation") {
            findMethod("com.android.keyguard.charge.ChargeUtils") {
                name == "supportWaveChargeAnimation"
            }.hookMethod {
                after { param ->
                    val ex = Throwable()
                    val stackElement = ex.stackTrace
                    var mResult = false
                    val classTrue = setOf(
                        "com.android.keyguard.charge.ChargeUtils",
                        "com.android.keyguard.charge.container.MiuiChargeContainerView"
                    )
                    for (i in stackElement.indices) {
                        when (stackElement[i].className) {
                            in classTrue -> {
                                mResult = true
                                break
                            }
                        }
                    }
                    param.result = mResult
                }
            }
            findMethod("com.android.keyguard.charge.wave.WaveView") {
                name == "updateWaveHeight"
            }.hookMethod {
                after { param ->
                    XposedHelpers.setIntField(param.thisObject, "mWaveXOffset", 0)
                }
            }
        }
    }
}
