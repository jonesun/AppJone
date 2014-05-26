package core.common.upgrade.upgradeAndroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import core.common.Version;

//<receiver android:name="UpgradeBroadcastReceiver" >
//<intent-filter>
//    <action android:name="android.intent.action.PACKAGE_ADDED" />
//    <action android:name="android.intent.action.PACKAGE_REPLACED" />
//
//    <data android:scheme="package" />
//</intent-filter>
//</receiver>

public class UpgradeBroadcastReceiver extends BroadcastReceiver {
	private static final String TAG = "wxd";

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getDataString()
				.equals("package:" + context.getPackageName())) {

			if (Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())) {

				Toast.makeText(
						context,
						"版本：" + Version.getAppName(context) + " "
								+ Version.getVerName(context),
						Toast.LENGTH_LONG).show();

				Log.i(TAG, "ACTION_PACKAGE_REPLACED:" + intent.getDataString()
						+ "=" + context.getPackageName());

				Intent i = context.getPackageManager()
						.getLaunchIntentForPackage(context.getPackageName());
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(i);

				String apkStorage = context.getCacheDir() + "/"
						+ context.getPackageName() + ".apk";

				File downLoadApk = new File(apkStorage);
				if (downLoadApk.exists()) {
					downLoadApk.delete();
				}
				Log.i(TAG, "upgrade file " + apkStorage + " was deleted!");

			}
		}

	}

}
