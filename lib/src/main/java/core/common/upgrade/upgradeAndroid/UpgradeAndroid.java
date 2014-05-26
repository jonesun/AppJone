package core.common.upgrade.upgradeAndroid;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import core.common.Shell;
import core.common.Version;
import core.common.DownloadManagerUtil;
import core.common.tuple.Tuple;
import core.common.tuple.Tuple3;
import core.common.upgrade.UpgradeBean;

/**
 * Created by jone_admin on 13-12-11.
 * 1.UpgradeAndroid.runUpgrade(context, upgradeBean, upgradeListener)
 * 2.register UpgradeBroadcastReceiver in AndroidManifest.xml //安装完自动打开的广播(可选)
 * 3.<uses-permission android:name="android.permission.INSTALL_PACKAGES" />...//一些权限, todo
 */
public class UpgradeAndroid {
    private static String TAG = UpgradeAndroid.class.getSimpleName();

    private static boolean verifyUpgradeBean(UpgradeBean upgradeBean){
        if(upgradeBean != null && upgradeBean.getFormUrl() != null){return true;}
        else {return false;}
    }

    public static void runUpgrade(final Context context,final UpgradeBean upgradeBean, final UpgradeListener upgradeListener){
        if(verifyUpgradeBean(upgradeBean)){
            Log.d("UpgradeAndroid", "upgrade running...");
            Toast.makeText(context, "正在检查更新...", Toast.LENGTH_LONG).show();
            RequestQueue mQueue = Volley.newRequestQueue(context);
            mQueue.add(new JsonObjectRequest(Request.Method.GET,
                    upgradeBean.getFormUrl(),
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int code = response.getInt("code");
                                String version = response.getString("version");
                                final String apkUrl = response.getString("address");
                                Log.d(TAG, "code: " + code + ", currentCode: " + Version.getVerCode(context) + ", apkUrl: " + apkUrl);
                                if(code > Version.getVerCode(context)){
                                    if(upgradeBean.isManualUpgrade()){
                                        new AlertDialog.Builder(context)
                                                .setTitle("升级")
                                                .setMessage("新版本: v" + version + ", 是否更新?")
                                                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {}})
                                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {upgradeListener.onUpgrade(upgrade(context, upgradeBean, apkUrl));}
                                                }).create().show();
                                    }else {
                                        upgradeListener.onUpgrade(upgrade(context, upgradeBean, apkUrl));
                                    }
                                }else {
                                    upgradeListener.onUpgrade(Tuple.tuple(false, 0l, "已经是最新版本."));
                                }
                            } catch (JSONException e) {
                                upgradeListener.onUpgrade(Tuple.tuple(false, -1l, "Json解析错误" + e.getMessage()));
                            }
                        }
                    },
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            upgradeListener.onUpgrade(Tuple.tuple(false, -1l, "连接服务器失败,请检查网络是否连接"));
                        }
                    }));
            mQueue.start();
        }else {
            Log.e(TAG, "upgradeBean传递有误");
        }
    }
    private static Tuple3<Boolean, Long, String> upgrade(final Context context, UpgradeBean upgradeBean, String apkUrl){
        long downloadId = DownloadManagerUtil.download(context,
                apkUrl,
                upgradeBean.getDownloadSavePath(),
                upgradeBean.getDownloadSaveFileName(),
                new DownloadManagerUtil.DownloadCompleteListener() {
                    @Override
                    public void onComplete(DownloadManager downloadManager, long completeDownloadId) {
                        DownloadManager.Query myDownloadQuery = new DownloadManager.Query();
                        myDownloadQuery.setFilterById(completeDownloadId);

                        Cursor myDownload = downloadManager.query(myDownloadQuery);
                        if (myDownload.moveToFirst()) {
                            int fileNameIdx = myDownload.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
                            int fileUriIdx =myDownload.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);

                            String fileName = myDownload.getString(fileNameIdx);
                            String fileUri = myDownload.getString(fileUriIdx);
                            Log.d("CompleteReceiver", fileName + " : " + fileUri);
                            if(UpgradeAndroid.installSilent(fileName) != 0){
                                Log.e("CompleteReceiver", "静默升级失败,采用显式升级");
                                UpgradeAndroid.installApk(context, fileName);
                            }
                        }
                        myDownload.close();
                    }
                });
        return Tuple.tuple(true, downloadId, "apk下载中...");
    }

    public static void installApk(Context c, String filePath){
        Uri uri = Uri.fromFile(new File(filePath));
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri,"application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        c.startActivity(intent);
    }

    public static void uninstallAPK(Context c, String packageName) {
        Uri packageURI = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(packageURI);
        c.startActivity(intent);
    }

    /**
     * install silent
     * @param filePath
     * @return 0 means normal, 1 means file not exist, 2 means other exception error
     */
    public static int installSilent(String filePath) {
        int result;
        File file = new File(filePath);
        if (filePath == null || filePath.length() == 0 || (file = new File(filePath)) == null || file.length() <= 0
                || !file.exists() || !file.isFile()) {
            return 1;
        }

        Tuple3<Integer, String, String> resultTuple = Shell.runCommandAndReturn("pm install -r " + filePath);
        result = verifyResultTuple(Shell.runCommandAndReturn("pm install -r " + filePath));
        if(result == 2){
            result = verifyResultTuple(Shell.runRootCommandAndReturn("pm install -r " + filePath));
        }
        Log.d(TAG, resultTuple.toString());
        return result;
    }
    private static Integer verifyResultTuple(Tuple3<Integer, String, String> resultTuple){
        if (resultTuple.v2.contains("Success") || resultTuple.v2.contains("success")) {
            return 0;
        } else {
            return 2;
        }
    }

    public interface UpgradeListener {
        void onUpgrade(Tuple3<Boolean, Long, String> result);
    }
}
