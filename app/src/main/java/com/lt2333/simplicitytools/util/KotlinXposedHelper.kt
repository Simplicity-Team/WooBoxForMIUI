package com.lt2333.simplicitytools.util

import android.content.Context
import android.content.res.XResources
import android.util.Log
import dalvik.system.BaseDexClassLoader
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LayoutInflated
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.lang.reflect.Field
import java.lang.reflect.Member
import java.util.*

const val TAG = "SimplicityTools"

fun log(any: Any?, online: Boolean = false) {
    val msg: String = if (any is Throwable) Log.getStackTraceString(any) else any.toString()
//    Log.d(TAG, msg)
    XposedBridge.log("$TAG: $msg")
}

typealias MethodHookParam = XC_MethodHook.MethodHookParam
typealias Replacer = (XC_MethodHook.MethodHookParam) -> Any?
typealias Hooker = (XC_MethodHook.MethodHookParam) -> Unit

fun Class<*>.hookMethod(method: String?, vararg args: Any?) = try {
    XposedHelpers.findAndHookMethod(this, method, *args)
} catch (e: NoSuchMethodError) {
    log(e)
    null
} catch (e: XposedHelpers.ClassNotFoundError) {
    log(e)
    null
} catch (e: ClassNotFoundException) {
    log(e)
    null
}

fun Member.hookMethod(callback: XC_MethodHook) = try {
    XposedBridge.hookMethod(this, callback)
} catch (e: Throwable) {
    log(e)
    null
}

inline fun XC_MethodHook.MethodHookParam.callHooker(crossinline hooker: Hooker) = try {
    hooker(this)
} catch (e: Throwable) {
    log("Error occurred calling hooker on ${this.method}")
    log(e)
}

inline fun XC_MethodHook.MethodHookParam.callReplacer(crossinline replacer: Replacer) = try {
    replacer(this)
} catch (e: Throwable) {
    log("Error occurred calling replacer on ${this.method}")
    log(e)
    null
}

inline fun Member.replaceMethod(crossinline replacer: Replacer) =
    hookMethod(object : XC_MethodReplacement() {
        override fun replaceHookedMethod(param: MethodHookParam) = param.callReplacer(replacer)
    })

inline fun Member.hookAfterMethod(crossinline hooker: Hooker) =
    hookMethod(object : XC_MethodHook() {
        override fun afterHookedMethod(param: MethodHookParam) = param.callHooker(hooker)
    })

inline fun Member.hookBeforeMethod(crossinline hooker: (XC_MethodHook.MethodHookParam) -> Unit) =
    hookMethod(object : XC_MethodHook() {
        override fun beforeHookedMethod(param: MethodHookParam) = param.callHooker(hooker)
    })

inline fun Class<*>.hookBeforeMethod(
    method: String?,
    vararg args: Any?,
    crossinline hooker: Hooker
) = hookMethod(method, *args, object : XC_MethodHook() {
    override fun beforeHookedMethod(param: MethodHookParam) = param.callHooker(hooker)
})

inline fun Class<*>.hookAfterMethod(
    method: String?,
    vararg args: Any?,
    crossinline hooker: Hooker
) = hookMethod(method, *args, object : XC_MethodHook() {
    override fun afterHookedMethod(param: MethodHookParam) = param.callHooker(hooker)
})

inline fun Class<*>.replaceMethod(
    method: String?,
    vararg args: Any?,
    crossinline replacer: Replacer
) = hookMethod(method, *args, object : XC_MethodReplacement() {
    override fun replaceHookedMethod(param: MethodHookParam) = param.callReplacer(replacer)
})

fun Class<*>.hookAllMethods(methodName: String?, hooker: XC_MethodHook): Set<XC_MethodHook.Unhook> =
    try {
        XposedBridge.hookAllMethods(this, methodName, hooker)
    } catch (e: NoSuchMethodError) {
        log(e)
        emptySet()
    } catch (e: XposedHelpers.ClassNotFoundError) {
        log(e)
        emptySet()
    } catch (e: ClassNotFoundException) {
        log(e)
        emptySet()
    }

inline fun Class<*>.hookBeforeAllMethods(methodName: String?, crossinline hooker: Hooker) =
    hookAllMethods(methodName, object : XC_MethodHook() {
        override fun beforeHookedMethod(param: MethodHookParam) = param.callHooker(hooker)
    })

inline fun Class<*>.hookAfterAllMethods(methodName: String?, crossinline hooker: Hooker) =
    hookAllMethods(methodName, object : XC_MethodHook() {
        override fun afterHookedMethod(param: MethodHookParam) = param.callHooker(hooker)

    })

