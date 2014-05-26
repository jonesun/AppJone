package core.common;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.net.ethernet.EthernetDevInfo;
import android.net.ethernet.IEthernetManager;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.lang.reflect.Method;
public class Net {

    //系统权限或/system/app应用
    //依赖类 frameworks\base\ethernet\java\android\net\ethernet
    // EthernetDevInfo info = new EthernetDevInfo();
    // info.setIfName("eth0");
    // info.setConnectMode(EthernetDevInfo.ETHERNET_CONN_MODE_MANUAL);//ETHERNET_CONN_MODE_DHCP
    // info.setIpAddress("192.168.1.224");
    // info.setNetMask("255.255.255.0");
    // info.setGateWay("192.168.1.1");
    // info.setDnsAddr("8.8.8.8");
    // setEthernetInfo(getApplicationContext(), info);
    // 未测BROADCAST_STICKY权限是否必须
    /*
    <uses-permission android:name="android.permission.WRITE_SETTINGS" />
    <uses-permission android:name="android.permission.WRITE_SECURE_SETTINGS"/>
    <uses-permission android:name="android.permission.BROADCAST_STICKY" />
    */
    public static void setEthernetInfo(Context c, EthernetDevInfo info) {
        try {
            Class<?> ServiceManager = c.getClass().getClassLoader()
                    .loadClass("android.os.ServiceManager");

            Method getServiceMethod = ServiceManager.getMethod("getService",
                    String.class);

            IBinder ibinder = (IBinder) getServiceMethod.invoke(null,
                    "ethernet");

            IEthernetManager eth = IEthernetManager.Stub.asInterface(ibinder);
            eth.updateDevInfo(info);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
