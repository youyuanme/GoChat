package com.sibozn.mocat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public abstract class BaseFragment extends Fragment {

	public abstract View onCreateThisFragment(LayoutInflater inflater,
			ViewGroup container);

	protected View viewFragment;

	public abstract void initViews(View view);

	public abstract void initData();

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		viewFragment = onCreateThisFragment(inflater, container);
		return viewFragment;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViews(viewFragment);
		initData();
	}

	/**
	 * 是否联网
	 * 
	 * @param isNetConnectedListener
	 */
//	protected void isNetUseful(IsNetConnectedListener isNetConnectedListener) {
//		boolean connected = Tools.isConnected(getActivity());
//		if (connected) {
//			isNetConnectedListener.isConnected("yes");
//		} else {
//			isNetConnectedListener.isConnected("no");
//		}
//	}

	/**
	 * 用户是否登录
	 * 
	 * @param isUserLoginListener
	 */
//	protected void isUserLogin(IsUserLoginListener isUserLoginListener) {
//		String userid = PreferenceHelper.readString(getActivity(), "userinfo",
//				"userid", "");
//		isUserLoginListener.userLogin(userid);
//	}

}
