package com.jone.app.ui.setting.joneSettings;

import android.app.WallpaperManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.jone.app.R;
import com.jone.app.services.JoneShowFloatLayoutService;

/**
 * Created by jone_admin on 14-1-24.
 */
public class UserSettingFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{
    private SharedPreferences sharedPreferences;
    private Preference pre_test;
    private CheckBoxPreference chP_open_float_window;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_user_setting);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Preference pre_change_wallpaper = findPreference("pre_change_wallpaper");
        if (pre_change_wallpaper != null) {
            pre_change_wallpaper.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent liveWallpapers = new Intent();
                    liveWallpapers.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
                    startActivityForResult(liveWallpapers, 0);
                    return false;
                }
            });
        }

        chP_open_float_window = (CheckBoxPreference) findPreference("chP_open_float_window");
        if (chP_open_float_window != null) {
            chP_open_float_window.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    if ((boolean) o) {
                        getActivity().startService(new Intent(getActivity(), JoneShowFloatLayoutService.class));
                    }else {
                        getActivity().stopService(new Intent(getActivity(), JoneShowFloatLayoutService.class));
                    }
                    return true;
                }
            });
        }

        pre_test = findPreference("pre_test");
        pre_test.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("pre_test", System.currentTimeMillis() + "");
                editor.commit();
                return false;
            }
        });
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.e("sssssss", "数据变化: " + s);
        if(s.equals("pre_test")){
             pre_test.setSummary(sharedPreferences.getString(s, ""));
        }else if(s.equals("chP_open_float_window")){
            boolean isCheck = sharedPreferences.getBoolean("chP_open_float_window", false);
            if(isCheck != chP_open_float_window.isChecked()){
                chP_open_float_window.setChecked(isCheck);
            }
        }
    }
}
