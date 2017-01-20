package com.yibingding.haolaiwu;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ybd.app.BaseActivity;
import com.ybd.app.tools.Tools;
import com.yibingding.haolaiwu.adapter.User_ScoreAdapter;
import com.yibingding.haolaiwu.domian.Score;

public class User_MyScoreDetailsActivity extends BaseActivity {
	PullToRefreshListView listView;
	List<Score> list = new ArrayList<Score>();

	@Override
	public void onCreateThisActivity() {
		setContentView(R.layout.user_myscoredetails);

	}

	@Override
	public void initViews() {
		TextView tv_tital = (TextView) findViewById(R.id.tv_tital);
		if (getIntent().getFlags() == 1) {// 1是佣金2是积分
			tv_tital.setText("佣金详情");
		} else {
			tv_tital.setText("积分详情");
		}
		listView = (PullToRefreshListView) findViewById(R.id.listview);
		listView.setMode(Mode.DISABLED);
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
			list = JSONArray.parseArray(data, Score.class);
			User_ScoreAdapter adapter = new User_ScoreAdapter(list, this);
			listView.setAdapter(adapter);
		} else {
			Tools.showToast(this, "提示信息：暂无数据!");
		}
	}

	public void back(View v) {
		finish();
	}
}
