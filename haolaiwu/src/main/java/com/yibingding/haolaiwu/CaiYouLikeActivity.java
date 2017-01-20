package com.yibingding.haolaiwu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.ybd.app.BaseActivity;
import com.ybd.app.interf.GetDataSuccessListener;
import com.ybd.app.views.MyListView;
import com.yibingding.haolaiwu.adapter.My_NEW_Adapter;
import com.yibingding.haolaiwu.domian.NewBean;
import com.yibingding.haolaiwu.internet.GetNews_Volley;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.Constants;
import com.yibingding.haolaiwu.tools.MyApplication;

public class CaiYouLikeActivity extends BaseActivity implements
		GetDataSuccessListener {

	private PullToRefreshScrollView pullToRefreshScrollView;
	private MyListView myListView;
	private int pageIndex = 1;
	private List<NewBean> list_new;
	private My_NEW_Adapter<NewBean> mAdapter;
	private boolean isInitData = true;
	private ProgressDialog dialog;

	@Override
	public void onCreateThisActivity() {
		setContentView(R.layout.activity_caiyoulike);
	}

	@Override
	public void initViews() {
		pullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.scrollView);
		myListView = (MyListView) findViewById(R.id.myListView);

		pullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.scrollView);
		myListView = (MyListView) findViewById(R.id.myListView);
		pullToRefreshScrollView.setMode(Mode.PULL_FROM_START);
		// int contentHeight = DensityUtils.getScreenH(this)
		// - DensityUtils.dip2px(this, 95);
		// LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
		// LinearLayout.LayoutParams.MATCH_PARENT, contentHeight);
		// pullToRefreshScrollView.setLayoutParams(lp);
		pullToRefreshScrollView
				.setOnRefreshListener(new OnRefreshListener2<ScrollView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						pageIndex = 1;
						isInitData = false;
						initData();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						// pageIndex++;
						pageIndex = 1;
						isInitData = false;
						initData();
					}
				});
	}

	@Override
	public void initData() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("top", MyApplication.pageSize);
		map.put("wordCount", MyApplication.wordCount);
		map.put("token", AESUtils.encode("top").replaceAll("\n", ""));
		if (isInitData) {
			if (dialog == null) {
				dialog = new ProgressDialog(this);
				dialog.setCanceledOnTouchOutside(false);
				dialog.setMessage("正在努力加载中...");
			}
			dialog.show();
		}
		System.out.println("========map.toString()======" + map.toString());
		GetNews_Volley list_Volley = new GetNews_Volley(this,
				Constants.GET_RECOMMAND_NEW, map);
		list_Volley.setOnGetDataSuccessListener(this);
	}

	@Override
	protected void onDestroy() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
		super.onDestroy();
	}

	public void onBack(View v) {
		finish();
	}

	@Override
	public void onGetDataSuccess(String tag, Object obj) {
		pullToRefreshScrollView.onRefreshComplete();
		if (obj != null) {
			if (isInitData && dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
			if (pageIndex == 1) {
				list_new = (List<NewBean>) obj;
				mAdapter = new My_NEW_Adapter<NewBean>(this, list_new, null);
				myListView.setAdapter(mAdapter);
			} else {
				List<NewBean> arrayList = (List<NewBean>) obj;
				list_new.addAll(arrayList);
			}
			mAdapter.notifyDataSetChanged();
		}
	}
}
