package com.lt2333.simplicitytools.hooks.rules.all.miuihome

import android.app.AndroidAppHelper
import android.content.ComponentName
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import com.github.kyuubiran.ezxhelper.init.InitFields.moduleRes
import com.github.kyuubiran.ezxhelper.utils.args
import com.github.kyuubiran.ezxhelper.utils.findAllMethods
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.getStaticObject
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.github.kyuubiran.ezxhelper.utils.invokeMethod
import com.github.kyuubiran.ezxhelper.utils.invokeStaticMethod
import com.github.kyuubiran.ezxhelper.utils.loadClass
import com.github.kyuubiran.ezxhelper.utils.putStaticObject
import com.lt2333.simplicitytools.R
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister
import de.robv.android.xposed.XposedHelpers

object ShortcutAddSmallWindowForAll : HookRegister() {
    override fun init() = hasEnable("miuihome_shortcut_add_small_window") {
        val mViewDarkModeHelper = loadClass("com.miui.home.launcher.util.ViewDarkModeHelper")
        val mSystemShortcutMenu = loadClass("com.miui.home.launcher.shortcuts.SystemShortcutMenu")
        val mSystemShortcutMenuItem = loadClass("com.miui.home.launcher.shortcuts.SystemShortcutMenuItem")
        val mAppShortcutMenu = loadClass("com.miui.home.launcher.shortcuts.AppShortcutMenu")
        val mShortcutMenuItem = loadClass("com.miui.home.launcher.shortcuts.ShortcutMenuItem")
        val mAppDetailsShortcutMenuItem = loadClass("com.miui.home.launcher.shortcuts.SystemShortcutMenuItem\$AppDetailsShortcutMenuItem")
        val mActivityUtilsCompat = loadClass("com.miui.launcher.utils.ActivityUtilsCompat")
        findAllMethods(mViewDarkModeHelper) {
            name == "onConfigurationChanged"
        }.hookAfter {
            mSystemShortcutMenuItem.invokeStaticMethod("createAllSystemShortcutMenuItems")
        }
        findAllMethods(mShortcutMenuItem) {
            name == "getShortTitle"
        }.hookAfter {
            if (it.result == "应用信息") {
                it.result = "信息"
            }
        }
        findMethod(mAppDetailsShortcutMenuItem) {
            name == "lambda\$getOnClickListener$0" && parameterCount == 2
        }.hookBefore {
            val obj = it.args[0]
            val view: View = it.args[1] as View
            val mShortTitle = obj.invokeMethod("getShortTitle") as CharSequence
            if (mShortTitle == moduleRes.getString(R.string.miuihome_shortcut_add_small_window_title)) {
                it.result = null
                val intent = Intent()
                val mComponentName = obj.invokeMethod("getComponentName") as ComponentName
                intent.action = "android.intent.action.MAIN"
                intent.addCategory("android.intent.category.LAUNCHER")
                intent.component = mComponentName
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                val callStaticMethod = mActivityUtilsCompat.invokeStaticMethod("makeFreeformActivityOptions", args(view.context, mComponentName.packageName))
                if (callStaticMethod != null) {
                    view.context.startActivity(intent, callStaticMethod.invokeMethod("toBundle") as Bundle)
                }
            }
        }
        findAllMethods(mSystemShortcutMenu) {
            name == "getMaxShortcutItemCount"
        }.hookAfter {
            it.result = 5
        }
        findAllMethods(mAppShortcutMenu) {
            name == "getMaxShortcutItemCount"
        }.hookAfter {
            it.result = 5
        }
        findAllMethods(mSystemShortcutMenuItem) {
            name == "createAllSystemShortcutMenuItems"
        }.hookAfter {
            val isDarkMode =
                AndroidAppHelper.currentApplication().applicationContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
            val mAllSystemShortcutMenuItems = mSystemShortcutMenuItem.getStaticObject("sAllSystemShortcutMenuItems") as Collection<Any>
            val mSmallWindowInstance = XposedHelpers.newInstance(mAppDetailsShortcutMenuItem)
            mSmallWindowInstance.invokeMethod("setShortTitle", args(moduleRes.getString(R.string.miuihome_shortcut_add_small_window_title)))
            mSmallWindowInstance.invokeMethod(
                "setIconDrawable",
                args(if (isDarkMode) moduleRes.getDrawable(R.drawable.ic_small_window_dark) else moduleRes.getDrawable(R.drawable.ic_small_window_light))
            )
            val sAllSystemShortcutMenuItems = ArrayList<Any>()
            sAllSystemShortcutMenuItems.add(mSmallWindowInstance)
            sAllSystemShortcutMenuItems.addAll(mAllSystemShortcutMenuItems)
            mSystemShortcutMenuItem.putStaticObject("sAllSystemShortcutMenuItems", sAllSystemShortcutMenuItems)
        }
    }

}
