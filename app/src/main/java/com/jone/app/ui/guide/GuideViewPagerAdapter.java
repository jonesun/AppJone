package com.jone.app.ui.guide;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.jone.app.Constants;
import com.jone.app.R;
import com.jone.app.ui.JoneMainActivity;
import com.jone.app.utils.Utils;

import java.util.List;

/**
 * Created by jone_admin on 14-3-7.
 */
public class GuideViewPagerAdapter extends PagerAdapter {
    private List<View> views;
    private Activity activity;
    public GuideViewPagerAdapter(List<View> views, Activity activity){
        this.views = views;
        this.activity = activity;
    }
    @Override //获取当前界面数
    public int getCount() {
        if(views != null){
            return views.size();
        }
        return 0;
    }

    @Override //判断是否由对象生成界面
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override //初始化position位置的界面
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position), 0);
        if(position == views.size() - 1){
            Button btn_start_go_home = (Button) container.findViewById(R.id.btn_start_go_home);
            btn_start_go_home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setGuided();
                }
            });
        }
        return views.get(position);
    }

    @Override //销毁position位置的界面
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        container.removeView(views.get(position));
    }

    private void setGuided(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        boolean isFirst = preferences.getBoolean(Constants.key_is_first_in, true);
        if(isFirst){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(Constants.key_is_first_in, false);
            editor.commit();
            activity.startActivity(new Intent(activity, JoneMainActivity.class));
        }
        activity.finish();
    }
}
