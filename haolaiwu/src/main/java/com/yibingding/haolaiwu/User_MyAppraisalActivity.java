package com.yibingding.haolaiwu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ybd.app.BaseActivity;
import com.ybd.app.tools.PreferenceHelper;
import com.ybd.app.volley.VolleyPost;
import com.yibingding.haolaiwu.adapter.User_MyAppraisalAdapter;
import com.yibingding.haolaiwu.domian.Appraisal;
import com.yibingding.haolaiwu.houseparopertynew.HousePropertyNewDetailsActivity;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.Constants;

public class User_MyAppraisalActivity extends BaseActivity {

	private List<Appraisal> list = new ArrayList<Appraisal>();
	private PullToRefreshListView listView;
	private User_MyAppraisalAdapter adapter;
	private ProgressDialog dialog;

	@Override
	public void onCreateThisActivity() {
		setContentView(R.layout.user_myappraisal);
	}

	@Override
	public void initViews() {
		listView = (PullToRefreshListView) findViewById(R.id.listview);
		listView.setMode(Mode.PULL_FROM_START);
		listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				getData();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub

			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Appraisal appraisal = list.get((int) arg3);
				Intent intent = new Intent(User_MyAppraisalActivity.this,
						HousePropertyNewDetailsActivity.class);
				intent.putExtra("Guid", appraisal.getNewsGuid());
				startActivity(intent);
			}
		});
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
				+ Constants.User_GetAppraisal_URL, params) {
			@Override
			public void pullJson(String json) {
				// Log.v("this", "获取的银行列表"+json);
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				if (json == null || json.equals("")) {
					Toast.makeText(context, "网络连接有问题！", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (listView.isRefreshing()) {
					listView.onRefreshComplete();
				}
				JSONArray array = JSONArray.parseArray(json);
				JSONObject obj = array.getJSONObject(0);
				if (obj.getString("state").equals("true")) {
					String v = obj.getString("result");
					List<Appraisal> templist = JSON.parseArray(v,
							Appraisal.class);
					Iterator<Appraisal> iterator = templist.iterator();
					while (iterator.hasNext()) {
						Appraisal i = iterator.next();
						// if (i.getNewsTitle() == null
						// || i.getNewsTitle().equals("")) {
						// iterator.remove();
						// }
					}
					list.clear();
					list.addAll(templist);
					showdata();
					// addBankView();
				}
			}

			@Override
			public String getPageIndex() {
				return null;
			}
		};

	}

	private void showdata() {
		if (adapter == null) {
			adapter = new User_MyAppraisalAdapter(this, list);
			listView.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
	}
}
