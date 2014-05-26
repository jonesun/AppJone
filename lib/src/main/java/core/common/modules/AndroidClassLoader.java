package core.common.modules;

import java.io.File;

import dalvik.system.DexClassLoader;

import android.content.Context;

public class AndroidClassLoader {

    public static Class<?> loadClassLoader(Context c, String dexPath,
                                           String className) {
        File dexOutputDir = c.getDir("dex", Context.MODE_MULTI_PROCESS);// 路径为/data/data/packageName/app_myxxx
        // 设定到sdcard会导致代码注入攻击
        DexClassLoader classLoader = new DexClassLoader(dexPath,
                dexOutputDir.getAbsolutePath(), null, ClassLoader
                .getSystemClassLoader().getParent());
        Class<?> mLoadClass = null;
        try {
            mLoadClass = classLoader.loadClass(className);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return mLoadClass;
    }

}
