package com.jone.app.ui.activities.fullScreen.splash;

import com.jone.app.Constants;
import com.jone.app.ui.JoneMainActivity;
import com.jone.app.ui.activities.fullScreen.splash.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jone.app.R;
import com.jone.app.ui.guide.GuideActivity;
import com.jone.app.utils.FestivalUtil;
import com.jone.app.utils.Lunar;
import com.jone.app.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;

public class FullSplashActivity extends Activity {
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    private SystemUiHider mSystemUiHider;

    private boolean isFirstIn = false;
    private static final int GO_HOME = 1000;
    private static final int GO_GUIDE = 1001;

    private static final long SPLASH_DELAY_MILLIS = 3000; // 延迟3秒

    private Calendar calendar;
    private FestivalUtil festivalUtil;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GO_HOME:
                    goHome();
                    break;
                case GO_GUIDE:
                    goGuide();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setScreenOrientation(this);
        setContentView(R.layout.activity_full_splash);
        View contentView = findViewById(R.id.fullscreen_content);
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        calendar = Calendar.getInstance();
        festivalUtil = new FestivalUtil(calendar.get(Calendar.YEAR), (calendar.get(Calendar.MONTH) + 1), calendar.get(Calendar.DAY_OF_MONTH));
        showChineseZodiac(); //显示生肖
        showDate(); //显示时间
        showFestival(); //显示节日
        init();
    }

    private void init(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(FullSplashActivity.this);
        isFirstIn = sharedPreferences.getBoolean(Constants.key_is_first_in, true);
        if (!isFirstIn) {
            handler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
        } else {
            handler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
        }
    }

    private void showChineseZodiac(){
        int chinese_zodiac_ids[] = new int[]{
                R.drawable.chinese_zodiac_0, R.drawable.chinese_zodiac_1, R.drawable.chinese_zodiac_2, R.drawable.chinese_zodiac_3,
                R.drawable.chinese_zodiac_4, R.drawable.chinese_zodiac_5, R.drawable.chinese_zodiac_6, R.drawable.chinese_zodiac_7,
                R.drawable.chinese_zodiac_8, R.drawable.chinese_zodiac_9, R.drawable.chinese_zodiac_10, R.drawable.chinese_zodiac_11,
        };
        ImageView im_chinese_zodiac = (ImageView) findViewById(R.id.im_chinese_zodiac);
        im_chinese_zodiac.setImageResource(chinese_zodiac_ids[Lunar.getChineseZodiacId()]);
    }

    private void showDate(){
        TextView txt_date = (TextView) findViewById(R.id.txt_date);
        txt_date.setText(calendar.get(Calendar.YEAR) + "年" +
                (calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日" + "周" + FestivalUtil.getWeekDay(calendar.get(Calendar.DAY_OF_WEEK) - 1)  +" 农历:" + festivalUtil.getChineseDate());
    }

    private void showFestival(){
        TextView txt_festival = (TextView) findViewById(R.id.txt_festival);
        ArrayList<String> fest = festivalUtil.getFestVals();
        StringBuffer festival = new StringBuffer();
//            String festival = "";
        if(fest.size() > 0){
            for(String str:fest){
                festival.append(str + "(" + FestivalUtil.getPinYin(str).trim() + ")" +" ");
//                    festival += str + " ";
                System.out.println(str + "(" + FestivalUtil.getPinYin(str, "_").trim() + ")");
            }
            txt_festival.setText("今天是: " + festival);
        }
    }

    public void goHome(){
        startActivity(new Intent(FullSplashActivity.this, JoneMainActivity.class));
        finish();
    }

    public void goGuide(){
        startActivity(new Intent(FullSplashActivity.this, GuideActivity.class));
        finish();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(0);
    }

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
