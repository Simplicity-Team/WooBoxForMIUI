package com.lt2333.simplicitytools.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.lt2333.simplicitytools.BuildConfig
import com.lt2333.simplicitytools.R
import com.lt2333.simplicitytools.util.ShellUtils

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.settings, SettingsFragment())
                    .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        checkLSPosed()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    @SuppressLint("WorldReadableFiles")
    private fun checkLSPosed() {
        try {
            getSharedPreferences("config", MODE_WORLD_READABLE)
        } catch (exception: SecurityException) {
            AlertDialog.Builder(this)
                    .setMessage("您似乎正在使用过时的 LSPosed 版本或 LSPosed 未激活，请更新 LSPosed 或者激活后再试。")
                    .show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.reboot -> {
                val commad = arrayOf("reboot")
                ShellUtils.execCommand(commad, true)
            }
            R.id.reboot_ui -> {
                val commad = arrayOf("killall com.android.systemui")
                ShellUtils.execCommand(commad, true)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            preferenceManager.sharedPreferencesName = "config"
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            findPreference<Preference>("verison")!!.summary =
                    BuildConfig.VERSION_NAME + "(" + BuildConfig.BUILD_TYPE + ")"
        }

        override fun onPreferenceTreeClick(preference: Preference?): Boolean {

            when (preference!!.key) {
                "opensource" -> {
                    try {
                        val uri =
                                Uri.parse("https://github.com/LittleTurtle2333/Simplicity_Tools_Xposed")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(activity, "访问失败", Toast.LENGTH_SHORT).show()
                    }
                }
                "issues" -> {
                    try {
                        val uri =
                                Uri.parse("https://github.com/LittleTurtle2333/Simplicity_Tools_Xposed/issues")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(activity, "访问失败", Toast.LENGTH_SHORT).show()
                    }
                }
                "dev_coolapk" -> {
                    try {
                        startActivity(
                                Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("coolmarket://u/883441")
                                )
                        )
                        Toast.makeText(activity, "乌堆小透明：靓仔，点个关注吧！", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(activity, "本机未安装酷安应用", Toast.LENGTH_SHORT).show()
                        val uri = Uri.parse("http://www.coolapk.com/u/883441")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    }
                }
            }
            return super.onPreferenceTreeClick(preference)
        }
    }
}