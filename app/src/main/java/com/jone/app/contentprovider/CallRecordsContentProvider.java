package com.jone.app.contentprovider;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.net.Uri;
import android.util.Log;

import com.jone.app.Constants;
import com.jone.app.dbHelper.MySQLiteHelper;

import java.util.ArrayList;


/**
 * Created by Administrator on 13-8-22.
 */
public class CallRecordsContentProvider extends ContentProvider {
    //
    private MySQLiteHelper mySQLiteHelper;
    private final static UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private final static int CALL_RECORD = 1;
    private final static int CALL_RECORDS = 2;
    private final static int CALL_RECORDS_LIMIT = 3;
    static {
        URI_MATCHER.addURI(Constants.CALL_RECORD_AUTHORITY, "callRecord/#", CALL_RECORD);
        URI_MATCHER.addURI(Constants.CALL_RECORD_AUTHORITY, "callRecord", CALL_RECORDS);
        URI_MATCHER.addURI(Constants.CALL_RECORD_AUTHORITY, "callRecordLimit/*", CALL_RECORDS_LIMIT);
    }
    public CallRecordsContentProvider(){
    }
    @Override
    public boolean onCreate() {
        mySQLiteHelper = new MySQLiteHelper(getContext(), Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
        return false;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        int flag = URI_MATCHER.match(uri);
        SQLiteDatabase sqLiteDatabase = mySQLiteHelper.getWritableDatabase();
        Uri returnUri = null;
        if(CALL_RECORDS == flag){
            long id = sqLiteDatabase.insert(Constants.CALL_RECORD_TABLE, null, contentValues);
            returnUri = ContentUris.withAppendedId(uri, id);
            getContext().getContentResolver().notifyChange(returnUri, null);
        }
        Log.i("ContentProvider", "---->>" + returnUri.toString());
        return returnUri;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        int count = -1;
        int flag = URI_MATCHER.match(uri);
        SQLiteDatabase sqLiteDatabase = mySQLiteHelper.getWritableDatabase();
        switch (flag) {
            case CALL_RECORD:
                long callRecordId = ContentUris.parseId(uri);
                String where_value = Constants.CALL_RECORD_ID + "=?";
                String[] where_args = { String.valueOf(callRecordId) };
                count = sqLiteDatabase.update(Constants.CALL_RECORD_TABLE, contentValues, where_value, where_args);
                break;
            case CALL_RECORDS:
                count = sqLiteDatabase.update(Constants.CALL_RECORD_TABLE, contentValues, selection, selectionArgs);
                break;
        }
        return count;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        SQLiteDatabase database = null;
        try{
            database = mySQLiteHelper.getWritableDatabase();
            //database.enableWriteAheadLogging();
        }catch (SQLiteDatabaseLockedException ex){
            database = mySQLiteHelper.getReadableDatabase();
        }
        int flag = URI_MATCHER.match(uri);
        Log.i("ContentProvider", "---->>flag: " + flag + ", uri: " + uri.toString());
        switch (flag) {
            case CALL_RECORD:
                long callRecordId = ContentUris.parseId(uri);
                String where_value = Constants.CALL_RECORD_ID + "=?";
                String[] where_args={String.valueOf(callRecordId)};
                cursor = database.query(true, Constants.CALL_RECORD_TABLE, projection, where_value, where_args, null, null, null, null);
                break;
            case CALL_RECORDS:
                //query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit)
                cursor = database.query(true, Constants.CALL_RECORD_TABLE, projection, selection, selectionArgs, null, null, sortOrder, null);
                break;
            case CALL_RECORDS_LIMIT:
                String limitCount = String.valueOf(ContentUris.parseId(uri)); //"0,50"
                limitCount = limitCount + "," + (Integer.parseInt(limitCount) + 50);
                Log.i("ContentProvider", "---->>" + limitCount);
                cursor = database.query(true, Constants.CALL_RECORD_TABLE, projection, selection, selectionArgs, null, null, sortOrder, limitCount);
                break;
        }
        return cursor;
    }



    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = -1;
        int flag = URI_MATCHER.match(uri);
        SQLiteDatabase database = mySQLiteHelper.getWritableDatabase();
        switch (flag) {
            case CALL_RECORD:
                long callRecordId = ContentUris.parseId(uri);
                String where_value = Constants.CALL_RECORD_ID + "=?";
                String[] where_args = { String.valueOf(callRecordId) };
                count = database.delete(Constants.CALL_RECORD_TABLE, where_value, where_args);
                break;

            case CALL_RECORDS:
                count = database.delete(Constants.CALL_RECORD_TABLE, selection, selectionArgs);
                break;
        }
        Log.i("ContentProvider", "---->>" + count);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        int flag = URI_MATCHER.match(uri);
        switch (flag) {
            case CALL_RECORD:
                return "vnd.android.cursor.item/callRecord";
            case CALL_RECORDS:
                return "vnd.android.cursor.dir/callRecords";
            case CALL_RECORDS_LIMIT:
                return "vnd.android.cursor.dir/callRecordLimit";
        }
        return null;
    }

    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        SQLiteDatabase database = mySQLiteHelper.getWritableDatabase();
        database.beginTransaction();//开始事务
        try{
            ContentProviderResult[]results = super.applyBatch(operations);
            database.setTransactionSuccessful();//设置事务标记为successful
            return results;
        }finally {
            database.endTransaction();//结束事务
        }
    }
}
