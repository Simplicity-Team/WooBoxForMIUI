package com.lt2333.simplicitytools.hook.app.systemui

import android.content.res.Configuration
import android.view.ViewGroup
import com.lt2333.simplicitytools.util.XSPUtils
import com.lt2333.simplicitytools.util.hasEnable
import com.lt2333.simplicitytools.util.hookAfterMethod
import com.lt2333.simplicitytools.util.hookBeforeMethod
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class Test : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        hasEnable("old_qs_custom_switch") {
            val mRows = XSPUtils.getInt("qs_custom_rows", 3)
            val mRowsHorizontal = XSPUtils.getInt("qs_custom_rows_horizontal", 2)
            val mColumns = XSPUtils.getInt("qs_custom_columns", 4)
            val mColumnsUnexpanded = XSPUtils.getInt("qs_custom_columns_unexpanded", 5)

            "com.android.systemui.qs.MiuiQuickQSPanel".hookBeforeMethod(
                lpparam.classLoader,
                "setMaxTiles", Int::class.java
            ) {
                //未展开时的列数
                it.args[0] = mColumnsUnexpanded
            }

            "com.android.systemui.qs.MiuiTileLayout".hookAfterMethod(
                lpparam.classLoader,
                "updateColumns"
            ) {
                //展开时的列数
                XposedHelpers.setObjectField(it.thisObject, "mColumns", mColumns)
            }
            "com.android.systemui.qs.MiuiTileLayout".hookAfterMethod(
                lpparam.classLoader,
                "updateResources"
            ) {
                //展开时的行数
                val viewGroup = it.thisObject as ViewGroup
                val mConfiguration: Configuration = viewGroup.context.resources.configuration
                if (mConfiguration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    XposedHelpers.setObjectField(viewGroup, "mMaxAllowedRows", mRows)
                } else {
                    XposedHelpers.setObjectField(viewGroup, "mMaxAllowedRows", mRowsHorizontal)
                }
                viewGroup.requestLayout()
            }
        }
    }
}