package com.yibingding.haolaiwu.tools;

import android.os.Handler;

public class ThreadUtils {
	/**
	 * 运行在子线程
	 * 
	 * @param run
	 */
	public static void runInThread(Runnable run) {
		new Thread(run).start();

	}

	private static Handler handler = MyApplication.handler;

	/**
	 * 运行在UI线程
	 * 
	 * @param run
	 */
	public static void runUIThread(Runnable run) {
		handler.post(run);
	}


}
