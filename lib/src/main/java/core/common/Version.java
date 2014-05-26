package core.common;

import android.content.Context;

public class Version {

	public static int getVerCode(Context context){
		int verCode = -1;
		try {
			verCode = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
		} catch (Exception e) {
			//Log.e(TAG, e.getMessage());
			System.out.println(e.getMessage());
		}
		return verCode;
	}

	public static String getVerName(Context context) {
		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return verName;
	}

	public static String getAppName(Context context) {
		String appName = "";
		try {
			appName = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).applicationInfo.loadLabel(
					context.getPackageManager()).toString();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return appName;
	}
}
