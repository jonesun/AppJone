package com.jone.app.broadcasts;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by jone_admin on 14-1-7.
 */
public class JoneDeviceAdminReceiver extends DeviceAdminReceiver {
    private static final String TAG = JoneDeviceAdminReceiver.class.getName();
    @Override
    public DevicePolicyManager getManager(Context context) {
        Log.i(TAG, "调用了getManager()方法");
        return super.getManager(context);
    }
    @Override
    public ComponentName getWho(Context context) {
        Log.i(TAG, "调用了getWho()方法");
        return super.getWho(context);
    }

    /**
     * 禁用
     */
    @Override
    public void onDisabled(Context context, Intent intent) {
        Log.i(TAG, "调用了onDisabled()方法");
        Toast.makeText(context, "禁用设备管理", Toast.LENGTH_SHORT).show();

        super.onDisabled(context, intent);
    }
    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        Log.i(TAG, "调用了onDisableRequested()方法");
        return super.onDisableRequested(context, intent);
    }

    /**
     * 激活
     */
    @Override
    public void onEnabled(Context context, Intent intent) {
        Log.i(TAG, "调用了onEnabled()方法");
        Toast.makeText(context, "启动设备管理", Toast.LENGTH_SHORT).show();

        super.onEnabled(context, intent);
    }
    @Override
    public void onPasswordChanged(Context context, Intent intent) {
        Log.i(TAG, "调用了onPasswordChanged()方法");
        super.onPasswordChanged(context, intent);
    }
    @Override
    public void onPasswordFailed(Context context, Intent intent) {
        Log.i(TAG, "调用了onPasswordFailed()方法");
        super.onPasswordFailed(context, intent);
    }
    @Override
    public void onPasswordSucceeded(Context context, Intent intent) {
        Log.i(TAG, "调用了onPasswordSucceeded()方法");
        super.onPasswordSucceeded(context, intent);
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "调用了onReceive()方法");
        super.onReceive(context, intent);
    }
    @Override
    public IBinder peekService(Context myContext, Intent service) {
        Log.i(TAG, "调用了peekService()方法");
        return super.peekService(myContext, service);
    }
}
