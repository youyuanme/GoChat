package com.yibingding.haolaiwu;

import android.os.Bundle;
import com.ybd.app.BaseActivity;
import com.yibingding.haolaiwu.fragment.FragmentSecond;

public class BuildingActivity extends BaseActivity {

	@Override
	public void onCreateThisActivity() {
		setContentView(R.layout.activity_building);
	}

	@Override
	public void initViews() {
		FragmentSecond fragmentSecond = new FragmentSecond();
		Bundle bundle = new Bundle();
		bundle.putString("activityOrFragmet", "activity");
		fragmentSecond.setArguments(bundle);
		getSupportFragmentManager().beginTransaction()
				.add(R.id.ll, fragmentSecond).commit();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

}
