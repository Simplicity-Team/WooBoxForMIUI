package com.lt2333.simplicitytools.utils
import android.annotation.SuppressLint
import android.content.Context


object SPUtils {
    @SuppressLint("WorldReadableFiles")
    fun getBoolean(context: Context, key: String, defValue: Boolean): Boolean {
        val pref = context.getSharedPreferences("config",Context.MODE_WORLD_READABLE)
        return pref.getBoolean(key, defValue)
    }

}

