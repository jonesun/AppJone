package com.jone.app.dbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jone.app.Constants;

/**
 * Created by Administrator on 13-8-21.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {
    public MySQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
   /* public static MySQLiteHelper newInstance(){

    }*/

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.v("MyDBhelper onCreate", "Creating all the tables");
        try{
            String createCallRecordSQL = "CREATE TABLE IF NOT EXISTS " + Constants.CALL_RECORD_TABLE +
                    "( " + Constants.CALL_RECORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + Constants.CALL_RECORD_NAME + " VARCHAR, "
                    + Constants.CALL_RECORD_CALL_TIME + " long"
                    + " );";
            sqLiteDatabase.execSQL(createCallRecordSQL);
            Log.v("success" , "成功添加表: " + Constants.CALL_RECORD_TABLE);
        }catch(SQLiteException ex){
            Log.v("Create table exception", ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.v("TaskDBAdapter", "Upgrading from version " + oldVersion + "to " + newVersion + ", which will destroy all old data");
        sqLiteDatabase.execSQL("drop table if exists " + Constants.CALL_RECORD_TABLE);
        onCreate(sqLiteDatabase);
    }

}
