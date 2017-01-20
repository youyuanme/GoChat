package com.yibingding.haolaiwu;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ybd.app.BaseActivity;
import com.ybd.app.tools.PreferenceHelper;
import com.ybd.app.tools.SystemTool;
import com.ybd.app.tools.Tools;
import com.ybd.app.volley.VolleyPost;
import com.yibingding.haolaiwu.adapter.User_RecommedAdapter;
import com.yibingding.haolaiwu.domian.BankCard;
import com.yibingding.haolaiwu.domian.Customer;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.Constants;
import com.yibingding.haolaiwu.tools.MyApplication;

public class User_MyRecommedActivity extends BaseActivity implements
		OnRefreshListener2 {

	private PullToRefreshListView listview;
	private List<Customer> list = new ArrayList<Customer>();
	private ProgressDialog dialog;
	private int pagePosition = 1;
	private boolean isInitData = true;
	private User_RecommedAdapter user_RecommedAdapter;

	@Override
	public void onCreateThisActivity() {
		setContentView(R.layout.user_myrecommed);
	}

	@Override
	public void initViews() {
		listview = (PullToRefreshListView) findViewById(R.id.listview);
		listview.setMode(Mode.BOTH);
		listview.setOnRefreshListener(this);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(User_MyRecommedActivity.this,
						User_MyRecommedDetailsActivity.class);
				Customer customer = list.get(arg2 - 1);
				intent.putExtra("customer", customer);
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
		getdata();
	}

	public void back(View v) {
		finish();
	}

	private void getdata() {
		if (pagePosition == 1 && !list.isEmpty()) {
			list.clear();
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("dstate", "");
		params.put("statetype", "");
		params.put("page", pagePosition + "");
		params.put("pagesize", "15");
		params.put("usertype",
				PreferenceHelper.readString(this, "userinfo", "tStyle"));
		params.put("userGuid",
				PreferenceHelper.readString(this, "userinfo", "guid"));
		params.put("Token", AESUtils.encode("userGuid"));
		VolleyPost request = new VolleyPost(this, Constants.SERVER_IP
				+ Constants.User_GetClientListByPage_URL, params) {
			@Override
			public void pullJson(String json) {
				if (dialog != null && dialog.isShowing() && isInitData) {
					dialog.dismiss();
					isInitData = false;
				}
				if (TextUtils.isEmpty(json)) {
					Tools.showToast(context, "网络连接有问题!");
					return;
				}
				try {
					JSONArray array = new JSONArray(json);
					JSONObject obj = array.getJSONObject(0);
					if (obj.getString("state").equals("true")) {
						JSONArray jsonArray = new JSONArray(
								obj.getString("result"));
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							Customer customer = new Customer();
							customer.setGuid(jsonObject.getString("Guid"));
							customer.setT_User_Guid(jsonObject
									.getString("t_User_Guid"));
							customer.setT_Assicoate_Guid(jsonObject
									.getString("t_Assicoate_Guid"));
							customer.setT_Client_Name(jsonObject
									.getString("t_Client_Name"));
							customer.setT_Client_CardID(jsonObject
									.getString("t_Client_CardID"));
							customer.setT_Client_Phone(jsonObject
									.getString("t_Client_Phone"));
							customer.setT_Client_QQ(jsonObject
									.getString("t_Client_QQ"));
							customer.setT_Client_Remark(jsonObject
									.getString("t_Client_Remark"));
							customer.setT_Client_dState(jsonObject
									.getString("t_Client_dState"));
							customer.setT_Client_dDate(jsonObject
									.getString("t_Client_dDate"));
							customer.setT_Client_sState(jsonObject
									.getString("t_Client_sState"));
							customer.setT_Client_sDate(jsonObject
									.getString("t_Client_sDate"));
							customer.setT_Client_mState(jsonObject
									.getString("t_Client_mState"));
							customer.setT_Client_mDate(jsonObject
									.getString("t_Client_mDate"));
							customer.setT_Client_eState(jsonObject
									.getString("t_Client_eState"));
							customer.setT_Client_eDate(jsonObject
									.getString("t_Client_eDate"));
							customer.setNames(jsonObject.getString("names"));
							customer.setStyle(jsonObject.getString("style"));
							customer.setPic(jsonObject.getString("pic"));
							customer.setProVinceName(jsonObject
									.getString("ProvinceName"));
							customer.setCityName(jsonObject
									.getString("CityName"));
							customer.setDistrictName(jsonObject
									.getString("DistrictName"));
							customer.setStreet(jsonObject.getString("Street"));
							customer.setAveragePrice(jsonObject
									.getString("AveragePrice"));
							list.add(customer);
						}
						listview.onRefreshComplete();
						if (pagePosition == 1) {
							user_RecommedAdapter = new User_RecommedAdapter(
									User_MyRecommedActivity.this, list);
							listview.setAdapter(user_RecommedAdapter);
						} else {
							user_RecommedAdapter.notifyDataSetChanged();
						}
					} else {
						Tools.showToast(context, obj.getString("result"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public String getPageIndex() {
				return null;
			}
		};
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase refreshView) {
		pagePosition = 1;
		getdata();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase refreshView) {
		// TODO Auto-generated method stub
		pagePosition++;
		getdata();
	}

}
