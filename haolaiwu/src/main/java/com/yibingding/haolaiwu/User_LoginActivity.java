package com.yibingding.haolaiwu;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.jpush.android.data.JPushLocalNotification;

import com.ybd.app.BaseActivity;
import com.ybd.app.tools.PreferenceHelper;
import com.ybd.app.tools.Tools;
import com.ybd.app.volley.VolleyPost;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.Constants;
import com.yibingding.haolaiwu.tools.MyUtils;

public class User_LoginActivity extends BaseActivity {
	TextView name, pwd;
	ProgressDialog dialog;

	@Override
	public void onCreateThisActivity() {
		setContentView(R.layout.user_login);
	}

	@Override
	public void initViews() {
		name = (TextView) findViewById(R.id.name);
		pwd = (TextView) findViewById(R.id.pwd);
	}

	@Override
	public void initData() {
		String userid = PreferenceHelper.readString(this, "userinfo", "userid");
		if (userid == null) {
			userid = "";
		}
		name.setText(userid);

	}

	public void back(View v) {
		finish();
	}

	public void submit(View v) {
		CharSequence namestr = name.getText();
		if (!MyUtils.isPhoneNumber(namestr.toString())) {
			Toast.makeText(this, "请输入 正确的手机号", Toast.LENGTH_SHORT).show();
			return;
		}
		CharSequence pwdstr = pwd.getText();
		if (pwdstr == null || pwdstr.toString().equals("")) {
			Toast.makeText(this, "请输入 密码", Toast.LENGTH_SHORT).show();
			return;
		}
		TelephonyManager tm = (TelephonyManager) getBaseContext()
				.getSystemService(TELEPHONY_SERVICE);
		Map<String, String> params = new HashMap<String, String>();
		params.put("userMobile", namestr.toString());
		params.put("userPwd", pwdstr.toString());
		params.put("androidRid", JPushInterface.getRegistrationID(this));
		params.put("IOSRid", "");
		params.put("Token", AESUtils.encode("userMobile"));
		System.out
		.println("===========JPushInterface.getRegistrationID(this)==="
				+ JPushInterface.getRegistrationID(this));
		if (dialog == null) {
			dialog = new ProgressDialog(this);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setMessage("正在努力加载中...");
		}
		dialog.show();
		VolleyPost post = new VolleyPost(this, Constants.SERVER_IP
				+ Constants.User_Login_URL, params) {

			@Override
			public void pullJson(String json) {
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				if (TextUtils.isEmpty(json)) {
					Tools.showToast(context,
							getResources().getString(R.string.lianwangshibai));
					return;
				}
				parseJSON(json);
			}

			@Override
			public String getPageIndex() {
				// TODO Auto-generated method stub
				return null;
			}
		};
		// 获取app下载地址
		// Map<String, String> params1 = new HashMap<String, String>();
		// params1.put("Token", AESUtils.encode("Token"));
		// // Log.e("======", "=======token==="+AESUtils.encode("Token"));
		// VolleyPost post1 = new VolleyPost(this, Constants.GET_DOWN_LOAD_SRC,
		// params1) {
		// @Override
		// public void pullJson(String json) {
		// // TODO Auto-generated method stub
		// if (dialog != null && dialog.isShowing()) {
		// dialog.dismiss();
		// }
		// if (TextUtils.isEmpty(json)) {
		// Tools.showToast(context,
		// getResources().getString(R.string.lianwangshibai));
		// return;
		// }
		// try {
		// String downloadAPPURL = new JSONArray(json)
		// .getJSONObject(0).getString("result");
		// SharedPreferences preference = context
		// .getSharedPreferences("userinfo",
		// Context.MODE_PRIVATE);
		// preference.edit()
		// .putString("downLoadAppURL", downloadAPPURL)
		// .commit();
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		//
		// @Override
		// public String getPageIndex() {
		// // TODO Auto-generated method stub
		// return null;
		// }
		// };
	}

	public void regist(View v) {
		Intent intent = new Intent(this, User_RegistActivity.class);
		intent.putExtra("fromlogin", true);
		startActivityForResult(intent, 1);
	}

	public void forgetpwd(View v) {
		Intent intent = new Intent(this, User_ForgetPWDAcivity.class);
		intent.putExtra("fromlogin", true);
		startActivityForResult(intent, 1);
	}

	private void parseJSON(String json) {
		try {
			JSONArray jsonArray = new JSONArray(json);
			JSONObject obj = jsonArray.getJSONObject(0);
			String state = obj.getString("state");
			if (state.equals("true")) {
				jsonArray = obj.getJSONArray("result");
				obj = jsonArray.getJSONObject(0);
				SharedPreferences preference = this.getSharedPreferences(
						"userinfo", Context.MODE_PRIVATE);
				Editor editor = preference.edit();
				editor.putString("guid", obj.getString("Guid"));
				editor.putString("userid", obj.getString("t_User_LoginId"));
				editor.putString("realname", obj.getString("t_User_RealName"));
				editor.putString("nickname", obj.getString("t_User_NickName"));
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
				finish();
			} else if (state.equals("false")) {

				Toast.makeText(this, obj.getString("result"),
						Toast.LENGTH_SHORT).show();
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if (arg0 == 1 && arg1 == 1) {
			finish();
		}
	}
}
