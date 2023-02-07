package com.lt2333.simplicitytools.hooks.rules.s.powerkeeper

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister

object MakeMilletMoreAggressiveForS : HookRegister() {
    override fun init() {
        findMethod("com.miui.powerkeeper.controller.FrozenAppController") {
            name == "appIsAllowToFrozen"
        }.hookBefore {
            hasEnable("make_millet_more_aggressive") {
                it.result = true
            }
        }
        findMethod("com.miui.powerkeeper.controller.FrozenAppController\$FrozenUtil") {
            name == "isHasRunningStateProcess"
        }.hookBefore {
            hasEnable("make_millet_more_aggressive") {
                it.result = false
            }
        }
        findMethod("com.miui.powerkeeper.controller.FrozenAppController\$FrozenUtil") {
            name == "isExectingService"
        }.hookBefore {
            hasEnable("make_millet_more_aggressive") {
                it.result = false
            }
        }
        findMethod("com.miui.powerkeeper.controller.FrozenAppController\$FrozenUtil") {
            name == "isReceivingBroadcast"
        }.hookBefore {
            hasEnable("make_millet_more_aggressive") {
                it.result = false
            }
        }
        findMethod("com.miui.powerkeeper.controller.FrozenAppController\$FrozenUtil") {
            name == "isHasProcessOOMTooLow"
        }.hookBefore {
            hasEnable("make_millet_more_aggressive") {
                it.result = false
            }
        }
        findMethod("com.miui.powerkeeper.controller.FrozenAppController") {
            name == "isHasNotification"
        }.hookBefore {
            hasEnable("make_millet_more_aggressive") {
                it.result = false
            }
        }
        findMethod("com.miui.powerkeeper.millet.MilletConfig") {
            name == "getEnable"
        }.hookBefore {
            hasEnable("make_millet_more_aggressive") {
                it.result = true
            }
        }
        findMethod("com.miui.powerkeeper.controller.FrozenAppController\$AppStateFrozenControl") {
            name == "frozenAppLater" &&
                    parameterTypes.contentEquals(arrayOf(Int::class.java))
        }.hookBefore {
            hasEnable("make_millet_more_aggressive") {
                it.args[0] = 3000
            }
        }
        findMethod("com.miui.powerkeeper.controller.FrozenAppController\$AppStateFrozenControl") {
            name == "isAllowFrozenNow"
        }.hookBefore {
            hasEnable("make_millet_more_aggressive") {
                hasEnable("make_millet_ignore_active") {
                    it.result = true
                }
            }
        }
    }
}