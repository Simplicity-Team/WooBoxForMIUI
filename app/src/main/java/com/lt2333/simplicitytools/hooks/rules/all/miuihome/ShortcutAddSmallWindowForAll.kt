package com.lt2333.simplicitytools.hooks.rules.all.miuihome

import android.app.AndroidAppHelper
import android.content.ComponentName
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current
import com.highcapable.yukihookapi.hook.factory.field
import com.lt2333.simplicitytools.R
import com.lt2333.simplicitytools.utils.hasEnable

object ShortcutAddSmallWindowForAll : YukiBaseHooker() {
    override fun onHook() = hasEnable("miuihome_shortcut_add_small_window") {
        val mViewDarkModeHelper = "com.miui.home.launcher.util.ViewDarkModeHelper".toClass()
        val mSystemShortcutMenu = "com.miui.home.launcher.shortcuts.SystemShortcutMenu".toClass()
        val mSystemShortcutMenuItem = "com.miui.home.launcher.shortcuts.SystemShortcutMenuItem".toClass()
        val mAppShortcutMenu = "com.miui.home.launcher.shortcuts.AppShortcutMenu".toClass()
        val mShortcutMenuItem = "com.miui.home.launcher.shortcuts.ShortcutMenuItem".toClass()
        val mAppDetailsShortcutMenuItem = "com.miui.home.launcher.shortcuts.SystemShortcutMenuItem\$AppDetailsShortcutMenuItem".toClass()
        val mActivityUtilsCompat = "com.miui.launcher.utils.ActivityUtilsCompat".toClass()

        mViewDarkModeHelper.hook {
            injectMember {
                method { name = "onConfigurationChanged" }.all()
                afterHook {
                    mSystemShortcutMenuItem.current {
                        method { name = "createAllSystemShortcutMenuItems" }.call()
                    }
                }
            }
        }
        mShortcutMenuItem.hook {
            injectMember {
                method { name = "getShortTitle" }
                afterHook {
                    if (this.result == "应用信息") {
                        this.result = "信息"
                    }
                }
            }
        }
        mAppDetailsShortcutMenuItem.hook {
            injectMember {
                method {
                    name = "lambda\$getOnClickListener$0"
                    paramCount = 2
                }
                beforeHook {
                    val obj = args[0]
                    val view: View = args[1] as View
                    val mShortTitle = obj?.current {
                        method { name = "getShortTitle" }.invoke<CharSequence>()
                    }

                    if (mShortTitle == moduleAppResources.getString(R.string.miuihome_shortcut_add_small_window_title)) {
                        this.result = null
                        val intent = Intent()
                        val mComponentName = obj.current {
                            method { name = "getComponentName" }.call()
                        } as ComponentName
                        intent.action = "android.intent.action.MAIN"
                        intent.addCategory("android.intent.category.LAUNCHER")
                        intent.component = mComponentName
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        val callStaticMethod = mActivityUtilsCompat.current {
                            method {
                                name = "makeFreeformActivityOptions"
                                paramCount = 2
                            }.call(
                                view.context, mComponentName.packageName
                            )
                        }
                        if (callStaticMethod != null) {
                            view.context.startActivity(intent, callStaticMethod.current {
                                method { name = "toBundle" }.call()
                            } as Bundle)
                        }
                    }
                }
            }
        }
        mSystemShortcutMenu.hook {
            injectMember {
                method {
                    name = "getMaxShortcutItemCount"
                }.all()
                afterHook {
                    this.result = 5
                }
            }
        }
        mAppShortcutMenu.hook {
            injectMember {
                method {
                    name = "getMaxShortcutItemCount"
                }.all()
                afterHook {
                    this.result = 5
                }
            }
        }
        mSystemShortcutMenuItem.hook {
            injectMember {
                method { name = "createAllSystemShortcutMenuItems" }.all()
                afterHook {
                    val isDarkMode =
                        AndroidAppHelper.currentApplication().applicationContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
                    val mAllSystemShortcutMenuItems = mSystemShortcutMenuItem.field {
                        name = "sAllSystemShortcutMenuItems"
                    }.get().cast<Collection<Any>>()
                    val mSmallWindowInstance = mAppDetailsShortcutMenuItem.newInstance()
                    mSmallWindowInstance.current {
                        method {
                            name = "setShortTitle"
                        }.call(moduleAppResources.getString(R.string.miuihome_shortcut_add_small_window_title))
                    }
                    mSmallWindowInstance.current {
                        method {
                            name = "setIconDrawable"
                        }.call(if (isDarkMode) moduleAppResources.getDrawable(R.drawable.ic_small_window_dark) else moduleAppResources.getDrawable(R.drawable.ic_small_window_light))
                    }
                    val sAllSystemShortcutMenuItems = ArrayList<Any>()
                    sAllSystemShortcutMenuItems.add(mSmallWindowInstance)
                    mAllSystemShortcutMenuItems?.let { sAllSystemShortcutMenuItems.addAll(it) }
                    mSystemShortcutMenuItem.field { name = "sAllSystemShortcutMenuItems" }.get().set(sAllSystemShortcutMenuItems)
                }
            }
        }


    }
}