package com.jone.app.ui.fragment.joneMain;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jone.app.R;
import com.jone.app.dao.WeatherCityDao;
import com.jone.app.entities.WeatherCity;
import com.jone.app.opengl.HomeGLRenderer;
import com.jone.app.ui.JoneMainActivity;
import com.jone.app.ui.dialogs.ContactOurDialog;
import com.jone.app.ui.fragment.JoneBaseFragment;
import com.jone.app.ui.guide.GuideActivity;

import core.common.Version;

/**
 * Created by jone_admin on 14-3-18.
 */
public class AboutFragment extends JoneBaseFragment {
    private static final String TAG = AboutFragment.class.getSimpleName();
    private ApplicationInfo applicationInfo;
    private GLSurfaceView glsurfaceview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applicationInfo = getActivity().getApplicationInfo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        glsurfaceview = (GLSurfaceView) view.findViewById(R.id.glsurfaceview);
        HomeGLRenderer homeGLRenderer = new HomeGLRenderer(getActivity());
        glsurfaceview.setZOrderOnTop(true);
        glsurfaceview.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        glsurfaceview.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        glsurfaceview.setRenderer(homeGLRenderer);
        TextView txtAppVersion = (TextView) view.findViewById(R.id.txtAppVersion);
        txtAppVersion.setText(Version.getAppName(getActivity()) + " " + Version.getVerName(getActivity()));

        view.findViewById(R.id.layoutGuide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), GuideActivity.class));
            }
        });
        view.findViewById(R.id.layoutContact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ContactOurDialog().show(getFragmentManager(), "contactOur");
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        glsurfaceview.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        glsurfaceview.onResume();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((JoneMainActivity) activity).onSectionAttached(3);
    }

    @Override
    public void onShow() {
        super.onShow();
        ((JoneMainActivity) getActivity()).onSectionAttached(3);
        glsurfaceview.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHide() {
        super.onHide();
        glsurfaceview.setVisibility(View.INVISIBLE);
    }
}
