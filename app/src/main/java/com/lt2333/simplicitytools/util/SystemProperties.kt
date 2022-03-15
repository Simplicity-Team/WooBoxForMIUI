package com.lt2333.simplicitytools.util

import android.content.Context

object SystemProperties {
    operator fun get(context: Context, key: String?): String? {
        var ret = ""
        try {
            val cl = context.classLoader
            val SystemProperties = cl.loadClass("android.os.SystemProperties")
            //参数类型
            val paramTypes: Array<Class<*>?> = arrayOfNulls(1)
            paramTypes[0] = String::class.java
            val get = SystemProperties.getMethod("get", *paramTypes)
            //参数
            val params = arrayOfNulls<Any>(1)
            params[0] = key
            ret = get.invoke(SystemProperties, *params) as String
        } catch (iAE: IllegalArgumentException) {
            throw iAE
        } catch (e: Exception) {
            ret = ""
        }
        return ret
    }
}