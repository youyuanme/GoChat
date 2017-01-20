package com.yibingding.haolaiwu;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ybd.app.BaseActivity;
import com.ybd.app.tools.Tools;
import com.yibingding.haolaiwu.factory.FragmentFactory;
import com.yibingding.haolaiwu.fragment.FragmentFirst;
import com.yibingding.haolaiwu.fragment.FragmentFouth;
import com.yibingding.haolaiwu.fragment.FragmentSecond;
import com.yibingding.haolaiwu.fragment.FragmentThird;

public class MainActivity extends BaseActivity implements OnClickListener {

	private long waitTime = 2000;
	private long touchTime = 0;

	private Fragment contentView;
	private FrameLayout pager_content;
	private int which = -1;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// 替换视图
			getSupportFragmentManager().beginTransaction()
					// .setCustomAnimations(R.anim.fragment_right_in,
					// R.anim.fragment_left_out)
					.replace(R.id.pager_content, contentView)
					.commitAllowingStateLoss();
		};
	};

	private LinearLayout ll_tab1, ll_tab2, ll_tab3, ll_tab4;
	private TextView tv1, tv2, tv3, tv4;
	private ImageView iv_tab1, iv_tab2, iv_tab3, iv_tab4;

	@Override
	public void onCreateThisActivity() {
		setContentView(R.layout.activity_main);
	}

	@Override
	public void initViews() {
		ll_tab1 = (LinearLayout) this.findViewById(R.id.ll_tab1);
		ll_tab2 = (LinearLayout) this.findViewById(R.id.ll_tab2);
		ll_tab3 = (LinearLayout) this.findViewById(R.id.ll_tab3);
		ll_tab4 = (LinearLayout) this.findViewById(R.id.ll_tab4);

		iv_tab1 = (ImageView) this.findViewById(R.id.iv_tab1);
		iv_tab2 = (ImageView) this.findViewById(R.id.iv_tab2);
		iv_tab3 = (ImageView) this.findViewById(R.id.iv_tab3);
		iv_tab4 = (ImageView) this.findViewById(R.id.iv_tab4);

		tv1 = (TextView) this.findViewById(R.id.tv_tab1);
		tv2 = (TextView) this.findViewById(R.id.tv_tab2);
		tv3 = (TextView) this.findViewById(R.id.tv_tab3);
		tv4 = (TextView) this.findViewById(R.id.tv_tab4);

		ll_tab1.setOnClickListener(this);
		ll_tab2.setOnClickListener(this);
		ll_tab3.setOnClickListener(this);
		ll_tab4.setOnClickListener(this);

		pager_content = (FrameLayout) findViewById(R.id.pager_content);

		setColors(tv1, tv2, tv3, tv4);
		unselect_img();
		iv_tab1.setBackgroundResource(R.drawable.shouye0);
		setTabSelection(0);

	}

	private void setColors(TextView tv1, TextView tv2, TextView tv3,
			TextView tv4) {
		tv1.setTextColor(getResources().getColor(R.color.tabSelect));
		tv2.setTextColor(getResources().getColor(R.color.tabunSelect));
		tv3.setTextColor(getResources().getColor(R.color.tabunSelect));
		tv4.setTextColor(getResources().getColor(R.color.tabunSelect));
	}

	private void unselect_img() {
		iv_tab1.setBackgroundResource(R.drawable.shouye1);
		iv_tab2.setBackgroundResource(R.drawable.loupan1);
		iv_tab3.setBackgroundResource(R.drawable.news1);
		iv_tab4.setBackgroundResource(R.drawable.my1);
	}

	@Override
	public void initData() {
	}

	@Override
	public void onClick(View v) {
		unselect_img();
		switch (v.getId()) {
		case R.id.ll_tab1:
			setColors(tv1, tv2, tv3, tv4);
			iv_tab1.setBackgroundResource(R.drawable.shouye0);
			setTabSelection(0);
			break;

		case R.id.ll_tab2:
			setColors(tv2, tv1, tv3, tv4);
			iv_tab2.setBackgroundResource(R.drawable.loupan0);
			setTabSelection(1);
			break;

		case R.id.ll_tab3:
			setColors(tv3, tv1, tv2, tv4);
			iv_tab3.setBackgroundResource(R.drawable.news0);
			setTabSelection(2);
			break;

		case R.id.ll_tab4:
			setColors(tv4, tv1, tv2, tv3);
			iv_tab4.setBackgroundResource(R.drawable.my0);
			setTabSelection(3);
			break;
		}

	}

	private void setTabSelection(int index) {
		switch (index) {
		case 0:
			if (which != 0)
				which = 0;
			else
				return;
			break;
		case 1:
			if (which != 1)
				which = 1;
			else
				return;
			break;
		case 2:
			if (which != 2)
				which = 2;
			else
				return;
			break;
		case 3:
			if (which != 3)
				which = 3;
			else
				return;
			break;
		}
		selectView();
	}

	private void selectView() {
		contentView = FragmentFactory.createFragment(which);
		getSupportFragmentManager().beginTransaction()
		// .setCustomAnimations(R.anim.fragment_right_in,
		// R.anim.fragment_left_out)
		.replace(R.id.pager_content, contentView)
		.commitAllowingStateLoss();
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				handler.sendEmptyMessage(1212);
//			}
//		}).start();
	}

	public void toNewsFragment() {
		setColors(tv3, tv1, tv2, tv4);
		iv_tab3.setBackgroundResource(R.drawable.news0);
		setTabSelection(2);
	}

	/**
	 * 监听[返回]键事件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 返回键
		if (KeyEvent.KEYCODE_BACK == keyCode) {

			long currentTime = System.currentTimeMillis();
			if ((currentTime - touchTime) >= waitTime) {
				Tools.showToast(getApplicationContext(), "再按一次，退出程序");
				touchTime = currentTime;
			} else {
				finish();
			}
			return true;
		}
		return false;
	}

}
