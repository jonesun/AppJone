package com.jone.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Display;

import com.jone.app.App;

import core.common.tuple.Tuple;
import core.common.tuple.Tuple2;

/**
 * Created by jone_admin on 14-2-19.
 */
public class SystemUtil {

    private static final int CASE_CAMERA = 0x0012;
    public static void takePhoto(Activity activity) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(intent, CASE_CAMERA);
    }

    private static final int OPEN_PHOTO = 0x3001;
    public static  void openPhoto(Activity activity){
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(i, OPEN_PHOTO);
    }

    public static void setNetwork(Context context){
        if(android.os.Build.VERSION.SDK_INT > 10 ){
            //3.0以上打开设置界面
            context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
        }else{
            context.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
        }
    }

    /**
     * s判断是否是平板
     * @param context
     * @return
     */
    public static boolean isPad(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * 判断设备是否带有闪光灯
     * @param context
     * @return
     */
    public static boolean isHaveFlashlight(Context context){
        boolean hasFlashLight = false;
        FeatureInfo[] feature= context.getPackageManager().getSystemAvailableFeatures();
        for (FeatureInfo featureInfo : feature) {
            if (PackageManager.FEATURE_CAMERA_FLASH.equals(featureInfo.name)) {
                hasFlashLight=true;
                break;
            }
        }
        return hasFlashLight;
    }

    public static DisplayMetrics getDisplayMetrics(Activity activity){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    /**
     * 精确获取屏幕尺寸（例如：3.5、4.0、5.0寸屏幕）
     * @param ctx
     * @return
     */
    public static double getScreenPhysicalSize(Activity ctx) {
        DisplayMetrics dm = new DisplayMetrics();
        ctx.getWindowManager().getDefaultDisplay().getMetrics(dm);
        double diagonalPixels = Math.sqrt(Math.pow(dm.widthPixels, 2) + Math.pow(dm.heightPixels, 2));
        return diagonalPixels / (160 * dm.density);
    }
}
