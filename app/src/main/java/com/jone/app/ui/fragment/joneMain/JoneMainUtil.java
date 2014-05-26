package com.jone.app.ui.fragment.joneMain;

import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.jone.app.Constants;
import com.jone.app.R;
import com.jone.app.callbacks.CommonListener;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

/**
 * Created by jone_admin on 2014/4/23.
 */
public class JoneMainUtil {
    private Button button_ok;
    public PopupWindow makePopupWindow(final PopupWindow window, final Context cx, int width, int height, final CommonListener listener){
        View contentView = LayoutInflater.from(cx).inflate(R.layout.cities_layout, null);
        window.setContentView(contentView);

        final WheelView country = (WheelView) contentView.findViewById(R.id.country);
        country.setViewAdapter(new CountryAdapter(cx));

        final String cities[][] = Constants.CITIES;
        final WheelView city = (WheelView) contentView.findViewById(R.id.city);

        country.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateCities(cx, city, cities, newValue);
            }
        });

        country.setCurrentItem(9);
        city.setCurrentItem(0);

        // 点击事件处理
        button_ok = (Button)contentView.findViewById(R.id.button_ok);
        button_ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                String info = Constants.PROVINCES[country.getCurrentItem()] + "-" +
//                        Constants.CITIES[country.getCurrentItem()][city.getCurrentItem()];
//                System.out.println("info: " + info);
                listener.onExecute(Constants.CITIES[country.getCurrentItem()][city.getCurrentItem()]);
                window.dismiss(); // 隐藏
            }
        });
        window.setWidth(width);
        window.setHeight(height/2);

        // 设置PopupWindow外部区域是否可触摸
        window.setFocusable(true); //设置PopupWindow可获得焦点
        window.setTouchable(true); //设置PopupWindow可触摸
        window.setOutsideTouchable(true); //设置非PopupWindow区域可触摸
        return window;
    }

    /**
     * Updates the city wheel
     */
    private void updateCities(Context context, WheelView city, String cities[][], int index) {
        ArrayWheelAdapter<String> adapter =
                new ArrayWheelAdapter<String>(context, cities[index]);
        adapter.setTextSize(18);
        city.setViewAdapter(adapter);
        city.setCurrentItem(cities[index].length / 2);
    }

    /**
     * Adapter for countries
     */
    private class CountryAdapter extends AbstractWheelTextAdapter {
        // Countries names
        private String countries[] = Constants.PROVINCES;
        /**
         * Constructor
         */
        protected CountryAdapter(Context context) {
            super(context, R.layout.country_layout, NO_RESOURCE);

            setItemTextResource(R.id.country_name);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return countries.length;
        }

        @Override
        protected CharSequence getItemText(int index) {
            return countries[index];
        }
    }
}
