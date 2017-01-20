package com.yibingding.haolaiwu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ybd.app.BaseActivity;
import com.ybd.app.tools.Tools;
import com.ybd.app.volley.VolleyPost;
import com.yibingding.haolaiwu.domian.User;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.Constants;
import com.yibingding.haolaiwu.tools.MyUtils;

public class User_RegistActivity extends BaseActivity {

	private TextView name, pwd, pwd1, verification;
	private List<User> list = new ArrayList<User>();
	private Spinner spinner;
	private String code;
	private TextView getverification;
	private View submit;
	private Timer timer;
	private boolean fromlogin;
	private ProgressDialog dialog;

	@Override
	public void onCreateThisActivity() {
		setContentView(R.layout.user_regist);
	}

	@Override
	public void initViews() {
		name = (TextView) findViewById(R.id.name);
		pwd = (TextView) findViewById(R.id.pwd);
		pwd1 = (TextView) findViewById(R.id.pwd1);
		verification = (TextView) findViewById(R.id.verification);
		spinner = (Spinner) findViewById(R.id.spinner);
		getverification = (TextView) findViewById(R.id.getverification);
		submit = findViewById(R.id.submit);
	}

	@Override
	public void initData() {
		fromlogin = getIntent().getBooleanExtra("fromlogin", false);
		getManager();
	}

	public void back(View v) {
		finish();
	}

	public void submit(View v) {
		CharSequence namstr = name.getText();
		CharSequence pwdstr = pwd.getText();
		CharSequence pwd1str = pwd1.getText();
		CharSequence verificationstr = verification.getText();
		if (namstr == null || namstr.toString().equals("")) {
			Tools.showToast(this, "请输入手机号!");
			return;
		}
		if (pwdstr == null || pwdstr.toString().equals("")) {
			Tools.showToast(this, "请输入密码!");
			return;
		}
		if (pwd1str == null || pwd1str.toString().equals("")) {
			Toast.makeText(this, "请输入确认密码!", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!pwd1str.toString().equals(pwdstr.toString())) {
			Toast.makeText(this, "请确认输入的密码一致!", Toast.LENGTH_SHORT).show();
			pwd1.setText("");
			pwd1.requestFocus();
			return;
		}
		if (verificationstr == null || verificationstr.toString().equals("")) {
			Toast.makeText(this, "请输入验证码!", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!verificationstr.toString().equals(code)) {
			Toast.makeText(this, "输入的验证码不正确!", Toast.LENGTH_SHORT).show();
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("userMobile", namstr.toString());
		params.put("userPwd", pwdstr.toString());
		if (!list.isEmpty()) {
			if (spinner.getSelectedItemPosition() == 0) {
				params.put("leader", "");
			} else {
				params.put("leader",
						list.get(spinner.getSelectedItemPosition() - 1)
								.getGuid());
			}
		} else {
			params.put("leader", "");
		}
		params.put("userType", "");
		params.put("Token", AESUtils.encode("userMobile"));
		if (dialog == null) {
			dialog = new ProgressDialog(this);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setMessage("正在努力加载中...");

		}
		dialog.show();
		VolleyPost request = new VolleyPost(this, Constants.SERVER_IP
				+ Constants.User_Regist_URL, params) {
			@Override
			public void pullJson(String json) {
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				if (json == null || json.equals("")) {
					Tools.showToast(context,
							getResources().getString(R.string.lianwangshibai));
					return;
				}
				JSONArray array = JSONArray.parseArray(json);
				JSONObject obj = array.getJSONObject(0);

				if (obj.getString("state").equals("true")) {
					array = obj.getJSONArray("result");
					obj = array.getJSONObject(0);
					Log.v("this", json);
					SharedPreferences preference = getSharedPreferences(
							"userinfo", Context.MODE_PRIVATE);
					Editor editor = preference.edit();
					editor.putString("guid", obj.getString("Guid"));
					editor.putString("userid", obj.getString("t_User_LoginId"));
					editor.putString("realname",
							obj.getString("t_User_RealName"));
					editor.putString("nickname",
							obj.getString("t_User_NickName"));
					editor.putString("sex", obj.getString("t_User_Sex"));
					editor.putString("birth", obj.getString("t_User_Birth"));
					editor.putString("mobile", obj.getString("t_User_Mobile"));
					editor.putString("avatar", obj.getString("t_User_Pic"));
					editor.putString("tStyle", obj.getString("t_User_Style"));
					editor.putString("style", obj.getString("UserStyle"));
					editor.putString("date", obj.getString("t_User_Date"));
					editor.putString("leader", obj.getString("t_User_Leader"));
					editor.putString("leaderid", obj.getString("LeaderLoginId"));
					editor.putString("leaderrealname",
							obj.getString("LeaderRealName"));
					editor.putString("accunt", obj.getString("Account"));
					editor.putString("Integral", obj.getString("Integral"));
					editor.putString("accountmsgcomplete",
							obj.getString("t_User_Complete"));
					editor.commit();
					Toast.makeText(User_RegistActivity.this, "注册成功并登陆",
							Toast.LENGTH_SHORT).show();
					if (fromlogin) {
						setResult(1, getIntent());
					}
					finish();
				} else if (obj.getString("state").equals("false")) {

					Toast.makeText(User_RegistActivity.this,
							obj.getString("result"), Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public String getPageIndex() {
				return null;
			}
		};
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
						if (dialog != null && dialog.isShowing()) {
							dialog.dismiss();
						}
						if (json == null || json.equals("")) {
							Tools.showToast(
									context,
									getResources().getString(
											R.string.lianwangshibai));
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
									runOnUiThread(new Runnable() {
										@Override
										public void run() {
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
						return null;
					}
				};
			} else {
				Toast.makeText(this, "请输入正确的手机号！", Toast.LENGTH_SHORT).show();
			}

		} else {
			CharSequence verstr = verification.getText();
			if (verstr != null && verstr.toString().equals(code)) {
				Toast.makeText(this, "验证成功！", Toast.LENGTH_SHORT).show();
				getverification.setText("获取验证码");
				if (timer != null) {
					timer.cancel();
					timer = null;
				}
				submit.setEnabled(true);
			}
		}

	}

	public void getManager() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userStyle", 3 + "");
		params.put("userGuid", "");
		params.put("Token", AESUtils.encode("userStyle"));
		if (dialog == null) {
			dialog = new ProgressDialog(this);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setMessage("正在努力加载中...");
		}
		dialog.show();
		VolleyPost request = new VolleyPost(this, Constants.SERVER_IP
				+ Constants.User_GetUserListByStyle_URL, params) {

			@Override
			public void pullJson(String json) {
				// TODO Auto-generated method stub
				// Log.v("this", "获取的经理列表"+json);

				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				if (json == null || json.equals("")) {
					Tools.showToast(context,
							getResources().getString(R.string.lianwangshibai));
					return;
				}
				JSONArray array = JSONArray.parseArray(json);
				JSONObject obj = array.getJSONObject(0);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						User_RegistActivity.this, R.layout.regist_spinner_item);
				adapter.add("不选择");
				if (obj.getString("state").equals("true")) {
					String v = obj.getString("result");
					list = JSON.parseArray(v, User.class);
					for (User user : list) {
						adapter.add(user.getT_User_RealName());
					}
					spinner.setAdapter(adapter);
				}
			}

			@Override
			public String getPageIndex() {
				return null;
			}
		};
	}
}
