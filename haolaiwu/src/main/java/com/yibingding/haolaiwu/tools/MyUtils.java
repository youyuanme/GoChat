package com.yibingding.haolaiwu.tools;

import java.io.File;
import java.math.BigDecimal;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.ybd.app.BaseActivity;

public class MyUtils {

	public static void showToast(final Context ctx, final String showMsg) {
		String currentThreadName = Thread.currentThread().getName();
		if (currentThreadName.equals("Main")) {
			Toast.makeText(ctx, showMsg, 0).show();
		} else {
			ThreadUtils.runUIThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(ctx, showMsg, 0).show();
				}
			});
		}
	}

	public static int getWindowWidth(Context ctx) {
		int width = 0;
		WindowManager wm = (WindowManager) ctx
				.getSystemService(Context.WINDOW_SERVICE);
		width = wm.getDefaultDisplay().getWidth();// 手机屏幕的宽度
		return width;
	}

	public static int getWindowHeight(Context ctx) {
		int width = 0;
		WindowManager wm = (WindowManager) ctx
				.getSystemService(Context.WINDOW_SERVICE);
		width = wm.getDefaultDisplay().getHeight();// 手机屏幕的高度
		return width;
	}

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static PopupWindow getPopWindow(View contentView) {
		PopupWindow pop = new PopupWindow(contentView,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setTouchable(true);
		return pop;
	}

	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	public static boolean isPhoneNumber(String phoneNumber) {
		String regex = "1[3578]\\d{9}";
		if (TextUtils.isEmpty(phoneNumber)) {
			return false;
		}
		return phoneNumber.matches(regex);
	}

	public static void setBackAlpha(Context ctx, float alpha) {
		WindowManager.LayoutParams params = ((BaseActivity) ctx).getWindow()
				.getAttributes();
		params.alpha = alpha;
		((BaseActivity) ctx).getWindow().setAttributes(params);
	}

	public static String getTotalCacheSize(Context context) throws Exception {
		long cacheSize = getFolderSize(context.getCacheDir());
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			cacheSize += getFolderSize(context.getExternalCacheDir());
		}
		return getFormatSize(cacheSize);
	}
	
	public static String getTotalACacheSize(Context context)throws Exception{
		File file = new File(context.getCacheDir(), "ACache");
		if (!file.exists()) {
			return "0k";
		}
		long cacheSize = getFolderSize(file);
		return getFormatSize(cacheSize);
	}

	public static void clearAllCache(Context context) {
		deleteDir(context.getCacheDir());
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			deleteDir(context.getExternalCacheDir());
		}
	}
	public static void clearACache(Context context){
		File file = new File(context.getCacheDir(), "ACache");
		deleteDir(file);
	}
	
	private static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		 return dir.delete();
	}

	// 获取文件
	// Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/
	// 目录，一般放一些长时间保存的数据
	// Context.getExternalCacheDir() -->
	// SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
	public static long getFolderSize(File file) throws Exception {
		long size = 0;
		try {
			File[] fileList = file.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				// 如果下面还有文件
				if (fileList[i].isDirectory()) {
					size = size + getFolderSize(fileList[i]);
				} else {
					size = size + fileList[i].length();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;
	}

	/**
	 * 格式化单位
	 * 
	 * @param size
	 * @return
	 */
	public static String getFormatSize(double size) {
		double kiloByte = size / 1024;
		if (kiloByte < 1) {
			// return size + "Byte";
			return "0K";
		}

		double megaByte = kiloByte / 1024;
		if (megaByte < 1) {
			BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
			return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "KB";
		}

		double gigaByte = megaByte / 1024;
		if (gigaByte < 1) {
			BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
			return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "MB";
		}

		double teraBytes = gigaByte / 1024;
		if (teraBytes < 1) {
			BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
			return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "GB";
		}
		BigDecimal result4 = new BigDecimal(teraBytes);
		return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
				+ "TB";
	}

}
