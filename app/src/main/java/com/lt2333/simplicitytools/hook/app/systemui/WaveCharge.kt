package com.lt2333.simplicitytools.hook.app.systemui

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookMethod
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.xposed.base.HookRegister
import de.robv.android.xposed.XposedHelpers

object WaveCharge: HookRegister() {

    override fun init() {
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
