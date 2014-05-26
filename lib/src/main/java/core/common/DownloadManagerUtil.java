package core.common;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

/**
 * Created by jone_admin on 13-12-24.
 * use: DownloadManagerUtil.download(context, url, savePath, saveName, completeListener);
 * or use: DownloadManagerUtil.download(context, url, completeListener)
 */
public class DownloadManagerUtil {
    public static long download(Context context, String url, String savePath, String saveName, DownloadCompleteListener completeListener){
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        saveName = saveName != null ? saveName : url.substring(url.lastIndexOf('/') + 1);
        if(savePath == null){
            request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, saveName);
        }else {
            if(savePath.indexOf("/mnt/sdcard/") > -1){
                savePath = savePath.replace("/mnt/sdcard/", "");
                Log.e("savePath", savePath);
            }
            request.setDestinationInExternalPublicDir(savePath, saveName);
        }
        long downloadId = downloadManager.enqueue(request);
        Log.d("download", "downloadId: " + downloadId);
        registerCompleteReceiver(context, downloadId, completeListener);
        return downloadId;
    }

    public static long download(Context context, String url, DownloadCompleteListener completeListener){
        return download(context, url, null, null, completeListener);
    }

    public interface DownloadCompleteListener{
        public void onComplete(DownloadManager downloadManager, long completeDownloadId);
    }

    private static void registerCompleteReceiver(Context context, long downloadId, DownloadCompleteListener completeListener){
        context.registerReceiver(new DownloadManagerUtil().new DownloadManagerReceiver(downloadId, completeListener), new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private class DownloadManagerReceiver extends BroadcastReceiver {
        private long downloadId;
        private DownloadCompleteListener completeListener;
        public DownloadManagerReceiver(long downloadId, DownloadCompleteListener completeListener){
            this.downloadId = downloadId;
            this.completeListener = completeListener;
        }
        @Override
        public void onReceive(Context context, Intent intent) {
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            Log.d("CompleteReceiver", "completeDownloadId: " + completeDownloadId + ", currentDownloadId: " + downloadId);
            if(downloadId == completeDownloadId){
                completeListener.onComplete(downloadManager, downloadId);
            }
            context.unregisterReceiver(this);
        }
    }
}
