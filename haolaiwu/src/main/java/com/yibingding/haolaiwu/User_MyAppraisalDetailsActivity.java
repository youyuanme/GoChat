package com.yibingding.haolaiwu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ybd.app.BaseActivity;
import com.ybd.app.tools.Tools;
import com.ybd.app.volley.VolleyPost;
import com.yibingding.haolaiwu.adapter.Comment_Adapter;
import com.yibingding.haolaiwu.domian.CommentBean;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.Constants;
import com.yibingding.haolaiwu.tools.MyApplication;

public class User_MyAppraisalDetailsActivity extends BaseActivity {
	PullToRefreshListView listView;
	private int pageIndex = 1;
	private List<CommentBean> list_newsCommentBeans;
	Comment_Adapter adapter;
	String associateGuid;
	TextView v;
	ProgressDialog dialog;

	@Override
	public void onCreateThisActivity() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initViews() {
		setContentView(R.layout.user_myappraisaldetails);
		listView = (PullToRefreshListView) findViewById(R.id.listview);
		v = (TextView) findViewById(R.id.value);
	}

	@Override
	public void initData() {
		String s = getIntent().getStringExtra("data");
		associateGuid = getIntent().getStringExtra("guid");
		list_newsCommentBeans = JSON.parseArray(s, CommentBean.class);

		adapter = new Comment_Adapter<CommentBean>(this, list_newsCommentBeans,
				null);
		listView.setAdapter(adapter);

	}

	public void back(View v) {
		finish();
	}

	public void submit(View v) {
		CharSequence vstr = this.v.getText();
		if (vstr == null || vstr.toString().equals("")) {
			Toast.makeText(this, "请输入评论内容！", Toast.LENGTH_SHORT).show();
			return;
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("fromUserGuid",
				getSharedPreferences("userinfo", Context.MODE_PRIVATE)
						.getString("guid", ""));
		map.put("fromContent", vstr.toString());
		map.put("associateGuid", associateGuid);
		map.put("Token", AESUtils.encode("fromUserGuid"));
		if (dialog == null) {
			dialog = new ProgressDialog(this);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setMessage("正在努力加载中...");
		}
		dialog.show();
		VolleyPost request = new VolleyPost(this, Constants.NEW_COMMENTADD_URL,
				map) {

			@Override
			public void pullJson(String json) {
				if (json == null || json.equals("")) {
					Tools.showToast(context, "网络连接有问题!");
					if (dialog != null && dialog.isShowing()) {
						dialog.dismiss();
					}
					return;
				}
				JSONArray array = JSONArray.parseArray(json);
				JSONObject obj = array.getJSONObject(0);
				if (obj.getString("state").equals("true")) {
					if (dialog.isShowing()) {
						dialog.setMessage("评论成功，正在刷新中。。。");
					}
				} else if (obj.getString("state").equals("false")) {
					Tools.showToast(context, "提示信息：" + obj.getString("result"));

					if (dialog != null && dialog.isShowing()) {
						dialog.dismiss();
					}
				}
			}

			@Override
			public String getPageIndex() {
				return null;
			}
		};
	}

	public void getData() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("page", pageIndex + "");
		map.put("pageSize", MyApplication.pageSize);
		map.put("associateGuid", associateGuid);
		map.put("Token", AESUtils.encode("associateGuid"));
		VolleyPost request = new VolleyPost(this,
				Constants.NEW_ABOUTCOMMENT_URL, map) {

			@Override
			public void pullJson(String json) {
				if (json == null || json.equals("")) {
					Tools.showToast(context, "网络连接有问题!");
					return;
				}
				JSONArray array = JSONArray.parseArray(json);
				JSONObject obj = array.getJSONObject(0);
				if (obj.getString("state").equals("true")) {
					String v = obj.getString("result");
					List<CommentBean> tempList = JSON.parseArray(v,
							CommentBean.class);
					list_newsCommentBeans.addAll(tempList);

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
