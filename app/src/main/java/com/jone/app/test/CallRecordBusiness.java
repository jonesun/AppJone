package com.jone.app.test;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.jone.app.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jone_admin on 13-11-1.
 */
public class CallRecordBusiness {
    private ContentResolver contentProvider;
    private Uri uri = Uri.parse(Constants.CALL_RECORD_URI);
    public CallRecordBusiness(ContentResolver contentProvider){
        this.contentProvider = contentProvider;
    }

    public Uri saveCallRecord(String roomName){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.CALL_RECORD_NAME, roomName);
        contentValues.put(Constants.CALL_RECORD_CALL_TIME, System.currentTimeMillis());

        Uri resultUri = contentProvider.insert(uri, contentValues);
        return resultUri;
    }

}
