package com.yibingding.haolaiwu;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ybd.app.BaseActivity;
import com.ybd.app.MyBaseApplication;
import com.ybd.app.tools.PreferenceHelper;
import com.ybd.app.volley.VolleyPost;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.Constants;
import com.yibingding.haolaiwu.tools.MyApplication;

public class User_AlterPwdActivity extends BaseActivity {
	TextView oldpwd, pwd, pwd1;
	ProgressDialog dialog;
	MyBaseApplication myApp;

	@Override
	public void onCreateThisActivity() {
		setContentView(R.layout.user_alterpwd);
		myApp = (MyBaseApplication) getApplication();
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		oldpwd = (TextView) findViewById(R.id.oldpwd);
		pwd = (TextView) findViewById(R.id.pwd);
		pwd1 = (TextView) findViewById(R.id.pwd1);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
	}

	public void back(View v) {
		finish();
	}

	public void submit(View v) {
		CharSequence namstr = oldpwd.getText();
		CharSequence pwdstr = pwd.getText();
		CharSequence pwd1str = pwd1.getText();
		if (namstr == null || namstr.toString().equals("")) {
			Toast.makeText(this, "请输入旧密码！", Toast.LENGTH_SHORT).show();
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
		if (dialog == null) {
			dialog = new ProgressDialog(this);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setMessage("正在努力加载中...");
		}
		dialog.show();
		Map<String, String> params = new HashMap<String, String>();
		params.put("userGuid",
				PreferenceHelper.readString(this, "userinfo", "guid"));
		params.put("userPwd", pwdstr.toString());
		params.put("oldUserPwd", namstr.toString());
		params.put("Token", AESUtils.encode("userGuid"));
		VolleyPost request = new VolleyPost(this, Constants.SERVER_IP
				+ Constants.User_AlertPwd_URL, params) {
			@Override
			public void pullJson(String json) {
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				if (json == null || json.equals("")) {
					Toast.makeText(User_AlterPwdActivity.this, "网络连接有问题！",
							Toast.LENGTH_SHORT).show();
					return;
				}
				JSONArray array = JSONArray.parseArray(json);
				JSONObject obj = array.getJSONObject(0);
				if (obj.getString("state").equals("true")) {
					for (int i = 0; i < myApp.list_activities.size(); i++) {
						if (!(myApp.list_activities.get(i) instanceof MainActivity)) {
							myApp.list_activities.get(i).finish();
						}
					}
					// User_AlterPwdActivity.this.finish();
					Toast.makeText(User_AlterPwdActivity.this, "修改成功并登陆",
							Toast.LENGTH_SHORT).show();
				} else if (obj.getString("state").equals("false")) {
					Toast.makeText(User_AlterPwdActivity.this,
							obj.getString("result"), Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public String getPageIndex() {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}
}
