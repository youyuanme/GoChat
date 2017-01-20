package com.yibingding.haolaiwu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.ybd.app.BaseActivity;
import com.ybd.app.interf.GetDataSuccessListener;
import com.yibingding.haolaiwu.activity.BuildingInfoActivity;
import com.yibingding.haolaiwu.adapter.MyAttention_Adapter;
import com.yibingding.haolaiwu.domian.MyAttentionBean;
import com.yibingding.haolaiwu.houseparopertynew.HousePropertyNewDetailsActivity;
import com.yibingding.haolaiwu.internet.GetCollectionList_Volley;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.Constants;
import com.yibingding.haolaiwu.view.ScrollListviewDelete;
import com.yibingding.haolaiwu.view.ScrollListviewDelete.ItemClickListener;

public class User_MyFollowActivity extends BaseActivity implements
		GetDataSuccessListener {
	private ScrollListviewDelete my_attention_listView;
	private MyAttention_Adapter myAttention_Adapter;
	private List<MyAttentionBean> arrayList;

	@Override
	public void onCreateThisActivity() {
		setContentView(R.layout.user_myfollow);
	}

	@Override
	public void initViews() {
		my_attention_listView = (ScrollListviewDelete) findViewById(R.id.my_attention_listView);
		my_attention_listView.setOnItemClickListener(new ItemClickListener() {
			@Override
			public void onItemClick(int position) {
				String style = arrayList.get(position).getStyle();
				MyAttentionBean myAttentionBean = arrayList.get(position);
				Intent intent = new Intent();
				if (TextUtils.equals("租赁", style)) {
					intent.putExtra("id", myAttentionBean.getAssoctateGuid());
					intent.setClass(User_MyFollowActivity.this,
							HouseDetailsActivity.class);
					intent.putExtra("whereFrom", Constants.HOUSE_RENT);
				} else if (TextUtils.equals("二手房", style)) {
					intent.putExtra("id", myAttentionBean.getAssoctateGuid());
					intent.setClass(User_MyFollowActivity.this,
							HouseDetailsActivity.class);
					intent.putExtra("whereFrom", Constants.OLD_HOUSE);
				} else if (TextUtils.equals("新闻", style)) {
					intent.setClass(User_MyFollowActivity.this,
							HousePropertyNewDetailsActivity.class);
					intent.putExtra("Guid", myAttentionBean.getAssoctateGuid());
				} else if (TextUtils.equals("楼盘", style)) {
					intent.setClass(User_MyFollowActivity.this,
							BuildingInfoActivity.class);
					intent.putExtra("id", myAttentionBean.getAssoctateGuid());
				}
				User_MyFollowActivity.this.startActivity(intent);
			}
		});
	}

	@Override
	protected void onResume() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userGuid",
				getSharedPreferences("userinfo", Context.MODE_PRIVATE)
						.getString("guid", ""));
		map.put("Token", AESUtils.encode("userGuid").replaceAll("\n", ""));
		GetCollectionList_Volley getCollectionList_Volley = new GetCollectionList_Volley(
				this, Constants.MY_ATTENTION_GETCOLLECIONLIST_URL, map);
		getCollectionList_Volley.setOnGetDataSuccessListener(this);
		super.onResume();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
	}

	public void back(View v) {
		finish();
	}

	@Override
	public void onGetDataSuccess(String tag, Object obj) {
		if (obj != null) {
			arrayList = (List<MyAttentionBean>) obj;
			myAttention_Adapter = new MyAttention_Adapter(this, arrayList, null);
			my_attention_listView.setAdapter(myAttention_Adapter);
		}
	}
}
