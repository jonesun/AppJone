package com.jone.app.ui.fragment.actionBarTabs;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.jone.app.R;

public class ActionBarTabFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private int position;
    public static ActionBarTabFragment newInstance(int position) {
        ActionBarTabFragment fragment = new ActionBarTabFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, position);
        fragment.setArguments(args);
        return fragment;
    }
    public ActionBarTabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_action_bar_tab, container, false);
        WebView webView = (WebView) rootView.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true); // 允许使用javascript脚本语言
        webView.getSettings().setBuiltInZoomControls(true); // 设置可以缩放
        webView.setBackgroundColor(Color.TRANSPARENT);
        // 设置javaScript可用于操作MainActivity类
//        webView.addJavascriptInterface(this,TAG);
        switch (position){
            case 0:
                webView.loadUrl("file:///android_asset/chart/3dchart.html");
                break;
            case 1:
                webView.loadUrl("file:///android_asset/chart/line_chart.html");
                break;
            case 2:
                webView.loadUrl("file:///android_asset/chart/2dpiechart.html");
                break;
            default:
                webView.loadUrl("file:///android_asset/chart/2dpiechart.html");
                break;
        }

        return rootView;
    }
}