inline fun Class<*>.replaceAfterAllMethods(methodName: String?, crossinline replacer: Replacer) =
    hookAllMethods(methodName, object : XC_MethodReplacement() {
        override fun replaceHookedMethod(param: MethodHookParam) = param.callReplacer(replacer)
    })

fun Class<*>.hookConstructor(vararg args: Any?) = try {
    XposedHelpers.findAndHookConstructor(this, *args)
} catch (e: NoSuchMethodError) {
    log(e)
    null
} catch (e: XposedHelpers.ClassNotFoundError) {
    log(e)
    null
} catch (e: ClassNotFoundException) {
    log(e)
    null
}

inline fun Class<*>.hookBeforeConstructor(vararg args: Any?, crossinline hooker: Hooker) =
    hookConstructor(*args, object : XC_MethodHook() {
        override fun beforeHookedMethod(param: MethodHookParam) = param.callHooker(hooker)
    })

inline fun Class<*>.hookAfterConstructor(vararg args: Any?, crossinline hooker: Hooker) =
    hookConstructor(*args, object : XC_MethodHook() {
        override fun afterHookedMethod(param: MethodHookParam) = param.callHooker(hooker)
    })

inline fun Class<*>.replaceConstructor(vararg args: Any?, crossinline hooker: Hooker) =
    hookConstructor(*args, object : XC_MethodReplacement() {
        override fun replaceHookedMethod(param: MethodHookParam) = param.callHooker(hooker)
    })

fun Class<*>.hookAllConstructors(hooker: XC_MethodHook): Set<XC_MethodHook.Unhook> = try {
    XposedBridge.hookAllConstructors(this, hooker)
} catch (e: NoSuchMethodError) {
    log(e)
    emptySet()
} catch (e: XposedHelpers.ClassNotFoundError) {
    log(e)
    emptySet()
} catch (e: ClassNotFoundException) {
    log(e)
    emptySet()
}

inline fun Class<*>.hookAfterAllConstructors(crossinline hooker: Hooker) =
    hookAllConstructors(object : XC_MethodHook() {
        override fun afterHookedMethod(param: MethodHookParam) = param.callHooker(hooker)
    })

inline fun Class<*>.hookBeforeAllConstructors(crossinline hooker: Hooker) =
    hookAllConstructors(object : XC_MethodHook() {
        override fun beforeHookedMethod(param: MethodHookParam) = param.callHooker(hooker)
    })

inline fun Class<*>.replaceAfterAllConstructors(crossinline hooker: Hooker) =
    hookAllConstructors(object : XC_MethodReplacement() {
        override fun replaceHookedMethod(param: MethodHookParam) = param.callHooker(hooker)
    })

fun String.hookMethod(classLoader: ClassLoader, method: String?, vararg args: Any?) = try {
    findClass(classLoader).hookMethod(method, *args)
} catch (e: XposedHelpers.ClassNotFoundError) {
    log(e)
    null
} catch (e: ClassNotFoundException) {
    log(e)
    null
}

inline fun String.hookBeforeMethod(
    classLoader: ClassLoader,
    method: String?,
    vararg args: Any?,
    crossinline hooker: Hooker
) = try {
    findClass(classLoader).hookBeforeMethod(method, *args, hooker = hooker)
} catch (e: XposedHelpers.ClassNotFoundError) {
    log(e)
    null
} catch (e: ClassNotFoundException) {
    log(e)
    null
}

inline fun String.hookAfterMethod(
    classLoader: ClassLoader,
    method: String?,
    vararg args: Any?,
    crossinline hooker: Hooker
) = try {
    findClass(classLoader).hookAfterMethod(method, *args, hooker = hooker)
} catch (e: XposedHelpers.ClassNotFoundError) {
    log(e)
    null
} catch (e: ClassNotFoundException) {
    log(e)
    null
}

inline fun String.replaceMethod(
    classLoader: ClassLoader,
    method: String?,
    vararg args: Any?,
    crossinline replacer: Replacer
) = try {
    findClass(classLoader).replaceMethod(method, *args, replacer = replacer)
} catch (e: XposedHelpers.ClassNotFoundError) {
    log(e)
    null
} catch (e: ClassNotFoundException) {
    log(e)
    null
}

fun XC_MethodHook.MethodHookParam.invokeOriginalMethod(): Any? =
    XposedBridge.invokeOriginalMethod(method, thisObject, args)

inline fun <T, R> T.runCatchingOrNull(func: T.() -> R?) = try {
    func()
} catch (e: Throwable) {
    null
}

fun Any.getObjectField(field: String?): Any? = XposedHelpers.getObjectField(this, field)

fun Any.getObjectFieldOrNull(field: String?): Any? = runCatchingOrNull {
    XposedHelpers.getObjectField(this, field)
}

