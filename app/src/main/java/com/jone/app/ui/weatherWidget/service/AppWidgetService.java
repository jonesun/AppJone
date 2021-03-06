package com.jone.app.ui.weatherWidget.service;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jone.app.App;
import com.jone.app.Constants;
import com.jone.app.R;
import com.jone.app.callbacks.CommonListener;
import com.jone.app.entities.WeatherInfo;
import com.jone.app.ui.weatherWidget.ui.WeatherWidget;
import com.jone.app.utils.Lunar;
import com.jone.app.utils.Utils;
import com.jone.app.utils.WeatherUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jone_admin on 13-12-30.
 */
public class AppWidgetService extends Service{
    private final static String TAG = AppWidgetService.class.getName();
    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateTime(System.currentTimeMillis());
        updateCalendar(Calendar.getInstance());
        App.getInstance().getHandler().post(runnableGetWeather);
        return START_STICKY;
        //return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long currentTime = System.currentTimeMillis() + 1000;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(currentTime));
            updateTime(currentTime);
            if(WeatherUtil.isUpdateWeather(AppWidgetService.this, calendar)){
                //更新天气
                App.getInstance().getHandler().post(runnableGetWeather);//更新天气
            }
            if(WeatherUtil.isUpdateCalendar(calendar)){
                updateCalendar(calendar);
            }
        }
    };
    private void updateTime(long currentTime){
        //更新时间
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_widget_weather);
        remoteViews.setTextViewText(R.id.tx_weather_time, geFormatTime(currentTime));
        ComponentName componentName = new ComponentName(this, WeatherWidget.class); //相当于获得所有本程序创建的appwidget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        appWidgetManager.updateAppWidget(componentName, remoteViews);
    }

    private void updateCalendar(Calendar calendar){
        RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.layout_widget_weather);
        String calendarInfo = "周" + Utils.weekNum2string(calendar.get(Calendar.DAY_OF_WEEK)) + " " + (calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日";
        remoteViews.setTextViewText(R.id.txt_calendar, calendarInfo);
        try {
            System.out.println(new Lunar().getLunarTime(calendar.getTimeInMillis()));
            remoteViews.setTextViewText(R.id.txt_calendar_lunar, new Lunar().getLunarTime(calendar.getTimeInMillis()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ComponentName componentName = new ComponentName(this, WeatherWidget.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        appWidgetManager.updateAppWidget(componentName, remoteViews);
    }

    private String geFormatTime(long currentTime){
        if(DateFormat.is24HourFormat(AppWidgetService.this)){
            return Utils.formatDataTime(currentTime, "HH:mm");
        }else {
            return Utils.formatDataTime(currentTime, "hh:mm");
        }
    }

    private Runnable runnableGetWeather = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "getWeatherRunnable");
            if(Utils.isNetworkAlive()){
                WeatherUtil.getLocationCityWeatherInfo(new WeatherUtil.WeatherInfoListener() {
                    @Override
                    public void onResponse(WeatherInfo weatherInfo) {
                        if(weatherInfo != null){
                            updateWeather(weatherInfo);
                        }else {
                            Toast.makeText(AppWidgetService.this, "天气更新失败, 请检查网络", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else {
                Toast.makeText(AppWidgetService.this, "天气更新失败, 请检查网络", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void updateWeather(WeatherInfo weatherInfo){
        if(weatherInfo != null){
            RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.layout_widget_weather);
            remoteViews.setTextViewText(R.id.tx_weather_city, weatherInfo.getCity());
            remoteViews.setTextViewText(R.id.tx_weather_template, weatherInfo.getTemp1() + "-" + weatherInfo.getTemp2());
            remoteViews.setTextViewText(R.id.tx_weather_weather, weatherInfo.getWeather());
            remoteViews.setImageViewResource(R.id.im_weather_icon, WeatherUtil.getWeatherIconByWeather(weatherInfo.getWeather()));
            ComponentName componentName = new ComponentName(this, WeatherWidget.class);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            appWidgetManager.updateAppWidget(componentName, remoteViews);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
