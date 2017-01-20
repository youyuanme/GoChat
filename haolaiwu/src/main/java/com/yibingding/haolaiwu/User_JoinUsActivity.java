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
import com.ybd.app.tools.PreferenceHelper;
import com.ybd.app.volley.VolleyPost;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.Constants;

public class User_JoinUsActivity extends BaseActivity {

	private TextView tv_title, tv_content;

	@Override
	public void onCreateThisActivity() {
		// TODO Auto-generated method stub
		setContentView(R.layout.user_joinus);
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_content = (TextView) findViewById(R.id.tv_content);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		params.put("Token", AESUtils.encode("Token"));
		VolleyPost request = new VolleyPost(this, Constants.GET_JoinUS, params) {
			@Override
			public void pullJson(String json) {
				if (json == null || json.equals("")) {
					Toast.makeText(User_JoinUsActivity.this, "网络连接有问题！",
							Toast.LENGTH_SHORT).show();
					return;
				}
				JSONArray array = JSONArray.parseArray(json);
				JSONObject obj = array.getJSONObject(0);
				if (obj.getString("state").equals("true")) {
					String Title = obj.getJSONArray("result").getJSONObject(0)
							.getString("Title");
					tv_title.setText(Title);
					tv_content.setText(obj.getJSONArray("result")
							.getJSONObject(0).getString("Instruction"));
				} else {
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
