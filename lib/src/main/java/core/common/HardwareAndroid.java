package core.common;

import android.content.Context;

/**
 * Created by admin on 13-11-19.
 */
public class HardwareAndroid {

    // 需要系统权限及添加 <uses-permission android:name="android.permission.SERIAL_PORT"/>
    // 依赖ROM内串口类：android.hardware.SerialPort
    //  mSerialPort = (SerialPort) core.common.HardwareAndroid.SerialManager(c, "openSerialPort", new Object[]{new String("/dev/ttyS3"), 38400});
    public static Object SerialManager(Context c, String methodName, Object[] o) {
        return Reflect.invokeSystemServiceMethod(c, "serial", "android.hardware.SerialManager", methodName, o, new Class[]{String.class, int.class});
    }

}
