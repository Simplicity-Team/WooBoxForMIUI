package com.lt2333.simplicitytools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.reboot_ui) {
            String[] commad = new String[]{"killall com.android.systemui"};
            ShellUtils.execCommand(commad, true);
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        checkEdXposed();
    }

    @SuppressLint("WorldReadableFiles")
    private void checkEdXposed() {
        try {
            // getSharedPreferences will hooked by LSPosed and change xml file path to /data/misc/edxp**
            // will not throw SecurityException
            //noinspection deprecation
            getSharedPreferences("config", Context.MODE_WORLD_READABLE);
        } catch (SecurityException exception) {
            new AlertDialog.Builder(this)
                    .setMessage("您似乎正在使用过时的 LSPosed 版本或 LSPosed 未激活，请更新 LSPosed 或者激活后再试。")
                    .show();
        }
    }


    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            getPreferenceManager().setSharedPreferencesName("config");
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            findPreference("verison").setSummary(BuildConfig.VERSION_NAME+"("+BuildConfig.BUILD_TYPE+")");
        }


        @Override
        public boolean onPreferenceTreeClick(Preference preference) {


            if (preference.getKey().equals("opensource")){
                try {
                    Uri uri = Uri.parse("https://github.com/LittleTurtle2333/Simplicity_Tools_Xposed");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "访问失败", Toast.LENGTH_SHORT).show();
                }
            }

            if (preference.getKey().equals("dev_coolapk")) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("coolmarket://u/883441")));
                    Toast.makeText(getActivity(), "乌堆小透明：靓仔，点个关注吧！", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "本机未安装酷安应用", Toast.LENGTH_SHORT).show();
                    Uri uri = Uri.parse("http://www.coolapk.com/u/883441");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }


            return super.onPreferenceTreeClick(preference);
        }
    }

}