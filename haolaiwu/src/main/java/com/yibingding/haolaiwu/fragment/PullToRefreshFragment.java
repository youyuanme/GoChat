package com.yibingding.haolaiwu.fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.ybd.app.tools.PreferenceHelper;
import com.ybd.app.tools.Tools;
import com.ybd.app.volley.VolleyPost;
import com.yibingding.haolaiwu.User_MyConsumerActivity;
import com.yibingding.haolaiwu.domian.Customer;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.Constants;
import com.yibingding.haolaiwu.tools.MyApplication;

import android.app.ProgressDialog;
import android.graphics.AvoidXfermode.Mode;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

public class PullToRefreshFragment extends Fragment {
	private PullToRefreshListView listview;
	private BaseAdapter adapter;
	private OnRefreshListener2<ListView> listener;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		listview = new PullToRefreshListView(container.getContext());
		listview.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.BOTH);
		if (adapter != null) {
			listview.setAdapter(adapter);
			listview.setOnRefreshListener(listener);
		}
		return listview;
		// return super.onCreateView(inflater, container, savedInstanceState);
	}

	public void setAdapter(BaseAdapter adapter) {
		this.adapter = adapter;
		if (listview != null) {
			listview.setAdapter(adapter);
		}
	}

	public void datachange() {
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
		if (listview != null) {
			listview.onRefreshComplete();
		}
	}

	public void setOnRefreshListener(OnRefreshListener2<ListView> listener) {
		this.listener = listener;
		if (listview != null) {
			listview.setOnRefreshListener(listener);
		}
	}

}
