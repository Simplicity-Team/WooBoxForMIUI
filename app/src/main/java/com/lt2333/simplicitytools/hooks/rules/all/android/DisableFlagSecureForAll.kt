package com.lt2333.simplicitytools.hooks.rules.all.android

import android.os.Build
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.MembersType
import com.lt2333.simplicitytools.utils.hasEnable

object DisableFlagSecureForAll : YukiBaseHooker() {
    override fun onHook() {
        "com.android.server.wm.WindowState".hook {
            injectMember {
                method { name = "isSecureLocked" }
                beforeHook {
                    hasEnable("disable_flag_secure") {
                        this.result = false
                    }
                }
            }
        }
        //Android13 额外Hook
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
            "com.android.server.wm.WindowSurfaceController".hook {
                injectMember {
                    method { name = "setSecure" }
                    beforeHook {
                        hasEnable("disable_flag_secure") {
                            this.args[0] = false
                        }
                    }
                }
                injectMember {
                    allMembers(MembersType.CONSTRUCTOR)
                    beforeHook {
                        hasEnable("disable_flag_secure") {
                            var flags = this.args[2] as Int
                            val secureFlag = 128
                            flags = flags and secureFlag.inv()
                            this.args[2] = flags
                        }
                    }
                }
            }
        }
    }
}