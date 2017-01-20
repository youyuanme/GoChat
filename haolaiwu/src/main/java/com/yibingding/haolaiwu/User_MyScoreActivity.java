package com.yibingding.haolaiwu;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ybd.app.BaseActivity;
import com.ybd.app.tools.PreferenceHelper;
import com.ybd.app.volley.VolleyPost;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.Constants;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class User_MyScoreActivity extends BaseActivity {
	TextView value;
	String detals, total;
	ProgressDialog dialog;

	@Override
	public void onCreateThisActivity() {
		setContentView(R.layout.user_myscore);

	}

	@Override
	public void initViews() {
		value = (TextView) findViewById(R.id.value);
	}

	@Override
	public void initData() {
		getData();
	}

	public void back(View v) {
		finish();
	}

	public void details(View v) {
		Intent intent = new Intent(this, User_MyScoreDetailsActivity.class);
		intent.putExtra("data", detals);
		intent.addFlags(2);// 1是佣金2是积分
		startActivity(intent);
	}

	private void getData() {
		Map<String, String> params = new HashMap<String, String>();
		String guid = PreferenceHelper.readString(this, "userinfo", "guid");
		if (guid == null || guid.equals("")) {
			return;
		}
		params.put("userGuid", guid);
		params.put("Token", AESUtils.encode("userGuid"));
		if (dialog == null) {
			dialog = new ProgressDialog(this);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setMessage("正在努力加载中...");
		}
		dialog.show();
		VolleyPost request = new VolleyPost(this, Constants.SERVER_IP
				+ Constants.User_Score_URL, params) {

			@Override
			public void pullJson(String json) {
				// TODO Auto-generated method stub

				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				if (json == null || json.equals("")) {
					Toast.makeText(User_MyScoreActivity.this, "网络连接有问题！",
							Toast.LENGTH_SHORT).show();
					return;
				}
				JSONArray array = JSONArray.parseArray(json);
				JSONObject jsonObject = array.getJSONObject(0);
				if (jsonObject.getString("state").equals("true")) {
					array = jsonObject.getJSONArray("result");
					total = array.getJSONObject(0).getString("Reward");
					detals = array.toJSONString();
					value.setText(total);
				} else if (jsonObject.getString("state").equals("false")) {
					// Toast.makeText(User_MyScoreActivity.this,
					// "提示信息：" + jsonObject.getString("result"),
					// Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public String getPageIndex() {
				return null;
			}
		};
	}
}
