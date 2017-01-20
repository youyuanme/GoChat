package com.yibingding.haolaiwu;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ybd.app.BaseActivity;
import com.ybd.app.MyBaseApplication;
import com.ybd.app.tools.PreferenceHelper;
import com.ybd.app.tools.Tools;
import com.ybd.app.volley.VolleyPost;
import com.yibingding.haolaiwu.domian.BankCard;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.Constants;
import com.yibingding.haolaiwu.tools.MyApplication;

public class User_FeedBackActivity extends BaseActivity {
	TextView title, content;
	ProgressDialog dialog;

	@Override
	public void onCreateThisActivity() {
		setContentView(R.layout.user_feedback);

	}

	@Override
	public void initViews() {
		title = (TextView) findViewById(R.id.title);
		content = (TextView) findViewById(R.id.content);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	public void back(View v) {
		finish();
	}

	public void submit(View v) {
		CharSequence titlestr = title.getText();
		if (titlestr == null || titlestr.toString().equals("")) {
			Tools.showToast(this, "请输入标题!");
			return;
		}
		CharSequence contentstr = content.getText();
		if (content == null || content.toString().equals("")) {
			Tools.showToast(this, "请输入内容!");
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("title", titlestr.toString());
		params.put("contents", contentstr.toString());
		params.put("userGuid",
				PreferenceHelper.readString(this, "userinfo", "guid"));
		params.put("Token", AESUtils.encode("title"));
		if (dialog == null) {
			dialog = new ProgressDialog(this);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setMessage("正在努力加载中...");
		}
		dialog.show();
		System.out
				.println("=====params.toString()========" + params.toString());
		VolleyPost request = new VolleyPost(this, Constants.SERVER_IP
				+ Constants.User_AddFeedBack_URL, params) {

			@Override
			public void pullJson(String json) {
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				if (json == null || json.equals("")) {
					Tools.showToast(context, "网络连接有问题!");
					return;
				}
				JSONArray array = JSONArray.parseArray(json);
				JSONObject obj = array.getJSONObject(0);
				if (obj.getString("state").equals("true")) {
					for (Activity activity : MyApplication.application.list_activities) {
						if (activity instanceof User_SysSettingsActivity) {
							activity.finish();
							break;
						}
					}
					Tools.showToast(context, "提交成功!");
					finish();
				} else {
					Tools.showToast(context, "提示信息：" + obj.getString("result"));
				}
			}

			@Override
			public String getPageIndex() {
				return null;
			}
		};
	}

}
