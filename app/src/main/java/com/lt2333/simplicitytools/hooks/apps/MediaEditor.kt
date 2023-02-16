package com.lt2333.simplicitytools.hooks.apps

import android.os.Build
import com.lt2333.simplicitytools.hooks.rules.s.mediaeditor.UnlockUnlimitedCroppingForS
import com.lt2333.simplicitytools.hooks.rules.t.mediaeditor.UnlockUnlimitedCroppingForT
import com.lt2333.simplicitytools.utils.xposed.base.AppRegister
import de.robv.android.xposed.callbacks.XC_LoadPackage

object MediaEditor : AppRegister() {
    override val packageName: String = "com.miui.mediaeditor"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        when (Build.VERSION.SDK_INT) {
            Build.VERSION_CODES.TIRAMISU -> {
                autoInitHooks(
                    lpparam,
                    UnlockUnlimitedCroppingForT, //解锁裁切最小值
                )
            }

            Build.VERSION_CODES.S -> {
                autoInitHooks(
                    lpparam,
                    UnlockUnlimitedCroppingForS, //解锁裁切最小值
                )
            }
        }
    }
}