package com.jone.app.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jone.app.App;
import com.jone.app.Constants;
import com.jone.app.callbacks.CommonListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by jone_admin on 14-1-3.
 */
public class Utils {

    /**
     * 根据设备类型设置屏幕方向
     * @param activity
     */
    public static void setScreenOrientation(Activity activity){
        if(SystemUtil.isPad(activity)){
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    public static TextView getEmptyView(Context context, String info){
        TextView emptyView = new TextView(context);
        emptyView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        emptyView.setPadding(20, 20, 20, 20);
        emptyView.setTextSize(16);
        emptyView.setText(info);
        emptyView.setTextColor(context.getResources().getColor(android.R.color.white));
        return emptyView;
    }
    public static TextView setGridEmptyView(Context context, GridView gridView, String info){
        TextView emptyView = getEmptyView(context, info);
        emptyView.setVisibility(View.GONE);
        ((ViewGroup)gridView.getParent()).addView(emptyView);
        gridView.setEmptyView(emptyView);
        return emptyView;
    }

    public static String formatDataTime(long time, String format) {
        return new SimpleDateFormat(format).format(new Date(time));
    }
    public static String  long2string(long time,SimpleDateFormat dateFormat){
        return dateFormat.format(time);
    }

    public static String weekNum2string(int weekNum){
        String[] weekStrings = {"日", "一", "二", "三", "四", "五", "六"};
        return weekStrings[weekNum - 1];
    }

    public static boolean haveInstallApp(Context context,String packageName) {
        if(TextUtils.isEmpty(packageName)) {
            return false;
        }
        // get packagemanager
        PackageManager packageManager =context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String installPackageName = pinfo.get(i).packageName;
                if(packageName.equals(installPackageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断网络情况
     *
     * @return false 表示没有网络 true 表示有网络
     */
    public static boolean isNetworkAlive() {
        // 获得网络状态管理器
        ConnectivityManager connectivityManager = (ConnectivityManager) App.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            // 建立网络数组
            NetworkInfo[] net_info = connectivityManager.getAllNetworkInfo();
            if (net_info != null) {
                for (int i = 0; i < net_info.length; i++) {
                    // 判断获得的网络状态是否是处于连接状态
                    if (net_info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 为程序创建桌面快捷方式
     * <uses-permission android:name="com.android.launcher.permission.INSTALL_SHORTCUT" />
     */
    public static void addShortcut(Activity activity, String shortcutName, int shortcutIconResource){
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");

        //快捷方式的名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName);
        shortcut.putExtra("duplicate", false); //不允许重复创建

        /****************************此方法已失效*************************/
        //ComponentName comp = new ComponentName(this.getPackageName(), "."+this.getLocalClassName());
        //shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(comp));  　　
        /******************************end*******************************/
        Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
        shortcutIntent.setClassName(activity.getPackageName(), activity.getClass().getName());
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);

        //快捷方式的图标
        Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(activity, shortcutIconResource);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);

        activity.sendBroadcast(shortcut);
    }

    public static boolean hasShortcut(Context context, SharedPreferences sharedPreferences, String key) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if(sharedPreferences.getBoolean(key, false)){
            return true;
        }
        return false;
    }

    /**
     * 删除程序的快捷方式
     *<uses-permission android:name="com.android.launcher.permission.UNINSTALL_SHORTCUT" />
     *
     */
    public static void delShortcut(Activity activity, String shortcutName){
        Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        //快捷方式的名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName);
        String appClass = activity.getPackageName() + "." +activity.getLocalClassName();
        ComponentName comp = new ComponentName(activity.getPackageName(), appClass);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(comp));

        activity.sendBroadcast(shortcut);

    }

    public static long intToLong(int i){
        long l = 0;
        try{
            l = Long.parseLong(String.valueOf(i));
        }catch (Exception e){
            Log.e("intToLong", "转换失败", e);
        }
        return l;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static float dip2px(Context context, int dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static float px2dip(Context context, int pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxValue / scale + 0.5f);
    }
}
