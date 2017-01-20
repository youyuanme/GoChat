package com.yibingding.haolaiwu;

import android.content.Intent;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import cn.jpush.android.api.JPushInterface;
import com.ybd.app.BaseActivity;
import com.ybd.app.tools.PreferenceHelper;

public class WelcomeActivity extends BaseActivity {

	private Boolean notSplash;

	@Override
	public void onCreateThisActivity() {
		int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
		Window window = this.getWindow();
		window.setFlags(flag, flag);
		setContentView(R.layout.activity_welcome);
		notSplash = PreferenceHelper.readBoolean(this, "userinfo", "isSplash",
				false);
		gotoYindaoye();
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResume() {
		JPushInterface.onResume(this);
		super.onResume();
	}

	@Override
	protected void onPause() {
		JPushInterface.onPause(this);
		super.onPause();
	}

	private void gotoYindaoye() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = null;
				if (notSplash) {
					intent = new Intent(WelcomeActivity.this,
							MainActivity.class);
				} else {
					intent = new Intent(WelcomeActivity.this,
							SplashActivit.class);
				}
				startActivity(intent);
				finish();
			}
		}, 3000);
	}

}
