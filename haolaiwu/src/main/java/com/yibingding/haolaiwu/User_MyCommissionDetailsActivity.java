package com.yibingding.haolaiwu;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.TextView;
import com.alibaba.fastjson.JSONArray;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.ybd.app.BaseActivity;
import com.ybd.app.tools.Tools;
import com.yibingding.haolaiwu.adapter.User_CommissionAdapter;
import com.yibingding.haolaiwu.domian.Commission;

public class User_MyCommissionDetailsActivity extends BaseActivity {
	PullToRefreshListView listView;
	List<Commission> list = new ArrayList<Commission>();

	@Override
	public void onCreateThisActivity() {
		setContentView(R.layout.user_myscoredetails);

	}

	@Override
	public void initViews() {
		listView = (PullToRefreshListView) findViewById(R.id.listview);
		listView.setMode(Mode.DISABLED);

		TextView tv_tital = (TextView) findViewById(R.id.tv_tital);
		TextView tv_jifen = (TextView) findViewById(R.id.tv_jifen);
		if (getIntent().getFlags() == 1) {// 1是佣金2是积分
			tv_tital.setText("佣金详情");
			tv_jifen.setText("佣金");
		} else {
			tv_tital.setText("积分详情");
			tv_jifen.setText("积分");
		}
		// listView.setOnRefreshListener(new
		// PullToRefreshBase.OnRefreshListener2() {
		//
		// @Override
		// public void onPullDownToRefresh(PullToRefreshBase refreshView) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void onPullUpToRefresh(PullToRefreshBase refreshView) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
	}

	@Override
	public void initData() {
		String data = getIntent().getStringExtra("data");
		if (data != null && (!data.equals(""))) {
			list = JSONArray.parseArray(data, Commission.class);
			User_CommissionAdapter adapter = new User_CommissionAdapter(list,
					this);
			listView.setAdapter(adapter);
		} else {
			Tools.showToast(this, "提示信息：暂无数据!");
		}
	}

	public void back(View v) {
		finish();
	}
}
