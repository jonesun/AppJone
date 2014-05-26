package com.jone.app.ui.fragment.joneMain;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.jone.app.Constants;
import com.jone.app.R;
import com.jone.app.asyncTaskLoader.CustomListAsyncTaskLoader;
import com.jone.app.callbacks.CommonListener;
import com.jone.app.entities.WeatherInfo;
import com.jone.app.logic.ControlLogic;
import com.jone.app.ui.JoneMainActivity;
import com.jone.app.ui.adpater.ControlBeanAdapter;
import com.jone.app.ui.fragment.JoneBaseFragment;
import com.jone.app.utils.Utils;
import com.jone.app.utils.WeatherUtil;
import com.jone.demo.Demo;
import com.jone.demo.IDemo;
import com.jone.demo.JoneInvocationHandler;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

/**
 * Created by jone_admin on 14-2-18.
 */
public class JoneMainFragment extends JoneBaseFragment {
    private static final String TAG = JoneMainFragment.class.getSimpleName();
    public static final int loaderId = 001001;
    private TextSwitcher textSwitcherNews;
    private GridView gridViewCenter;
    private ControlBeanAdapter adapter;
    private ControlLogic controlLogic;

    private LoaderManager loaderManager;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0://更新天气
                    updateWeatherUI((WeatherInfo) msg.obj);
                    break;
            }
        }
    };
    private Runnable showNewsRunnable;
    private int currentNewsIndex = 0;
    private String news[] = new String[]{
            "你若想得到这世界最好的东西，先提供这世界最好的你。",
            "如果你没有梦想，那么你只能为别人的梦想打工。",
            "你的父母仍在为你打拼，这就是你今天坚强的理由。",
            "什么是坚持?就是每天告诉自己，再坚持一天。",
            "祝新的一年里马到成功！",
    };

    private BroadcastReceiver networkChangeBroadcastReceiver;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controlLogic = new ControlLogic(getActivity());
        loaderManager = getLoaderManager();
        currentNewsIndex = (int)(Math.random()*news.length);
        IDemo demo = (IDemo) JoneInvocationHandler.newInstance(new Demo());
        demo.getResult(22222);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_jone_main, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        bindBroadcast();
//        System.out.println("getWeatherURLByCityName: " +  WeatherUtil.getWeatherURLByCityName(getActivity(), "北京"));
    }

    private TextView txtLocation;
    private TextView txtWeather;
    private TextView txtChangeCity;
    private ImageView imWeatherIcon;
    int width,height;
    private PopupWindow popupWindow;
    private JoneMainUtil joneMainUtil;
    private void initViews(final View rootView){
        txtLocation = (TextView) rootView.findViewById(R.id.txtLocation);
        txtWeather = (TextView) rootView.findViewById(R.id.txtWeather);
        imWeatherIcon = (ImageView) rootView.findViewById(R.id.imWeatherIcon);

        joneMainUtil = new JoneMainUtil();
        popupWindow = new PopupWindow(getActivity());

        // 获取屏幕的高度和宽度
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();

        txtChangeCity = (TextView) rootView.findViewById(R.id.txtChangeCity);
        txtChangeCity.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        txtChangeCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 显示 popupWindow
                joneMainUtil.makePopupWindow(popupWindow, getActivity(), width, height , new CommonListener() {
                    @Override
                    public Object onExecute(Object o) {
                        setWeatherInfoByCity((String) o);
                        return null;
                    }
                });
                int[] xy = new int[2];
                rootView.getLocationOnScreen(xy);
                popupWindow.showAtLocation(rootView,Gravity.CENTER|Gravity.BOTTOM, 0, -height);
            }
        });

        textSwitcherNews = (TextSwitcher) rootView.findViewById(R.id.textSwitcherNews);
        initTextSwitcherNews();

        gridViewCenter = (GridView) rootView.findViewById(R.id.gridViewCenter);
        adapter = new ControlBeanAdapter(getActivity(), R.layout.item_control_gridview);
        gridViewCenter.setAdapter(adapter);
        loaderManager.initLoader(loaderId, null, callbacks);
    }

    private void setWeatherInfo(){
        if(Utils.isNetworkAlive()){
            imWeatherIcon.setVisibility(View.INVISIBLE);
            txtLocation.setText("loading...");
            WeatherUtil.getLocationCityWeatherInfo(new WeatherUtil.WeatherInfoListener() {
                @Override
                public void onResponse(WeatherInfo weatherInfo) {
                    Message message = new Message();
                    message.what = 0;
                    message.obj = weatherInfo;
                    handler.sendMessage(message);
                }
            });
        }else {
            txtLocation.setText("当前城市: 未知");
            txtWeather.setText("天气获取失败");
        }
    }

    private void setWeatherInfoByCity(String city){
        if(Utils.isNetworkAlive()){
            imWeatherIcon.setVisibility(View.INVISIBLE);
            txtChangeCity.setVisibility(View.INVISIBLE);
            txtLocation.setText("loading...");
            WeatherUtil.getWeatherInfoByCity(city, new WeatherUtil.WeatherInfoListener() {
                @Override
                public void onResponse(WeatherInfo weatherInfo) {
                    Message message = new Message();
                    message.what = 0;
                    message.obj = weatherInfo;
                    handler.sendMessage(message);
                }
            });
        }else {
            txtWeather.setText("天气获取失败");
        }
    }

    private void updateWeatherUI(WeatherInfo weatherInfo){
        imWeatherIcon.setVisibility(View.VISIBLE);
        txtChangeCity.setVisibility(View.VISIBLE);
        if(weatherInfo != null){
            txtChangeCity.setVisibility(View.VISIBLE);
            txtLocation.setText("当前城市: " + weatherInfo.getCity());
            txtWeather.setText("温度: " + weatherInfo.getTemp1() + "-" + weatherInfo.getTemp2() + "\n"
                    + "天气: " + weatherInfo.getWeather() + "\n"
                    + "发布时间: " + weatherInfo.getPtime());
            imWeatherIcon.setImageResource(WeatherUtil.getWeatherIconByWeather(weatherInfo.getWeather()));
        }else {
            txtLocation.setText("当前城市: 未知");
            txtChangeCity.setVisibility(View.INVISIBLE);
            txtWeather.setText("天气获取失败");
            imWeatherIcon.setVisibility(View.INVISIBLE);
        }
    }

    private void bindBroadcast(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION); //网络状态变化
        networkChangeBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
