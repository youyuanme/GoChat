package com.yibingding.haolaiwu;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ybd.app.BaseActivity;
import com.ybd.app.tools.PreferenceHelper;
import com.ybd.app.tools.Tools;
import com.ybd.app.volley.VolleyPost;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.Constants;

public class User_MyCommissionActivity extends BaseActivity {
	TextView value;
	String detals, total;
	ProgressDialog dialog;

	@Override
	public void onCreateThisActivity() {
		setContentView(R.layout.user_mycommission);
	}

	@Override
	public void initViews() {
		value = (TextView) findViewById(R.id.value);
	}

	@Override
	public void initData() {
		// getData();
	}

	@Override
	protected void onResume() {
		getData();
		super.onResume();
	}

	public void back(View v) {
		finish();
	}

	public void tixian(View v) {
		String complete = PreferenceHelper.readString(this, "userinfo",
				"accountmsgcomplete");
		if (complete != null && complete.equals("1")) {
			if (total != null && (!total.equals(""))) {
				float reward = Float.parseFloat(total);
				if (reward < 1) {
					Tools.showToast(this, "不好意思，您的当前佣金不足提现!");
					return;
				}
				Intent intent = new Intent(this, User_TakeMoneyActivity.class);
				// startActivityForResult(intent, 1);
				intent.putExtra("zhanghuyue", reward);
				startActivity(intent);
			} else {
				Tools.showToast(this, "金额不足!");
			}
		} else {
			Tools.showToast(this, "请完成个人账户信息!");
		}
	}

	public void details(View v) {
		Intent intent = new Intent(this, User_MyCommissionDetailsActivity.class);
		intent.putExtra("data", detals);
		intent.addFlags(1);// 1是佣金2是积分
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
			dialog.setMessage(getString(R.string.dialog_loading));
		}
		dialog.show();
		VolleyPost request = new VolleyPost(this, Constants.SERVER_IP
				+ Constants.User_Commission_URL, params) {

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
				JSONObject jsonObject = array.getJSONObject(0);
				if (jsonObject.getString("state").equals("true")) {
					array = jsonObject.getJSONArray("result");
					total = array.getJSONObject(0).getString("Reward");
					detals = array.toJSONString();
					value.setText(total);
				} else if (jsonObject.getString("state").equals("false")) {
					// Toast.makeText(User_MyCommissionActivity.this,
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

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// if(arg0==1&&arg1==1){
		// getData();
		// }
		super.onActivityResult(arg0, arg1, arg2);
	}
}