@Suppress("UNCHECKED_CAST")
fun <T> Any.getObjectFieldAs(field: String?) = XposedHelpers.getObjectField(this, field) as T

@Suppress("UNCHECKED_CAST")
fun <T> Any.getObjectFieldOrNullAs(field: String?) = runCatchingOrNull {
    XposedHelpers.getObjectField(this, field) as T
}

fun Any.getIntField(field: String?) = XposedHelpers.getIntField(this, field)

fun Any.getIntFieldOrNull(field: String?) = runCatchingOrNull {
    XposedHelpers.getIntField(this, field)
}

fun Any.getLongField(field: String?) = XposedHelpers.getLongField(this, field)

fun Any.getLongFieldOrNull(field: String?) = runCatchingOrNull {
    XposedHelpers.getLongField(this, field)
}

fun Any.getBooleanFieldOrNull(field: String?) = runCatchingOrNull {
    XposedHelpers.getBooleanField(this, field)
}

fun Any.callMethod(methodName: String?, vararg args: Any?): Any? =
    XposedHelpers.callMethod(this, methodName, *args)

fun Any.callMethodOrNull(methodName: String?, vararg args: Any?): Any? = runCatchingOrNull {
    XposedHelpers.callMethod(this, methodName, *args)
}

fun Class<*>.callStaticMethod(methodName: String?, vararg args: Any?): Any? =
    XposedHelpers.callStaticMethod(this, methodName, *args)

fun Class<*>.callStaticMethodOrNull(methodName: String?, vararg args: Any?): Any? =
    runCatchingOrNull {
        XposedHelpers.callStaticMethod(this, methodName, *args)
    }

@Suppress("UNCHECKED_CAST")
fun <T> Class<*>.callStaticMethodAs(methodName: String?, vararg args: Any?) =
    XposedHelpers.callStaticMethod(this, methodName, *args) as T

@Suppress("UNCHECKED_CAST")
fun <T> Class<*>.callStaticMethodOrNullAs(methodName: String?, vararg args: Any?) =
    runCatchingOrNull {
        XposedHelpers.callStaticMethod(this, methodName, *args) as T
    }

@Suppress("UNCHECKED_CAST")
fun <T> Class<*>.getStaticObjectFieldAs(field: String?) = XposedHelpers.getStaticObjectField(
    this,
    field
) as T

@Suppress("UNCHECKED_CAST")
fun <T> Class<*>.getStaticObjectFieldOrNullAs(field: String?) = runCatchingOrNull {
    XposedHelpers.getStaticObjectField(this, field) as T
}

fun Class<*>.getStaticObjectField(field: String?): Any? =
    XposedHelpers.getStaticObjectField(this, field)

fun Class<*>.getStaticObjectFieldOrNull(field: String?): Any? = runCatchingOrNull {
    XposedHelpers.getStaticObjectField(this, field)
}

fun Class<*>.setStaticObjectField(field: String?, obj: Any?) = apply {
    XposedHelpers.setStaticObjectField(this, field, obj)
}

fun Class<*>.setStaticObjectFieldIfExist(field: String?, obj: Any?) = apply {
    try {
        XposedHelpers.setStaticObjectField(this, field, obj)
    } catch (ignored: Throwable) {
    }
}

inline fun <reified T> Class<*>.findFieldByExactType(): Field? =
    XposedHelpers.findFirstFieldByExactType(this, T::class.java)

fun Class<*>.findFieldByExactType(type: Class<*>): Field? =
    XposedHelpers.findFirstFieldByExactType(this, type)

@Suppress("UNCHECKED_CAST")
fun <T> Any.callMethodAs(methodName: String?, vararg args: Any?) =
    XposedHelpers.callMethod(this, methodName, *args) as T

@Suppress("UNCHECKED_CAST")
fun <T> Any.callMethodOrNullAs(methodName: String?, vararg args: Any?) = runCatchingOrNull {
    XposedHelpers.callMethod(this, methodName, *args) as T
}

fun Any.callMethod(methodName: String?, parameterTypes: Array<Class<*>>, vararg args: Any?): Any? =
    XposedHelpers.callMethod(this, methodName, parameterTypes, *args)

fun Any.callMethodOrNull(
    methodName: String?,
    parameterTypes: Array<Class<*>>,
    vararg args: Any?
): Any? = runCatchingOrNull {
    XposedHelpers.callMethod(this, methodName, parameterTypes, *args)
}

fun Class<*>.callStaticMethod(
    methodName: String?,
    parameterTypes: Array<Class<*>>,
    vararg args: Any?
): Any? = XposedHelpers.callStaticMethod(this, methodName, parameterTypes, *args)

fun Class<*>.callStaticMethodOrNull(
    methodName: String?,
    parameterTypes: Array<Class<*>>,
    vararg args: Any?
): Any? = runCatchingOrNull {
    XposedHelpers.callStaticMethod(this, methodName, parameterTypes, *args)
}

