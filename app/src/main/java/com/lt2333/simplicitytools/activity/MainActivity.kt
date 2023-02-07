package com.lt2333.simplicitytools.activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import cn.fkj233.ui.activity.MIUIActivity
import cn.fkj233.ui.dialog.MIUIDialog
import com.lt2333.simplicitytools.BuildConfig
import com.lt2333.simplicitytools.R
import com.lt2333.simplicitytools.activity.pages.all.AboutPage
import com.lt2333.simplicitytools.activity.pages.all.MenuPage
import com.lt2333.simplicitytools.activity.pages.s.*
import com.lt2333.simplicitytools.activity.pages.t.*
import com.lt2333.simplicitytools.hooks.rules.s.android.*
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import java.util.*
import kotlin.system.exitProcess

class MainActivity : MIUIActivity() {
    private val activity = this
    override fun onCreate(savedInstanceState: Bundle?) {
        if (!checkLSPosed()) isLoad = false
        super.onCreate(savedInstanceState)
        if (isLoad && !BuildConfig.DEBUG) {
            AppCenter.start(
                application,
                "ae2037d3-9914-4e0c-b02b-f9b2bb2574e5",
                Analytics::class.java,
                Crashes::class.java
            )
        }
    }

    //检测LSPosed是否激活
    @SuppressLint("WorldReadableFiles")
    private fun checkLSPosed(): Boolean {
        try {
            setSP(getSharedPreferences("config", MODE_WORLD_READABLE))
            return true
        } catch (exception: SecurityException) {
            MIUIDialog(this) {
                setTitle(R.string.Tips)
                setMessage(R.string.not_support)
                setCancelable(false)
                setRButton(R.string.Done) {
                    exitProcess(0)
                }
            }.show()
        }
        return false
    }


    init {
        when (Build.VERSION.SDK_INT) {
            Build.VERSION_CODES.TIRAMISU -> {
                registerPage(MainPageForT::class.java)
                registerPage(SystemUIPageForT::class.java)
                registerPage(AndroidPageForT::class.java)
                registerPage(OtherPageForT::class.java)
                registerPage(HideIconPageForT::class.java)
                registerPage(AboutPage::class.java)
                registerPage(MenuPage::class.java)
            }
            Build.VERSION_CODES.S -> {
                registerPage(MainPageForS::class.java)
                registerPage(SystemUIPageForS::class.java)
                registerPage(AndroidPageForS::class.java)
                registerPage(OtherPageForS::class.java)
                registerPage(HideIconPageForS::class.java)
                registerPage(AboutPage::class.java)
                registerPage(MenuPage::class.java)
            }
        }
    }
}