package com.yibingding.haolaiwu.tools;

import java.util.HashMap;

import android.R.integer;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import cn.jpush.android.data.r;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

import com.yibingding.haolaiwu.R;

public class AutoShareSDK {
	static Context context;
	static Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 3:
				Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	};

	public static void wechat(final Context con, String titile, String url,
			String text, String imagePath, String strname) {
		// 获取单个平台
		context = con;
		Platform plat = ShareSDK.getPlatform(strname);
		String name = plat.getName();
		boolean isWechat = "WechatMoments".equals(name)
				|| "Wechat".equals(name) || "WechatFavorite".equals(name);
		if (isWechat && !plat.isClientValid()) {
			Message msg = new Message();
			msg.what = 3;
			int resId = R.string.wechat_client_inavailable;
			msg.obj = con.getString(resId);
			handler.sendMessage(msg);
			return;
		}
		ShareParams sp = new ShareParams();
		sp.setTitle(titile);
		sp.setTitleUrl(url);
		sp.setText(text);
		// sp.setImagePath(imagePath);
		sp.setAddress("");
		sp.setShareType(Platform.SHARE_TEXT);
		plat.setPlatformActionListener(new PlatformActionListener() {
			public void onError(Platform platform, int action, Throwable t) {
				// 操作失败的处理代码
				Message msg = new Message();
				msg.what = 3;
				int resId = R.string.share_failed;
				msg.obj = con.getString(resId);
				handler.sendMessage(msg);
			}

			public void onComplete(Platform platform, int action,
					HashMap<String, Object> res) {
				// 操作成功的处理代码
				Message msg = new Message();
				msg.what = 3;
				int resId = R.string.share_completed;
				msg.obj = con.getString(resId);
				handler.sendMessage(msg);

			}

			public void onCancel(Platform platform, int action) {
				// 操作取消的处理代码
				Message msg = new Message();
				msg.what = 3;
				int resId = R.string.share_canceled;
				msg.obj = con.getString(resId);
				handler.sendMessage(msg);
			}
		});
		plat.share(sp);
	}

	public static void wechat2(final Context con, String titile, String url,
			String text, String imagePath, int iamgeID, String strname) {
		// 获取单个平台
		context = con;
		Platform plat = ShareSDK.getPlatform(strname);
		String name = plat.getName();
		boolean isWechat = "WechatMoments".equals(name)
				|| "Wechat".equals(name) || "WechatFavorite".equals(name);
		if (isWechat && !plat.isClientValid()) {
			Message msg = new Message();
			msg.what = 3;
			int resId = R.string.wechat_client_inavailable;
			msg.obj = con.getString(resId);
			handler.sendMessage(msg);
			return;
		}
		ShareParams sp = new ShareParams();
		sp.setTitle(titile);
		sp.setTitleUrl(url);
		sp.setUrl(url);
		sp.setText(text);
		// sp.setImagePath(imagePath);
		if (iamgeID == -1) {
			sp.setImageUrl(imagePath);
		} else {
			sp.setImageData(BitmapFactory.decodeResource(
					context.getResources(), iamgeID));
		}
		sp.setAddress("");
		sp.setShareType(Platform.SHARE_WEBPAGE);
		plat.setPlatformActionListener(new PlatformActionListener() {
			public void onError(Platform platform, int action, Throwable t) {
				// 操作失败的处理代码
				Message msg = new Message();
				msg.what = 3;
				int resId = R.string.share_failed;
				msg.obj = con.getString(resId);
				handler.sendMessage(msg);
			}

			public void onComplete(Platform platform, int action,
					HashMap<String, Object> res) {
				// 操作成功的处理代码
				Message msg = new Message();
				msg.what = 3;
				int resId = R.string.share_completed;
				msg.obj = con.getString(resId);
				handler.sendMessage(msg);

			}

			public void onCancel(Platform platform, int action) {
				// 操作取消的处理代码
				Message msg = new Message();
				msg.what = 3;
				int resId = R.string.share_canceled;
				msg.obj = con.getString(resId);
				handler.sendMessage(msg);
			}
		});
		plat.share(sp);
	}
}
