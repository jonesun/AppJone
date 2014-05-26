package com.jone.app.ui.setting.joneSettings;

import android.app.ActionBar;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.MenuItem;

import com.jone.app.R;

import java.util.List;

/**
 * Created by jone_admin on 14-1-7.
 */
public class JoneSettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("设置");
        }
        if(hasHeaders()){
//            ImageButton icon = new ImageButton(this);
//            icon.setBackgroundResource(R.drawable.ic_launcher);
//            setListFooter(icon);
//            setTitle("Setting");
        }
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        //super.onBuildHeaders(target);
        loadHeadersFromResource(R.xml.preference_headers, target);
        Header header = new Header();
        header.fragment = AdminSettingFragment.class.getCanonicalName();
        header.iconRes = R.drawable.icon;
        header.title = "管理员设置";
        header.summary = "管理员设置";
        target.add(header);
//        for(Header header : target){
//            Log.e("sssss", header.fragment + ", title: " + header.title + ", summary: " +header.summary + ", " );
//            header.fragment = AdminSettingFragment.class.getCanonicalName();
//            header.title = "管理员设置";
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
