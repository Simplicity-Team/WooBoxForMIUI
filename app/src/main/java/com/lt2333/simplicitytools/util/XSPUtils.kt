package com.lt2333.simplicitytools.util

import com.lt2333.simplicitytools.BuildConfig
import de.robv.android.xposed.XSharedPreferences

object XSPUtils {
    var prefs = XSharedPreferences(BuildConfig.APPLICATION_ID, "config")

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        if (prefs.hasFileChanged()) {
            prefs.reload()
        }
        return prefs.getBoolean(key, defValue)
    }
    fun getInt(key: String, defValue: Int): Int {
        if (prefs.hasFileChanged()) {
            prefs.reload()
        }
        return prefs.getInt(key, defValue)
    }
}

inline fun hasEnable(key: String, default: Boolean = false, crossinline block: () -> Unit) {
    if (XSPUtils.getBoolean(key, default)) {
        block()
    }
}
