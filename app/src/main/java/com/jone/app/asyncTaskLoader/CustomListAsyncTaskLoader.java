package com.jone.app.asyncTaskLoader;

import android.content.AsyncTaskLoader;

import com.jone.app.App;
import com.jone.app.callbacks.CommonListener;

import java.util.Collections;
import java.util.List;


/**
 * Created by jone_admin on 13-12-11.
 */
public class CustomListAsyncTaskLoader extends AsyncTaskLoader<List> {
    private List list;
    private CommonListener listener;
    public CustomListAsyncTaskLoader(CommonListener listener) {
        super(App.getInstance());
        this.listener = listener;
    }

    @Override
    protected void onStartLoading() {
        // just make sure if we already have content to deliver
        if (list != null)
            deliverResult(list);

        // otherwise if something has been changed or first try
        if (takeContentChanged() || list == null)
            forceLoad();
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();

        // clear reference to object
        // it's necessary to allow GC to collect the object
        // to avoid memory leaking
        list = null;
    }

    @Override
    public List loadInBackground() {
        // even if fail return empty list and print exception stack trace
        List result = (List) listener.onExecute(null);
        return Collections.unmodifiableList(result);
    }
}
