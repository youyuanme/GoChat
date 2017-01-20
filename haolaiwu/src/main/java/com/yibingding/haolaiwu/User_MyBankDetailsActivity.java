package com.yibingding.haolaiwu;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ybd.app.BaseActivity;
import com.ybd.app.tools.PreferenceHelper;
import com.ybd.app.volley.VolleyPost;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.Constants;

public class User_MyBankDetailsActivity extends BaseActivity {
	TextView name, no;
	int tempindex;
	String guid;
	ProgressDialog dialog;

	@Override
	public void onCreateThisActivity() {
		setContentView(R.layout.user_mybankdetails);

	}

	@Override
	public void initViews() {
		no = (TextView) findViewById(R.id.value);
		name = (TextView) findViewById(R.id.name);

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		name.setText(getIntent().getStringExtra("name"));
		String noString = getIntent().getStringExtra("no");
		no.setText("卡号：" + noString.substring(0, 4) + "****"
				+ noString.substring(noString.length() - 4, noString.length()));
		tempindex = getIntent().getIntExtra("index", -1);
		guid = getIntent().getStringExtra("guid");

	}

	public void back(View v) {
		finish();
	}

	public void unband(View v) {
		if (tempindex > -1) {
			Map<String, String> params = new HashMap<String, String>();
			params.put("Guid", guid);
			params.put("Token", AESUtils.encode("Guid"));
			if (dialog == null) {
				dialog = new ProgressDialog(this);
				dialog.setCanceledOnTouchOutside(false);
				dialog.setMessage("正在努力加载中...");
			}
			dialog.show();
			VolleyPost request = new VolleyPost(this, Constants.SERVER_IP
					+ Constants.User_UnBandBank_URL, params) {

				@Override
				public void pullJson(String json) {
					// TODO Auto-generated method stub
					if (dialog != null && dialog.isShowing()) {
						dialog.dismiss();
					}
					if (json == null || json.equals("")) {
						Toast.makeText(User_MyBankDetailsActivity.this,
								"网络连接有问题！", Toast.LENGTH_SHORT).show();
						return;
					}
					JSONArray array = JSONArray.parseArray(json);
					JSONObject obj = array.getJSONObject(0);
					if (obj.getString("state").equals("true")) {
						Toast.makeText(User_MyBankDetailsActivity.this,
								"解除绑定成功！", Toast.LENGTH_SHORT).show();
						Intent intent = getIntent();
						intent.putExtra("index", tempindex);
						setResult(1, intent);
						finish();
					} else if (obj.getString("state").equals("false")) {
						Toast.makeText(User_MyBankDetailsActivity.this,
								"提示信息：" + obj.getString("reulst"),
								Toast.LENGTH_SHORT).show();
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
}
