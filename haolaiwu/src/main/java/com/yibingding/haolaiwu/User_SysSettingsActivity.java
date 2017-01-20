package com.yibingding.haolaiwu;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.ybd.app.BaseActivity;
import com.ybd.app.tools.Tools;
import com.yibingding.haolaiwu.dialog.DialogUnLogin;
import com.yibingding.haolaiwu.dialog.User_SimpleDialog;
import com.yibingding.haolaiwu.tools.MyUtils;

public class User_SysSettingsActivity extends BaseActivity {

	private TextView cache, tv_banbenhao;
	private Button bt_unlogin;
	private User_SimpleDialog dialogCallPhone;
	private DialogUnLogin dialog;
	private User_SimpleDialog dialogClearcache;

	@Override
	public void onCreateThisActivity() {
		setContentView(R.layout.user_syssettings);
	}

	@Override
	public void initViews() {
		cache = (TextView) findViewById(R.id.cache);
		bt_unlogin = (Button) findViewById(R.id.bt_unlogin);
		tv_banbenhao = (TextView) findViewById(R.id.tv_banbenhao);
		dialogCallPhone = new User_SimpleDialog(this);
		dialogClearcache = new User_SimpleDialog(this);
		dialog = new DialogUnLogin(this);
	}

	@Override
	public void initData() {
		if (TextUtils.isEmpty(getVersion())) {
			Tools.showToast(this, "获取版本号失败!");
		} else {
			tv_banbenhao.setText("v" + getVersion());
		}
		if (TextUtils.isEmpty(getSharedPreferences("userinfo",
				Context.MODE_PRIVATE).getString("guid", ""))) {
			bt_unlogin.setVisibility(View.INVISIBLE);
		}
		try {
			cache.setText(MyUtils.getTotalACacheSize(this));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		if (dialogCallPhone != null && dialogCallPhone.isShowing()) {
			dialogCallPhone.dismiss();
		}
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
		super.onDestroy();
	}

	public void back(View v) {
		finish();
	}

	public void onUnlogin(View v) {
		System.out.println("================");
		dialog.setConfirmListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				SharedPreferences preference = User_SysSettingsActivity.this
						.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
				Editor editor = preference.edit();
				editor.putString("guid", "");
				editor.putString("userid", "");
				editor.putString("realname", "");
				editor.putString("nickname", "");
				editor.putString("sex", "");
				editor.putString("birth", "");
				editor.putString("mobile", "");
				editor.putString("avatar", "");
				editor.putString("tStyle", "");
				editor.putString("style", "");
				editor.putString("date", "");
				editor.putString("leader", "");
				editor.putString("leaderid", "");
				editor.putString("leaderrealname", "");
				editor.putString("accunt", "");
				editor.putString("Integral", "");
				editor.putString("accountmsgcomplete", "");
				editor.commit();
				dialog.dismiss();
				User_SysSettingsActivity.this.finish();
			}
		});
		dialog.show();
	}

	/*
	 * public void update(View v) {// 房贷计算器 Map<String, String> map = new
	 * HashMap<String, String>(); map.put("Token", AESUtils.encode("Token"));
	 * Comment_Volley comment_Volley = new Comment_Volley(this,
	 * Constants.GET_Counter, map); comment_Volley
	 * .setOnGetDataSuccessListener(new GetDataSuccessListener() {
	 * 
	 * @Override public void onGetDataSuccess(String tag, Object obj) { // TODO
	 * Auto-generated method stub if (obj != null) { Map<String, String> map =
	 * (Map<String, String>) obj; if (TextUtils.equals(map.get("state"),
	 * "true")) { Intent intent = new Intent( User_SysSettingsActivity.this,
	 * FangdaijisuanqiActivity.class); intent.putExtra("url",
	 * map.get("result")); startActivity(intent); } else {
	 * System.out.println("获取房贷计算器url出错"); } } } }); }
	 */
	public void faceback(View v) {
		Intent intent = new Intent(this, User_FeedBackActivity.class);
		startActivity(intent);
	}

	public void joinus(View v) {
		Intent intent = new Intent(this, User_JoinUsActivity.class);
		startActivity(intent);
	}

	public void onCallPhone(View v) {

		dialogCallPhone.setTitleText(("是否呼叫：400-012-8017"));
		dialogCallPhone.setConfirmListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(Intent.ACTION_CALL, Uri
						.parse("tel:4000128017")));
				dialogCallPhone.dismiss();
			}
		});
		dialogCallPhone.show();
	}

	public void clearcache(View v) {
		dialogClearcache.setConfirmListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// MyUtils.clearACache(User_SysSettingsActivity.this);
				deleteFile(new File(getCacheDir(), "ACache"));
				dialogClearcache.dismiss();
				User_SysSettingsActivity.this.initData();
			}
		});
		dialogClearcache.show();
		// Window dialogWindow = dialog.getWindow();
		// WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		// WindowManager windowManager = dialogWindow.getWindowManager();
		// // Display dispaly = windowManager.getDefaultDisplay();
		// // lp.width=(int) (dispaly.getWidth()*0.8);
		// lp.width=400;
		// //
		// 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,也可以用setAttributes
		// // dialog.onWindowAttributesChanged(lp);
		// dialogWindow.setAttributes(lp);
		// Window window = dialog.getWindow();
		// window.getDecorView().setPadding(0, 0, 0, 0);
		// WindowManager.LayoutParams lp = window.getAttributes();
		// lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		// lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		// window.setAttributes(lp);
		// window.setGravity(Gravity.CENTER_VERTICAL);
		// window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
		// WindowManager.LayoutParams.WRAP_CONTENT);
	}

	public void alterpwd(View v) {
		if (TextUtils.isEmpty(getSharedPreferences("userinfo",
				Context.MODE_PRIVATE).getString("guid", ""))) {
			Tools.showToast(this, "您还未登录，请登录!");
			return;
		}
		Intent intent = new Intent(this, User_AlterPwdActivity.class);
		startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		if (arg0 == 1 && arg1 == 1) {
			finish();
			super.onActivityResult(arg0, arg1, arg2);
			return;
		}
		super.onActivityResult(arg0, arg1, arg2);

	}

	public void deleteFile(File file) {
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete();
			} else if (file.isDirectory()) { // 否则如果它是一个目录
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
					this.deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
				}
			}
			// file.delete();
		}
	}

	/**
	 * 获取版本号
	 * 
	 * @return 当前应用的版本号
	 */
	public String getVersion() {
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

}
