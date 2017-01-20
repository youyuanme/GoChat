package com.yibingding.haolaiwu.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.ybd.app.BaseFragment;
import com.ybd.app.interf.GetDataSuccessListener;
import com.ybd.app.tools.DensityUtils;
import com.ybd.app.tools.Tools;
import com.ybd.app.views.MyListView;
import com.yibingding.haolaiwu.R;
import com.yibingding.haolaiwu.SearchActivity;
import com.yibingding.haolaiwu.adapter.My_NEW_Adapter;
import com.yibingding.haolaiwu.dialog.Dialognetwork;
import com.yibingding.haolaiwu.domian.NewBean;
import com.yibingding.haolaiwu.houseparopertynew.HousePropertyNewDetailsActivity;
import com.yibingding.haolaiwu.internet.GetNews_Volley;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.Constants;
import com.yibingding.haolaiwu.tools.MyApplication;

/*新闻/发现*/
public class FragmentThird extends BaseFragment implements OnClickListener,
		GetDataSuccessListener {

	private ImageView iv_new_back;
	private EditText editTextSearch;
	private PullToRefreshScrollView pullToRefreshScrollView;
	private MyListView myListView;
	private int pageIndex = 1;
	private List<NewBean> list_new;
	private My_NEW_Adapter<NewBean> mAdapter;
	private Dialognetwork dialog;
	private boolean isInitData = true;

	@Override
	public View onCreateThisFragment(LayoutInflater inflater,
			ViewGroup container) {
		list_new = new ArrayList<NewBean>();
		return inflater.inflate(R.layout.fragment_third, null);
	}

	@Override
	public void initViews(View view) {
		iv_new_back = (ImageView) view.findViewById(R.id.iv_new_back);
		editTextSearch = (EditText) view.findViewById(R.id.editTextSearch);
		editTextSearch.setOnClickListener(this);
		pullToRefreshScrollView = (PullToRefreshScrollView) view
				.findViewById(R.id.scrollView);
		myListView = (MyListView) view.findViewById(R.id.myListView);
		pullToRefreshScrollView.setMode(Mode.BOTH);
		int contentHeight = DensityUtils.getScreenH(getActivity())
				- DensityUtils.dip2px(getActivity(), 118);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, contentHeight);
		pullToRefreshScrollView.setLayoutParams(lp);
		pullToRefreshScrollView
				.setOnRefreshListener(new OnRefreshListener2<ScrollView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						pageIndex = 1;
						if (!list_new.isEmpty()) {
							list_new.clear();
						}
						isInitData = false;
						initData();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						pageIndex++;
						isInitData = false;
						initData();
					}
				});
	}

	@Override
	public void initData() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("page", pageIndex + "");
		map.put("pageSize", MyApplication.pageSize);
		map.put("wordCount", MyApplication.wordCount);
		map.put("token", AESUtils.encode("page").replaceAll("\n", ""));
		if (isInitData) {
			if (dialog == null) {
				dialog = new Dialognetwork(getActivity());
				// dialog.setCanceledOnTouchOutside(false);
				// dialog.setMessage("正在努力加载中...");
			}
			dialog.show();
		}
		System.out.println("============map.toString()===" + map.toString());
		GetNews_Volley list_Volley = new GetNews_Volley(getActivity(),
				Constants.GET_NEW, map);
		list_Volley.setOnGetDataSuccessListener(this);
	}

	@Override
	public void onDestroy() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.editTextSearch:
			Tools.startActivity(getActivity(), SearchActivity.class);
			break;
		}
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
				mAdapter = new My_NEW_Adapter<NewBean>(getActivity(), list_new,
						null);
				myListView.setAdapter(mAdapter);
			} else {
				List<NewBean> arrayList = (List<NewBean>) obj;
				list_new.addAll(arrayList);
			}
			mAdapter.notifyDataSetChanged();
			System.out.println("===========list_new.size()==="
					+ list_new.size());
		}
	}

}
