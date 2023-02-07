package com.lt2333.simplicitytools.hooks.rules.s.powerkeeper

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister

object DoNotClearAppForS : HookRegister() {
    override fun init() {
        findMethod("com.miui.powerkeeper.statemachine.SleepModeControllerNew") {
            name == "clearApp"
        }.hookBefore {
            hasEnable("do_not_clear_app") {
                it.result = null
            }
        }

        findMethod("com.miui.powerkeeper.statemachine.PowerStateMachine") {
            name == "clearAppWhenScreenOffTimeOut"
        }.hookBefore {
            hasEnable("do_not_clear_app") {
                it.result = null
            }
        }

        findMethod("com.miui.powerkeeper.statemachine.PowerStateMachine") {
            name == "clearAppWhenScreenOffTimeOutInNight"
        }.hookBefore {
            hasEnable("do_not_clear_app") {
                it.result = null
            }
        }

        findMethod("com.miui.powerkeeper.powerchecker.PowerCheckerController") {
            name == "clearApp"
        }.hookBefore {
            hasEnable("do_not_clear_app") {
                it.result = null
            }
        }

        findMethod("com.miui.powerkeeper.powerchecker.PowerCheckerController") {
            name == "autoKillApp"
        }.hookBefore {
            hasEnable("do_not_clear_app") {
                it.result = null
            }
        }

        findMethod("com.miui.powerkeeper.statemachine.SleepModeController\$SleepProcessHelper") {
            name == "killAppsInSleep"
        }.hookBefore {
            hasEnable("do_not_clear_app") {
                it.result = null
            }
        }

        findMethod("com.miui.powerkeeper.statemachine.DynamicTurboPowerHandler") {
            name == "clearApp"
        }.hookBefore {
            hasEnable("do_not_clear_app") {
                it.result = null
            }
        }

        findMethod("miui.process.ProcessManager") {
            name == "kill"
        }.hookBefore {
            hasEnable("do_not_clear_app") {
                it.result = false
            }
        }
    }
}