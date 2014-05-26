package com.jone.app.dbHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.DatabaseTableConfigUtil;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.j256.ormlite.table.TableUtils;
import com.jone.app.entities.WeatherCity;
import com.jone.bean.Note;

import java.sql.SQLException;

/**
 * Created by jone_admin on 2014/3/25.
 */
public class JoneORMLiteHelper extends OrmLiteSqliteOpenHelper {
    private static final String TAG = JoneORMLiteHelper.class.getSimpleName();
    private static JoneORMLiteHelper joneORMLiteHelper = null;

    private static final String DATABASE_NAME = "jone.db";
    private static final int DATABASE_VERSION = 1;

    public JoneORMLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static JoneORMLiteHelper getJoneORMLiteHelper(Context context) {
        if (joneORMLiteHelper == null && context != null) {
            joneORMLiteHelper = OpenHelperManager.getHelper(context, JoneORMLiteHelper.class);
        }
        return joneORMLiteHelper;
    }

    public static void releaseHelper() {
        /*
         * 释放资源
         */
        if (joneORMLiteHelper != null) {
            OpenHelperManager.releaseHelper();
            joneORMLiteHelper = null;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Note.class);
            TableUtils.createTableIfNotExists(connectionSource, WeatherCity.class);
        } catch (SQLException e) {
            Log.e(TAG, "数据表创建失败 ", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i2) {
        try {
            TableUtils.dropTable(connectionSource, Note.class, true);
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            Log.e(TAG, "更新数据库失败", e);
        }
    }

    @Override
    public void close() {
        super.close();
    }

    public <D extends Dao<T, ?>, T> D getDao(Class<T> clazz) throws SQLException {
        // lookup the dao, possibly invoking the cached database config
        Dao<T, ?> dao = DaoManager.lookupDao(connectionSource, clazz);
        if (dao == null) {
            // try to use our new reflection magic
            DatabaseTableConfig<T> tableConfig = DatabaseTableConfigUtil.fromClass(connectionSource, clazz);
            if (tableConfig == null) {
                /**
                 * TODO: we have to do this to get to see if they are using the deprecated annotations like
                 * {@link DatabaseFieldSimple}.
                 */
                dao = (Dao<T, ?>) DaoManager.createDao(connectionSource, clazz);
            } else {
                dao = (Dao<T, ?>) DaoManager.createDao(connectionSource, tableConfig);
            }
        }

        @SuppressWarnings("unchecked")
        D castDao = (D) dao;
        return castDao;
    }
}
