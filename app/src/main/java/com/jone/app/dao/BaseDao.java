package com.jone.app.dao;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.jone.app.App;
import com.jone.app.dbHelper.JoneORMLiteHelper;
import com.jone.app.utils.Utils;

import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jone_admin on 2014/3/25.
 */
public class BaseDao<T> {
    private static final String TAG = BaseDao.class.getSimpleName();
    private Class<T> entityClass;
    private Dao<T, Integer> dao;
    public BaseDao(){
        entityClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
        try {
            dao = JoneORMLiteHelper.getJoneORMLiteHelper(App.getInstance()).getDao(entityClass);
        } catch (SQLException e) {
            Log.e(TAG, entityClass + "  dao获取失败", e);
        }
    }


    public int create(T t){

        int result = -1;
        try {
            result = getDao().create(t);
        } catch (SQLException e) {
            Log.e(TAG, t + " 添加失败", e);
        }
        return result;
    }

    public int delete(T t){
        int result = -1;
        try {
            result = getDao().delete(t);
        } catch (SQLException e) {
            Log.e(TAG, t + " 删除失败", e);
        }
        return result;
    }
    public  int deleteAll(){
        int result = -1;
        try {
            result = getDao().delete(queryList());
        } catch (SQLException e) {
            Log.e(TAG,  " 删除失败", e);
        }
        return result;
    }

    public T queryByColumn(String columnName, Object value){
        T result = null;
        try {
            List<T> list = getDao().queryBuilder().where().eq(columnName, value).query();
            if(list != null && list.size() > 0){
                result = list.get(0);
            }
        } catch (SQLException e) {
            Log.e(TAG, columnName + ": " + value + " 查询失败", e);
        }
        return result;
    }

    public List<T> queryList(){
        List<T> results = null;
        try {
            results = getDao().queryForAll();
        } catch (SQLException e) {
            Log.e(TAG, "查询失败", e);
        }
        return results;
    }

    public long getCount(){
        long l = 0;
        try {
            l = getDao().queryBuilder().countOf();
        } catch (SQLException e) {
            Log.e(TAG, "查询失败, getCount: " + l, e);
        }
        return l;
    }

    public List<T> query(String orderByColumnName, boolean isAscending, int limit){
        List<T> list = new ArrayList<>();
        try {
            list = getDao().queryBuilder().orderBy(orderByColumnName, isAscending).limit(Utils.intToLong(limit)).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public Dao.CreateOrUpdateStatus createOrUpdate(T t){
        Dao.CreateOrUpdateStatus result = null;
        try {
            result = dao.createOrUpdate(t);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int update(T t){
        int result = -1;
        try {
            result = getDao().update(t);
        } catch (SQLException e) {
            Log.e(TAG, t + "更新失败", e);
        }
        return result;
    }

    public Dao<T, Integer> getDao() {
        return dao;
    }
}
