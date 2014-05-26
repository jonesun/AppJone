package core.common.modules;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.util.Log;


public class Module {

	public static boolean instance(Context c, String apkPath,  String className,
			Class<?>[] pClass, Object[] args) {
		boolean isOK = true;
		// 通过反射调用
		//String dexPath = c.getFilesDir() + "/module/" + apkPath + ".apk";
		System.out.println("Module.instance(...)  apkPath:" + apkPath);

		if (new File(apkPath).exists()) {
			Class<?> PluginTest = AndroidClassLoader.loadClassLoader(
					c.getApplicationContext(), apkPath, className);

			try {
				Object obj = PluginTest.getConstructor(pClass)
						.newInstance(args);
				System.out.println(obj.getClass().getName());
				PluginTest = null;
				obj = null;
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		} else {
            isOK = false;
			System.out.println("Module.instance(...) File not exists:"
					+ apkPath);
		}

		System.out.println("Module.instance(...) end!");
		System.gc();

		return isOK;
	}

    public static Object getInstance(Context targetContext, String classCanonicalName, Class<?>[] pClass, Object[] args){
        Class<?> c = null;
        Object obj = null;
        try {
            c = targetContext.getClassLoader().loadClass(classCanonicalName);
            obj = c.getConstructor(pClass).newInstance(args);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
            Log.e("TAG", "Exception", e);
        }  catch (InvocationTargetException e) {
            Log.e("TAG", e.getTargetException().toString());
            e.printStackTrace();
        }
        return obj;
    }

    public static Object getInstance(Context c, String apkPath, String className) {
        Object obj = null;
        if (new File(apkPath).exists()) {
            Class<?> PluginTest = AndroidClassLoader.loadClassLoader(c.getApplicationContext(), apkPath, className);
            try {
                obj = PluginTest.newInstance();
                System.out.println("obj: " + obj.getClass().getName());

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            finally {
                PluginTest = null;
            }

        } else {
            System.out.println("Module.instance(...) File not exists:" + apkPath);
        }
        System.out.println("Module.instance(...) end!");
        System.gc();
        return obj;
    }

    public static void invokeMethod(Object obj, String methodName, Class<?>[] pClass, Object[] args){
        if(obj != null){
            try {
                Method m = obj.getClass().getDeclaredMethod(methodName,pClass);
                m.invoke(obj, args);
            } catch (IllegalAccessException | NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                System.err.println(e.getTargetException());
                //e.printStackTrace();
            }
        }
    }

}
