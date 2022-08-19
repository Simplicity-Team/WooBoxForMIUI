package com.lt2333.simplicitytools.hook.app.powerkeeper

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.xposed.base.HookRegister

object DoNotClearApp : HookRegister() {
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
    }
}