//                Log.d(TAG, "网络状态变化");
                setWeatherInfo();
            }
        };
        getActivity().registerReceiver(networkChangeBroadcastReceiver, intentFilter);
    }

    private void unBindBroadcast(){
        if(networkChangeBroadcastReceiver != null){
            getActivity().unregisterReceiver(networkChangeBroadcastReceiver);
        }
    }


    private void initTextSwitcherNews(){
        textSwitcherNews.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView tv = new TextView(getActivity());
                tv.setTextSize(16);
                tv.setTextColor(getResources().getColor(android.R.color.darker_gray));
                tv.setGravity(Gravity.CENTER);
                return tv;
            }
        });

        // 设置淡入淡出的动画效果
        Animation in = AnimationUtils.loadAnimation(getActivity(),
                android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(getActivity(),
                android.R.anim.slide_out_right);
        textSwitcherNews.setInAnimation(in);
        textSwitcherNews.setOutAnimation(out);

        showNewsRunnable = new Runnable() {
            @Override
            public void run() {
                if (currentNewsIndex > news.length - 1) {
                    currentNewsIndex = 0;
                }
                textSwitcherNews.setText(news[currentNewsIndex]);
                currentNewsIndex++;
                handler.postDelayed(this, 5000);
            }
        };
        handler.postDelayed(showNewsRunnable, 500);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((JoneMainActivity) activity).onSectionAttached(0);
    }

    @Override
    public void onShow() {
        super.onShow();
        ((JoneMainActivity) getActivity()).onSectionAttached(0);
    }

    private LoaderManager.LoaderCallbacks<List> callbacks = new LoaderManager.LoaderCallbacks<List>() {
        @Override
        public Loader<List> onCreateLoader(int i, Bundle bundle) {
            return new CustomListAsyncTaskLoader(new CommonListener() {
                @Override
                public Object onExecute(Object o) {
                    return controlLogic.getControlBeans();
                }
            });
        }

        @Override
        public void onLoadFinished(Loader<List> listLoader, List list) {
            adapter.setData(list);
        }

        @Override
        public void onLoaderReset(Loader<List> listLoader) {
            adapter.clear();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(loaderManager.getLoader(loaderId) != null){
            loaderManager.destroyLoader(loaderId);
        }
        if(showNewsRunnable != null){
            handler.removeCallbacks(showNewsRunnable);
        }
        unBindBroadcast();
    }
}
