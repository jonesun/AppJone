package com.jone.app.ui;

import android.app.Activity;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jone.app.R;
import com.jone.app.ui.activities.JoneAidlClientActivity;
import com.jone.app.ui.activities.TestGridLayoutActivity;
import com.jone.app.ui.activities.keyboard.KeydemoActivity;
import com.jone.app.ui.fragment.JoneBaseFragment;
import com.jone.app.ui.fragment.joneMain.AboutFragment;
import com.jone.app.ui.fragment.joneMain.AllAppsFragment;
import com.jone.app.ui.fragment.joneMain.DeviceInfoFragment;
import com.jone.app.ui.fragment.joneMain.JoneMainFragment;
import com.jone.app.ui.fragment.NavigationDrawerFragment;
import com.jone.app.ui.fullscreen.FullscreenActivity;
import com.jone.app.ui.setting.SettingsActivity;
import com.jone.app.ui.setting.joneSettings.JoneSettingsActivity;
import com.jone.app.utils.Utils;
import com.jone.app.wifidirect.discovery.WiFiServiceDiscoveryActivity;

public class JoneMainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private long exitTime = 0;

    private FragmentManager fragmentManager;
    private JoneBaseFragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setScreenOrientation(this);
        setContentView(R.layout.activity_jone_main);
        fragmentManager = getFragmentManager();
//        JoneBaseFragment.addFragment(fragmentManager, R.id.container, currentFragment, JoneMainFragment.class.getName());
        mNavigationDrawerFragment = (NavigationDrawerFragment)getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        getWindow().getDecorView().setBackgroundResource(R.drawable.bg01);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        if(fragmentManager == null){
            fragmentManager = getFragmentManager();
        }
        JoneBaseFragment fragment;
        String fragmentTag;
        switch (position){
            case 0:
                fragmentTag = JoneMainFragment.class.getName();
//                fragment = new JoneMainFragment();
                if(fragmentManager.findFragmentByTag(fragmentTag) == null){
                    fragment = new JoneMainFragment();
                }else {
                    fragment = (JoneBaseFragment) fragmentManager.findFragmentByTag(fragmentTag);
                }
                break;
            case 1:
                fragmentTag = AllAppsFragment.class.getName();
                if(fragmentManager.findFragmentByTag(fragmentTag) == null){
                    fragment = new AllAppsFragment();
                }else {
                    fragment = (JoneBaseFragment) fragmentManager.findFragmentByTag(fragmentTag);
                }
                break;
            case 2:
                fragmentTag = DeviceInfoFragment.class.getName();
                if(fragmentManager.findFragmentByTag(fragmentTag) == null){
                    fragment = new DeviceInfoFragment();
                }else {
                    fragment = (JoneBaseFragment) fragmentManager.findFragmentByTag(fragmentTag);
                }
                break;
            case 3:
                fragmentTag = AboutFragment.class.getName();
                if(fragmentManager.findFragmentByTag(fragmentTag) == null){
                    fragment = new AboutFragment();
                }else {
                    fragment = (JoneBaseFragment) fragmentManager.findFragmentByTag(fragmentTag);
                }
                break;
            default:
                fragmentTag = PlaceholderFragment.class.getName();
                if(fragmentManager.findFragmentByTag(fragmentTag) == null){
                    fragment = PlaceholderFragment.newInstance(position);
                }else {
                    fragment = (JoneBaseFragment) fragmentManager.findFragmentByTag(fragmentTag);
                }
                break;
        }
        JoneBaseFragment.switchFragment(fragmentManager, R.id.container, currentFragment, fragment, fragmentTag);
        currentFragment = fragment;
    }

    public void onSectionAttached(String title){
        mTitle = title;
    }

    public void onSectionAttached(int number) {
        String[] titles = getResources().getStringArray(R.array.titles);
        mTitle = titles[number];
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.jone_main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(JoneMainActivity.this, JoneSettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends JoneBaseFragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        private LinearLayout layout_center;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_jone_test, container, false);
//            int index = getArguments().getInt(ARG_SECTION_NUMBER);
            layout_center = (LinearLayout) rootView.findViewById(R.id.layout_center);
            setCenter();
            return rootView;
        }

        private void setCenter(){
            Class toActivityClasses[] = new Class[]{
                    SwipeViewsActivity.class, ActionBarTabsActivity.class, ActionBarSpinnerActivity.class,
                    FullscreenActivity.class, LoginActivity.class, SettingsActivity.class, WiFiServiceDiscoveryActivity.class,
                    JoneAidlClientActivity.class, TestGridLayoutActivity.class, KeydemoActivity.class,
            };
            for(Class clazz : toActivityClasses){
                addOperatorBtn(clazz);
            }
        }

        private void addOperatorBtn(final Class toActivityClazz){
            Button button_operator = new Button(getActivity());
            button_operator.setText("打开" + toActivityClazz.getSimpleName());
            button_operator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), toActivityClazz));
                }
            });
            layout_center.addView(button_operator);
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((JoneMainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

        @Override
        public void onShow() {
            super.onShow();
            ((JoneMainActivity) getActivity()).onSectionAttached(4);
        }
    }

}
