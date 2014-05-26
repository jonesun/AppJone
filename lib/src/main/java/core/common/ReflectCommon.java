package core.common;

import java.lang.reflect.Field;

public class ReflectCommon {
    public static Object getEntityProperty(Object obj, String fieldName) {
        Object result = null;
        Class objClass = obj.getClass();
        try {
            Field field = null;
            field = objClass.getDeclaredField(fieldName);

            // 修改访问控制权限
            boolean accessFlag = field.isAccessible();
            if (!accessFlag) {
                field.setAccessible(true);
            }

            result = field.get(obj);
            // 恢复访问控制权限
            field.setAccessible(accessFlag);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("ReflectCommon.getEntityProperty:" + e);
        }
        return result;
    }

    public static void setEntityProperty(Object obj, String fieldName, Object value) {
        Class objClass = obj.getClass();
        try {
            Field field = null;
            field = objClass.getDeclaredField(fieldName);

            // 修改访问控制权限
            boolean accessFlag = field.isAccessible();
            if (!accessFlag) {
                field.setAccessible(true);
            }

            field.set(obj, value);
            // 恢复访问控制权限
            field.setAccessible(accessFlag);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("ReflectCommon.setEntityProperty:" + e);
        }
    }
}
