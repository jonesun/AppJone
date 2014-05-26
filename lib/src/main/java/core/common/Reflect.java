package core.common;

import android.content.Context;

import java.lang.reflect.Method;

/**
 * Created by admin on 13-11-21.
 */
public class Reflect {

    public static Object invokeSystemServiceMethod(Context c, String contextServiceName, String serviceManagerName, String methodName, Object[] o) {
        return invokeSystemServiceMethod(c, contextServiceName, serviceManagerName, methodName, o, null);
    }

    public static Object invokeSystemServiceMethod(Context c, String contextServiceName, String serviceManagerName, String methodName, Object[] o, Class[] clazz) {
        Object result = null;
        try {
            Class<?> serviceManager = Class
                    .forName(serviceManagerName);
            Object service = c.getSystemService(contextServiceName);
            if (service != null) {
                Method expand = serviceManager.getMethod(methodName, clazz == null ? genParas(o) : clazz);
                expand.setAccessible(true);
                result = expand.invoke(service, o);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return result;
    }

    public static Class[] genParas(Object[] paras) {
        Class[] c = null;
        if (paras != null) {
            int len = paras.length;
            c = new Class[len];
            for (int i = 0; i < len; i++) {
                //System.out.println(i + "pTestName:" + c[i].isPrimitive());
                //默认支持Integer.class，不支持int.class
                c[i] = paras[i].getClass();
            }
        }
        return c;
    }
}
