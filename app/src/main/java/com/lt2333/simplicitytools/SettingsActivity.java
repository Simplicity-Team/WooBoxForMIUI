package com.lt2333.simplicitytools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
                    .setMessage("不支持")
                    .show();
        }
    }


    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            getPreferenceManager().setSharedPreferencesName("config");
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }

    }

}