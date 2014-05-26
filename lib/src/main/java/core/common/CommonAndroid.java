package core.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Instrumentation;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.InputType;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;

public class CommonAndroid {
    public static void sendStringSync(final String s) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new Instrumentation().sendStringSync(s);
            }
        }).start();
    }

    public static void sendKeyDownUpSync(final int keyCode) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new Instrumentation().sendKeyDownUpSync(keyCode);
            }
        }).start();
    }
//    public static void setSoftInputShownOnFocus(EditText et) {
//        setSoftInputShownOnFocus(null,et);
//    }
//
//    @Deprecated()
//    public static void setSoftInputShownOnFocus(Activity a, EditText et) {
//        if (android.os.Build.VERSION.SDK_INT <= 10) {
//            et.setInputType(InputType.TYPE_NULL);
//        } else {
//            if (a != null) {
//                a.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//            }
//            try {
//                Class<EditText> cls = EditText.class;
//                Method setSoftInputShownOnFocus;
//                setSoftInputShownOnFocus = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
//                setSoftInputShownOnFocus.setAccessible(true);
//                setSoftInputShownOnFocus.invoke(et, false);
//            } catch (Exception e) {
//                Log.e(Config.TAG, e.getMessage());
//            }
//            try {
//                Class<EditText> cls = EditText.class;
//                Method setShowSoftInputOnFocus;
//                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
//                setShowSoftInputOnFocus.setAccessible(true);
//                setShowSoftInputOnFocus.invoke(et, false);
//            } catch (Exception e) {
//                Log.e(Config.TAG, e.getMessage());
//            }
//        }
//    }

    public static String getCurrentProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    public static X509Certificate getX509Certificate(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        int flags = PackageManager.GET_SIGNATURES;
        PackageInfo packageInfo = null;

        try {
            packageInfo = pm.getPackageInfo(context.getPackageName(), flags);
        } catch (PackageManager.NameNotFoundException e) {
            System.out.println(e.getMessage());
        }

        Signature[] signatures = packageInfo.signatures;

        // cert = DER encoded X.509 certificate:
        byte[] cert = signatures[0].toByteArray();
        InputStream input = new ByteArrayInputStream(cert);
        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X509");
        } catch (CertificateException e) {
            System.out.println(e.getMessage());
        }

        X509Certificate c = null;
        try {
            c = (X509Certificate) cf.generateCertificate(input);
        } catch (CertificateException e) {
            System.out.println(e.getMessage());
        }
        return c;
    }

    public static String getSignSubjectDN(Context context, String packageName) {
        X509Certificate c = getX509Certificate(context, packageName);
        System.out.println("Certificate for: " + c.getSubjectDN());
        System.out.println("Certificate issued by: " + c.getIssuerDN());
        System.out.println("The certificate is valid from " + c.getNotBefore() + " to " + c.getNotAfter());
        System.out.println("Certificate SN# " + c.getSerialNumber());
        System.out.println("Generated with " + c.getSigAlgName());

        return c.getSubjectDN().toString();
    }


    public static boolean isSign(Context context) {
        String sign = CommonAndroid.getSignSubjectDN(context, context.getPackageName());
        if (sign.indexOf("Android Debug") > -1) {
            System.out.println("当前运行的是无系统签名版本！");
            return false;
        }
        return true;
    }

    public static String toCharsString(byte[] sigBytes) {
        byte[] sig = sigBytes;
        final int N = sig.length;
        final int N2 = N * 2;
        char[] text = new char[N2];
        for (int j = 0; j < N; j++) {
            byte v = sig[j];
            int d = (v >> 4) & 0xf;
            text[j * 2] = (char) (d >= 10 ? ('a' + d - 10) : ('0' + d));
            d = v & 0xf;
            text[j * 2 + 1] = (char) (d >= 10 ? ('a' + d - 10) : ('0' + d));
        }
        return new String(text);
    }
}
