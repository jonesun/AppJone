package com.jone.app.ui.setting.joneSettings;

import android.app.DownloadManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.util.Log;
import android.widget.Toast;

import com.jone.app.R;
import com.jone.app.broadcasts.JoneDeviceAdminReceiver;
import com.jone.app.utils.Utils;

import core.common.DownloadManagerUtil;
import core.common.upgrade.upgradeAndroid.UpgradeAndroid;

/**
 * Created by jone_admin on 14-1-7.
 */
public class AdminSettingFragment extends PreferenceFragment {
    private DevicePolicyManager devicePolicyManager;
    private ComponentName componentName;
    private CheckBoxPreference is_start_device_manager;
    private EditTextPreference edit_lock_screen_password;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_admin);


        //动态添加Preference
        Preference preference = new Preference(getActivity());
        preference.setTitle("测试");
        preference.setKey("test");
        getPreferenceScreen().addPreference(preference);

        devicePolicyManager = (DevicePolicyManager) getActivity().getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(getActivity(), JoneDeviceAdminReceiver.class);
        is_start_device_manager = (CheckBoxPreference) findPreference("is_start_device_manager");
        controlDeviceManager(is_start_device_manager.isChecked());
        is_start_device_manager.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                controlDeviceManager(is_start_device_manager.isChecked());
                return true;
            }
        });
        edit_lock_screen_password = (EditTextPreference) findPreference("lock_screen_password");
        edit_lock_screen_password.setSummary(edit_lock_screen_password.getText());
        edit_lock_screen_password.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                edit_lock_screen_password.setSummary(o.toString());
                return true; //必须返回true
            }
        });
        Preference pr_lock_screen = findPreference("lock_screen");
        pr_lock_screen.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                sysLock(edit_lock_screen_password.getText());
                return false;
            }
        });

    }

    private void startLogDebugTool(){
        try{
            ComponentName toActivity = new ComponentName("com.readystatesoftware.ghostlog", "com.readystatesoftware.ghostlog.MainActivity");
            Intent intent = new Intent();
            intent.setComponent(toActivity);
            startActivity(intent);
        }catch (Exception ex){
            Log.e("日志调试工具", "", ex);
            Toast.makeText(getActivity(), "打开失败", Toast.LENGTH_LONG).show();
        }
    }

    private void controlDeviceManager(boolean isStart){
        if(isStart){
            startDeviceManager();
        }else {
            stopDeviceManager();
        }
    }

    private void startDeviceManager(){
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);

        //权限列表
        //EXTRA_DEVICE_ADMIN参数中说明了用到哪些权限
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);

        //描述
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "这是startDeviceManager");
        startActivityForResult(intent, 0);
    }

    private void stopDeviceManager(){
        if(devicePolicyManager.isAdminActive(componentName)){
            devicePolicyManager.removeActiveAdmin(componentName);
        }
    }

    private void sysLock(String password){
        if(devicePolicyManager.isAdminActive(componentName)){
            if(password != null){
                devicePolicyManager.resetPassword(password, 0);
            }
            devicePolicyManager.lockNow();
        }
    }
}
