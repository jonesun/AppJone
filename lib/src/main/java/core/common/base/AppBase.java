package core.common.base;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import core.common.VolleyCommon;


public class AppBase extends Application {
    private static Handler handler = new Handler();
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences sharedPreferencesService;
    private static VolleyCommon volleyCommon;
    private static Yaml yaml;
    static public final Logger log = LoggerFactory.getLogger(AppBase.class);

    public static Handler getHandler() {
        return handler;
    }

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public static SharedPreferences getSharedPreferencesService() {
        return sharedPreferencesService;
    }

    public static VolleyCommon getVolleyCommon() {
        return volleyCommon;
    }

    public static Yaml getYaml() {
        return yaml;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("AppBase onCreate");
        log.info("Logger - AppBase onCreate");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferencesService = getSharedPreferences("service", Context.MODE_MULTI_PROCESS);
        volleyCommon = new VolleyCommon(getApplicationContext());
        yaml = new Yaml();
    }

    public static Object getValueOnSharedPreferencesService(String fieldName) {
        String valueOfSerializer = getSharedPreferencesService().getString(fieldName, null);
        if (valueOfSerializer != null && !valueOfSerializer.isEmpty()) {
            return getYaml().load(valueOfSerializer);
        }
        return null;
    }
}
