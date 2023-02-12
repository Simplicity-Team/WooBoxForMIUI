package com.lt2333.simplicitytools.hooks.rules.t.systemui

import android.content.res.Configuration
import android.view.ViewGroup
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.github.kyuubiran.ezxhelper.utils.putObject
import com.lt2333.simplicitytools.utils.XSPUtils
import com.lt2333.simplicitytools.utils.hasEnable
import com.lt2333.simplicitytools.utils.xposed.base.HookRegister

object OldQSCustomForT : HookRegister() {

    override fun init() = hasEnable("old_qs_custom_switch") {
        val mRows = XSPUtils.getInt("qs_custom_rows", 3)
        val mRowsHorizontal = XSPUtils.getInt("qs_custom_rows_horizontal", 2)
        val mColumns = XSPUtils.getInt("qs_custom_columns", 4)
        val mColumnsUnexpanded = XSPUtils.getInt("qs_custom_columns_unexpanded", 5)

        findMethod("com.android.systemui.qs.MiuiQuickQSPanel") {
            name == "setMaxTiles" && parameterCount == 1
        }.hookBefore {
            //未展开时的列数
            it.args[0] = mColumnsUnexpanded
        }

        findMethod("com.android.systemui.qs.MiuiTileLayout") {
            name == "updateColumns"
        }.hookAfter {
            //展开时的列数
            it.thisObject.putObject("mColumns", mColumns)
        }

        findMethod("com.android.systemui.qs.MiuiTileLayout") {
            name == "updateResources"
        }.hookAfter {
            //展开时的行数
            val viewGroup = it.thisObject as ViewGroup
            val mConfiguration: Configuration = viewGroup.context.resources.configuration
            if (mConfiguration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                viewGroup.putObject("mMaxAllowedRows", mRows)
            } else {
                viewGroup.putObject("mMaxAllowedRows", mRowsHorizontal)
            }
            viewGroup.requestLayout()
        }
    }

}