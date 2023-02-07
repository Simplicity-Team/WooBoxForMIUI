package com.lt2333.simplicitytools.hooks.rules.s.systemui

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.github.kyuubiran.ezxhelper.utils.putObject
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister

object WaveChargeForS: HookRegister() {

    override fun init() {
        hasEnable("enable_wave_charge_animation") {
            findMethod("com.android.keyguard.charge.ChargeUtils") {
                name == "supportWaveChargeAnimation"
            }.hookAfter {
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
                it.result = mResult
            }
            findMethod("com.android.keyguard.charge.wave.WaveView") {
                name == "updateWaveHeight"
            }.hookAfter {
                it.thisObject.putObject("mWaveXOffset", 0)
            }
        }
    }

}
