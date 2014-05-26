package core.common;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

public class IO {


	// 拷贝不设置权限
	public static int copyAssetsToFiles(Context c, String assetsFilePath) {
		return copyAssetsToFiles(c, assetsFilePath, null);
	}

	// 拷贝并设置权限
	public static int copyAssetsToFiles(Context c, String assetsFilePath,
			String permissions) {
		int r = -1;
		String fileName = new File(assetsFilePath).getName();
		final String filePath = c.getFilesDir() + "/" + fileName;
		File toFile = new File(filePath);
		if (toFile.exists()) {
			Log.d(Config.TAG, filePath + " file is exists!");
		} else {
			try {
				InputStream inputStream = c.getResources().getAssets()
						.open(assetsFilePath);
				FileOutputStream outStream = c.openFileOutput(fileName,
						Context.MODE_PRIVATE);
				byte[] data = new byte[1024 * 100];
				for (int i = inputStream.read(data); i > 0; i = inputStream
						.read(data)) {
					outStream.write(data, 0, i);
				}
				outStream.close();
				inputStream.close();

				if (permissions != null) {
					r = chmod(filePath, permissions);// Shell.exec("chmod " +
														// permissions + " " +
														// filePath);
				}
			} catch (IOException e) {
				Log.d(Config.TAG, e.getMessage());
			}
			Log.d(Config.TAG, filePath + " copy ok!");
		}

		return r;
	}

	public static int chmod(String filePath, String permissions) {
		return Shell.runCommand("chmod " + permissions + " " + filePath);
	}

	public static int chmod(String path, int mode) {
		int r = -1;// ?
		Class<?> fileUtils;
		try {
			fileUtils = Class.forName("android.os.FileUtils");

			Method setPermissions = fileUtils.getMethod("setPermissions",
					String.class, int.class, int.class, int.class);
			r = (Integer) setPermissions.invoke(null, path, mode, -1, -1);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return r;
	}
}
