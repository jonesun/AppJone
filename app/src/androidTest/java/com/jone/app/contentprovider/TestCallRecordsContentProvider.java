package com.jone.app.contentprovider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.jone.app.Constants;

/**
 * Created by jone on 2014/5/7.
 */
public class TestCallRecordsContentProvider extends AndroidTestCase {
    ContentResolver mContentResolver;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        //AndroidTestCase 中核心的就是getContext(),可以得到一个模拟的context
        mContentResolver = getContext().getContentResolver();
    }
    public void testInsert() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.CALL_RECORD_NAME, "test测试用01");
        contentValues.put(Constants.CALL_RECORD_CALL_TIME, System.currentTimeMillis());
        Uri uri = Uri.parse(Constants.CALL_RECORD_URI);
        Uri resultUri = mContentResolver.insert(uri, contentValues);
        //测试是否真的插入数据了
        assertNotNull(resultUri);
        //测试完删除所有数据
        //mContentResolver.delete(uri, Constants.CALL_RECORD_ID + "=" + "0", null);

    }
}
