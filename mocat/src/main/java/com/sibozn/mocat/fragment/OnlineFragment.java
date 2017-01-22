package com.sibozn.mocat.fragment;

import java.util.List;

import com.sibozn.mochat.R;
import com.sibozn.mochat.adapter.OnlineAdapter;
import com.sibozn.mochat.domain.People;
import com.sibozn.mochat.interf.GetDataSuccessListener;
import com.sibozn.mochat.interf.SuccessListener;
import com.sibozn.mochat.internet.OnlineDate_Volley;
import com.sibozn.mochat.tools.SystemTool;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class OnlineFragment extends BaseFragment implements OnRefreshListener,
		GetDataSuccessListener {

	private final String GET_ONLIN_URL = "http://moliao.sibozn.com/api/new.php?device_id=";

	private Context ctx;
	private SwipeRefreshLayout srl_refresh;
	private ListView list_view;
	private OnlineAdapter<People> onlineAdapte;

	@Override
	public View onCreateThisFragment(LayoutInflater inflater,
			ViewGroup container) {
		ctx = getActivity();
		return View.inflate(ctx, R.layout.fragment_online, null);
	}

	@Override
	public void initViews(View view) {
		srl_refresh = (SwipeRefreshLayout) view.findViewById(R.id.srl_refresh);
		list_view = (ListView) view.findViewById(R.id.list_view);
		srl_refresh.setOnRefreshListener(this);
		srl_refresh.setColorSchemeResources(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		list_view.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				System.out.println("=======position========"+position);
			}
		});
	}

	@Override
	public void initData() {
		new OnlineDate_Volley(ctx, GET_ONLIN_URL + SystemTool.getPhoneIMEI(ctx)
				+ "&sex=", "1").setOnGetDataSuccessListener(this);
	}

	@Override
	public void onRefresh() {
		initData();
	}

	@Override
	public void onGetDataSuccess(String tag, Object obj) {
		// TODO Auto-generated method stub
		if (obj != null) {
			if (TextUtils.equals("1", tag)) {
				List<People> peoples = (List<People>) obj;
				System.out.println("=========peoples.size()======"
						+ peoples.size());
				onlineAdapte = new OnlineAdapter<People>(ctx, peoples);
				list_view.setAdapter(onlineAdapte);
				if (srl_refresh.isRefreshing()) {
					srl_refresh.setRefreshing(false);
				}
			}
		}
	}

}
