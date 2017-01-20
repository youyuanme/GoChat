package com.yibingding.haolaiwu.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.ybd.app.BaseActivity;
import com.ybd.app.interf.GetDataSuccessListener;
import com.yibingding.haolaiwu.R;
import com.yibingding.haolaiwu.adapter.HouseRentAdapter;
import com.yibingding.haolaiwu.domian.HouseRentItem;
import com.yibingding.haolaiwu.internet.GetBuildingList;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.Constants;
import com.yibingding.haolaiwu.tools.MyApplication;
import com.yibingding.haolaiwu.tools.MyUtils;
import com.yibingding.haolaiwu.weight.WRefreshListView;

public class BuildingCommendActivity extends BaseActivity implements
		OnClickListener {

	private WRefreshListView refreshListView;
	private int top = 20;
	private int index = 1;
	private List<HouseRentItem> houseRentItems;
	private ProgressDialog dialog;
	private boolean isInitData = true;

	@Override
	public void onCreateThisActivity() {
		setContentView(R.layout.activity_commendbuilding);
	}

	@Override
	public void initViews() {
		ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		refreshListView = (WRefreshListView) findViewById(R.id.lv_commendbuilding);
		refreshListView.getLoadingLayoutProxy(false, true).setPullLabel(
				"上拉加载更多");
		refreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel(
				"正在加载中。。。");
		refreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel(
				"释放加载。");
		refreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {

				if (refreshListView.isHeaderShown()) {
					// 刷新
					index = 1;
					isInitData = false;
					getData();
				} else if (refreshListView.isFooterShown()) {
					// 加载更多
					index++;
					getData();
				}
			}
		});
		refreshListView.setMode(Mode.PULL_FROM_START);
	}

	@Override
	public void initData() {
		getData();
	}

	@Override
	protected void onDestroy() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
		super.onDestroy();
	}

	private void getData() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("top", "0");
		map.put("cityName", MyApplication.city);
		map.put("Token", AESUtils.encode("top").replaceAll("\n", ""));
		if (isInitData) {
			if (dialog == null) {
				dialog = new ProgressDialog(this);
				dialog.setCanceledOnTouchOutside(false);
				dialog.setMessage("正在努力加载中...");
			}
			dialog.show();
		}
		System.out.println("============map.toString()====" + map.toString());
		GetBuildingList getBuildingList = new GetBuildingList(this,
				Constants.COMMANDBUILDING_URL, map);
		getBuildingList
				.setOnGetDataSuccessListener(new GetDataSuccessListener() {
					private HouseRentAdapter houseRentAdapter;

					@Override
					public void onGetDataSuccess(String tag, Object obj) {
						if (isInitData && dialog != null && dialog.isShowing()) {
							dialog.dismiss();
						}
						houseRentItems = (List<HouseRentItem>) obj;
						if (houseRentItems.size() == 0) {
							MyUtils.showToast(BuildingCommendActivity.this,
									"暂无推荐房产信息");
							refreshListView.onRefreshComplete();
							return;
						}
						houseRentAdapter = new HouseRentAdapter(
								BuildingCommendActivity.this, houseRentItems,
								Constants.BUILDING);

						refreshListView.setAdapter(houseRentAdapter);
						refreshListView.onRefreshComplete();
					}
				});
	}

	@Override
	public void onClick(View v) {
		finish();
	}

}
