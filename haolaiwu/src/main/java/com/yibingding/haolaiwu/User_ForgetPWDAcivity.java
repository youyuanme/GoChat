package com.yibingding.haolaiwu;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ybd.app.BaseActivity;
import com.ybd.app.tools.StringUtils;
import com.ybd.app.volley.VolleyPost;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.Constants;
import com.yibingding.haolaiwu.tools.MyUtils;

public class User_ForgetPWDAcivity extends BaseActivity {
	TextView name, pwd, pwd1, verification;
	TextView getverification;
	View submit;
	Timer timer;
	String code;
	boolean fromlogin;
	ProgressDialog dialog;

	@Override
	public void onCreateThisActivity() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		setContentView(R.layout.user_forgetpwd);
		name = (TextView) findViewById(R.id.name);
		pwd = (TextView) findViewById(R.id.pwd);
		pwd1 = (TextView) findViewById(R.id.pwd1);
		verification = (TextView) findViewById(R.id.verification);
		getverification = (TextView) findViewById(R.id.getverification);
		submit = findViewById(R.id.submit);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		fromlogin = getIntent().getBooleanExtra("fromlogin", false);

	}

	public void getverification(View v) {
		if (getverification.getText().toString().contains("获取")) {
			CharSequence namstr = name.getText();
			if (namstr == null || namstr.toString().equals("")) {
				Toast.makeText(this, "请输入手机号！", Toast.LENGTH_SHORT).show();
				return;
			}
			if (MyUtils.isPhoneNumber(namstr.toString())) {
				Map<String, String> params = new HashMap<String, String>();
				params.put("userMobile", namstr.toString());
				params.put("Token", AESUtils.encode("userMobile"));
				if (dialog == null) {
					dialog = new ProgressDialog(this);
					dialog.setCanceledOnTouchOutside(false);
					dialog.setMessage("正在努力加载中...");
				}
				dialog.show();
				VolleyPost request = new VolleyPost(this, Constants.SERVER_IP
						+ Constants.SMS_URL, params) {

					@Override
					public void pullJson(String json) {
						// TODO Auto-generated method stub

						if (dialog != null && dialog.isShowing()) {
							dialog.dismiss();
						}
						if (json == null || json.equals("")) {
							Toast.makeText(User_ForgetPWDAcivity.this,
									"网络连接有问题！", Toast.LENGTH_SHORT).show();
							return;
						}
						JSONArray array = JSONArray.parseArray(json);
						JSONObject obj = array.getJSONObject(0);
						if (obj.getString("state").equals("true")) {
							code = obj.getString("code");
							timer = new Timer();
							TimerTask task = new TimerTask() {
								int count = 60;

								@Override
								public void run() {
									// TODO Auto-generated method stub
									runOnUiThread(new Runnable() {
										@Override
										public void run() {
											// TODO Auto-generated method stub
											if (count > 0) {
												getverification.setText(count
														+ "s后重新获取");
											} else {
												getverification
														.setText("获取验证码");
												timer.cancel();
												timer = null;
											}
											count--;
										}
									});
								}
							};
							timer.schedule(task, 1000, 1000);
						}
					}

					@Override
					public String getPageIndex() {
						// TODO Auto-generated method stub
						return null;
					}
				};
			} else {
				Toast.makeText(this, "手机格式不正确！", Toast.LENGTH_SHORT).show();
			}

		}
	}

	public void submit(View v) {
		CharSequence namstr = name.getText();
		CharSequence pwdstr = pwd.getText();
		CharSequence pwd1str = pwd1.getText();
		CharSequence verificationstr = verification.getText();
		if (!MyUtils.isPhoneNumber(namstr.toString())) {
			Toast.makeText(this, "请输入正确的手机号！", Toast.LENGTH_SHORT).show();
			return;
		}
		if (verificationstr == null || verificationstr.toString().equals("")) {
			Toast.makeText(this, "请输入验证码！", Toast.LENGTH_SHORT).show();
			return;
		}
		if (pwdstr == null || pwdstr.toString().equals("")) {
			Toast.makeText(this, "请输入新密码！", Toast.LENGTH_SHORT).show();
			return;
		}
		if (pwd1str == null || pwd1str.toString().equals("")) {
			Toast.makeText(this, "请输入确认密码！", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!pwd1str.toString().equals(pwdstr.toString())) {
			Toast.makeText(this, "请确认输入的密码一致！", Toast.LENGTH_SHORT).show();
			pwd1.setText("");
			pwd1.requestFocus();
			return;
		}
		if (!verificationstr.toString().equals(code)) {
			Toast.makeText(this, "验证码不正确！", Toast.LENGTH_SHORT).show();
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("userMobile", namstr.toString());
		params.put("userPwd", pwdstr.toString());
		params.put("code", code + "");
		params.put("Token", AESUtils.encode("userMobile"));
		if (dialog == null) {
			dialog = new ProgressDialog(this);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setMessage("正在努力加载中...");
		}
		dialog.show();
		VolleyPost request = new VolleyPost(this, Constants.SERVER_IP
				+ Constants.User_ForgetPwd_URL, params) {

			@Override
			public void pullJson(String json) {
				// TODO Auto-generated method stub
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
				JSONArray array = JSONArray.parseArray(json);
				JSONObject obj = array.getJSONObject(0);
				if (obj.getString("state").equals("true")) {
					Toast.makeText(User_ForgetPWDAcivity.this, "密码修改成功请直接登陆！",
							Toast.LENGTH_SHORT).show();
					if (fromlogin) {
						setResult(1, getIntent());
					}
					finish();
				}
			}

			@Override
			public String getPageIndex() {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}

	public void back(View v) {
		finish();
	}
}
