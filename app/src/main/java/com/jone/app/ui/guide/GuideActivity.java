package com.jone.app.ui.guide;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jone.app.R;
import com.jone.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends Activity implements ViewPager.OnPageChangeListener{

    private ViewPager viewPager;
    private GuideViewPagerAdapter guideViewPagerAdapter;
    private List<View> views;

    private ImageView[] dots; //底部小点图片

    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setScreenOrientation(this);
        setContentView(R.layout.activity_guide);

        initViews();
        initDots();
    }

    private void initViews(){
        LayoutInflater inflater = LayoutInflater.from(this);

        views = new ArrayList<>();
        views.add(inflater.inflate(R.layout.view_guide_one, null));
        views.add(inflater.inflate(R.layout.view_guide_two, null));
        views.add(inflater.inflate(R.layout.view_guide_three, null));
        views.add(inflater.inflate(R.layout.view_guide_four, null));

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        guideViewPagerAdapter = new GuideViewPagerAdapter(views, GuideActivity.this);
        viewPager.setAdapter(guideViewPagerAdapter);
        viewPager.setOnPageChangeListener(this);
    }

    private void initDots(){
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        dots = new ImageView[views.size()];
        for(int i = 0; i < views.size(); i++){
            dots[i] = (ImageView)ll.getChildAt(i);
            dots[i].setEnabled(true);
        }
        currentIndex = 0;
        dots[currentIndex].setEnabled(false);
    }

    private void setCurrentDot(int position){
        if(position < 0 || (position > views.size() - 1) || currentIndex == position){
            return;
        }
        dots[position].setEnabled(false);
        dots[currentIndex].setEnabled(true);
        currentIndex = position;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override// 设置底部小点选中状态
    public void onPageSelected(int position) {
        setCurrentDot(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