fun String.findClass(classLoader: ClassLoader?): Class<*> =
    XposedHelpers.findClass(this, classLoader)

fun String.findClassOrNull(classLoader: ClassLoader?): Class<*>? =
    XposedHelpers.findClassIfExists(this, classLoader)

fun Class<*>.new(vararg args: Any?): Any = XposedHelpers.newInstance(this, *args)

fun Class<*>.new(parameterTypes: Array<Class<*>>, vararg args: Any?): Any =
    XposedHelpers.newInstance(this, parameterTypes, *args)

fun Class<*>.findField(field: String?): Field = XposedHelpers.findField(this, field)

fun Class<*>.findFieldOrNull(field: String?): Field? = XposedHelpers.findFieldIfExists(this, field)

fun <T> T.setIntField(field: String?, value: Int) = apply {
    XposedHelpers.setIntField(this, field, value)
}

fun <T> T.setLongField(field: String?, value: Long) = apply {
    XposedHelpers.setLongField(this, field, value)
}

fun <T> T.setObjectField(field: String?, value: Any?) = apply {
    XposedHelpers.setObjectField(this, field, value)
}

fun <T> T.setBooleanField(field: String?, value: Boolean) = apply {
    XposedHelpers.setBooleanField(this, field, value)
}

inline fun XResources.hookLayout(
    id: Int,
    crossinline hooker: (XC_LayoutInflated.LayoutInflatedParam) -> Unit
) {
    try {
        hookLayout(id, object : XC_LayoutInflated() {
            override fun handleLayoutInflated(liparam: LayoutInflatedParam) {
                try {
                    hooker(liparam)
                } catch (e: Throwable) {
                    log(e)
                }
            }
        })
    } catch (e: Throwable) {
        log(e)
    }
}

inline fun XResources.hookLayout(
    pkg: String,
    type: String,
    name: String,
    crossinline hooker: (XC_LayoutInflated.LayoutInflatedParam) -> Unit
) {
    try {
        val id = getIdentifier(name, type, pkg)
        hookLayout(id, hooker)
    } catch (e: Throwable) {
        log(e)
    }
}

fun Class<*>.findFirstFieldByExactType(type: Class<*>): Field =
    XposedHelpers.findFirstFieldByExactType(this, type)

fun Class<*>.findFirstFieldByExactTypeOrNull(type: Class<*>?): Field? = runCatchingOrNull {
    XposedHelpers.findFirstFieldByExactType(this, type)
}

fun Any.getFirstFieldByExactType(type: Class<*>): Any? =
    javaClass.findFirstFieldByExactType(type).get(this)

@Suppress("UNCHECKED_CAST")
fun <T> Any.getFirstFieldByExactTypeAs(type: Class<*>) =
    javaClass.findFirstFieldByExactType(type).get(this) as? T

inline fun <reified T : Any> Any.getFirstFieldByExactType() =
    javaClass.findFirstFieldByExactType(T::class.java).get(this) as? T

fun Any.getFirstFieldByExactTypeOrNull(type: Class<*>?): Any? = runCatchingOrNull {
    javaClass.findFirstFieldByExactTypeOrNull(type)?.get(this)
}

@Suppress("UNCHECKED_CAST")
fun <T> Any.getFirstFieldByExactTypeOrNullAs(type: Class<*>?) =
    getFirstFieldByExactTypeOrNull(type) as? T

inline fun <reified T> Any.getFirstFieldByExactTypeOrNull() =
    getFirstFieldByExactTypeOrNull(T::class.java) as? T

fun ClassLoader.allClassesList(): List<String> {
    var classLoader = this
    while (classLoader !is BaseDexClassLoader) {
        if (classLoader.parent != null) classLoader = classLoader.parent
        else return emptyList()
    }
    return classLoader.getObjectField("pathList")
        ?.getObjectFieldAs<Array<Any>>("dexElements")
        ?.flatMap {
            it.getObjectField("dexFile")?.callMethodAs<Enumeration<String>>("entries")?.toList()
                .orEmpty()
        }.orEmpty()
}

inline fun ClassLoader.findDexClassLoader(): BaseDexClassLoader? {
    var classLoader = this
    while (classLoader !is BaseDexClassLoader) {
        if (classLoader.parent != null) classLoader = classLoader.parent
        else return null
    }
    return classLoader
}

inline fun <T> T?.isNull(crossinline isNull: () -> Unit):T? {
    if (this == null) isNull()
    return this
}

inline fun <T> T?.isNonNull(crossinline nonNull: (T) -> Unit): T? {
    if (this != null) nonNull(this)
    return this
}
