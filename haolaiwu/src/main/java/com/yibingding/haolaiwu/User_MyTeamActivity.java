package com.yibingding.haolaiwu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.ybd.app.BaseActivity;
import com.ybd.app.tools.PreferenceHelper;
import com.ybd.app.tools.Tools;
import com.ybd.app.views.MyListView;
import com.ybd.app.volley.VolleyPost;
import com.yibingding.haolaiwu.adapter.UserAdapter;
import com.yibingding.haolaiwu.domian.User;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.Constants;

public class User_MyTeamActivity extends BaseActivity {
	private PullToRefreshScrollView pulltorefreshscrollview;
	private MyListView myListView;
	private PullToRefreshListView listview;
	private List<User> list;
	private ProgressDialog dialog;

	@Override
	public void onCreateThisActivity() {
		setContentView(R.layout.user_myteam);
	}

	@Override
	public void initViews() {
		listview = (PullToRefreshListView) findViewById(R.id.listview);
		listview.setMode(Mode.DISABLED);
		// pulltorefreshscrollview = (PullToRefreshScrollView)
		// findViewById(R.id.pulltorefreshscrollview);
		// myListView = (MyListView) findViewById(R.id.myListView);
		// pulltorefreshscrollview.setMode(Mode.DISABLED);
	}

	@Override
	public void initData() {
		if (dialog == null) {
			dialog = new ProgressDialog(this);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setMessage("正在努力加载中...");
		}
		dialog.show();
		getData();
	}

	public void back(View v) {
		finish();
	}

	private void getData() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userGuid",
				PreferenceHelper.readString(this, "userinfo", "guid"));
		params.put("Token", AESUtils.encode("userGuid"));
		VolleyPost request = new VolleyPost(this, Constants.SERVER_IP
				+ Constants.User_GetTeamUser_URL, params) {

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
					String v = obj.getString("result");
					list = JSON.parseArray(v, User.class);
					UserAdapter adapter = new UserAdapter(list,
							User_MyTeamActivity.this);
					listview.setAdapter(adapter);
					// addBankView();
				}
			}

			@Override
			public String getPageIndex() {
				return null;
			}
		};

	}
}
