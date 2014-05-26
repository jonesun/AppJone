package com.jone.app.ui.fragment;

import android.app.Fragment;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.jone.app.Constants;
import com.jone.app.R;
import com.jone.app.test.CallRecordBusiness;
import com.jone.app.utils.SystemUtil;

/**
 * Created by jone_admin on 14-2-21.
 */
public class JoneTestListFragment extends Fragment {
    private View rootView;
    private SimpleCursorAdapter callRecordAdapter;
    private android.app.LoaderManager loaderManager;
    private ListView listView;
    private Button btn_add;
    private CallRecordBusiness callRecordBusiness;
    private ContentResolver contentResolver;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentResolver = getActivity().getContentResolver();
        callRecordBusiness = new CallRecordBusiness(contentResolver);
        contentResolver.registerContentObserver(Uri.parse(Constants.CALL_RECORD_URI), true, new MyObserver(new Handler()));
        rootView = inflater.inflate(R.layout.list_fragment_jone_test, container, false);
        btn_add = (Button) rootView.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri resultUri = callRecordBusiness.saveCallRecord("联系" + System.currentTimeMillis());
            }
        });
        listView = (ListView) rootView.findViewById(android.R.id.list);
        callRecordAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_1,
                null,
                new String[]{Constants.CALL_RECORD_NAME},
                new int[]{android.R.id.text1},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listView.setAdapter(callRecordAdapter);
        loaderManager = getLoaderManager();
        loaderManager.initLoader(111111, null, callbacks_callRecord);
        return rootView;
    }


    private LoaderManager.LoaderCallbacks<Cursor> callbacks_callRecord = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            Uri uri = Uri.parse(Constants.CALL_RECORD_URI);
            return new CursorLoader(getActivity(), uri, null, null, null, Constants.CALL_RECORD_CALL_TIME + " desc");
        }

        @Override
        public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
            callRecordAdapter.swapCursor(cursor);
            callRecordAdapter.notifyDataSetChanged();
        }

        @Override
        public void onLoaderReset(Loader<Cursor> cursorLoader) {
            callRecordAdapter.swapCursor(null);
        }
    };

    class MyObserver extends ContentObserver {
        public MyObserver(Handler handler) {
            super(handler);
        }
        //当监听到数据发生了变化就调用这个方法，并将新添加的数据查询出来
        public void onChange(boolean selfChange) {
            Log.e("sssssssssssss", "有数据变化: " + selfChange);
            loaderManager.restartLoader(111111, null, callbacks_callRecord);
        }
    }
}
