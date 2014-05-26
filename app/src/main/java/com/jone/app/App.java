package com.jone.app;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.jone.app.services.JoneShowFloatLayoutService;
import com.jone.app.dbHelper.JoneORMLiteHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.common.VolleyCommon;
import core.common.view.CommonView;

/**
 * Created by jone_admin on 14-1-3.
 */
public class App extends Application {
    private static App instance;
    private Handler handler;
    private VolleyCommon volleyCommon;
    private SharedPreferences sharedPreferences;

    public static App getInstance() {
        return instance;
    }

    private static JoneORMLiteHelper joneORMLiteHelper;

    public VolleyCommon getVolleyCommon() {
        return volleyCommon;
    }

    public static List<String> mEmoticons = new ArrayList<String>();
    public static Map<String, Integer> mEmoticonsId = new HashMap<String, Integer>();
    public static List<String> mEmoticons_Zem = new ArrayList<String>();
    public static List<String> mEmoticons_Zemoji = new ArrayList<String>();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        handler = new Handler();
        volleyCommon = new VolleyCommon(getApplicationContext());
        CommonView.alwaysShowActionBarOverflow(getApplicationContext());//在具有硬件菜单键设备上依然显示Action bar overflow
        init();
    }

    private void init(){
        for (int i = 1; i < 64; i++) {
            String emoticonsName = "[zem" + i + "]";
            int emoticonsId = getResources().getIdentifier("zem" + i,
                    "drawable", getPackageName());
            mEmoticons.add(emoticonsName);
            mEmoticons_Zem.add(emoticonsName);
            mEmoticonsId.put(emoticonsName, emoticonsId);
        }
        for (int i = 1; i < 59; i++) {
            String emoticonsName = "[zemoji" + i + "]";
            int emoticonsId = getResources().getIdentifier("zemoji_e" + i,
                    "drawable", getPackageName());
            mEmoticons.add(emoticonsName);
            mEmoticons_Zemoji.add(emoticonsName);
            mEmoticonsId.put(emoticonsName, emoticonsId);
        }
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(App.this);
        boolean isOpenFloatWindow = getSharedPreferences().getBoolean("chP_open_float_window", true);
        if(isOpenFloatWindow){
            startService(new Intent(getInstance(), JoneShowFloatLayoutService.class));
        }
    }

    public Handler getHandler() {
        return handler;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }
}
