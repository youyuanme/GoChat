package com.sibozn.mocat.fragment;

import com.sibozn.mochat.R;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MessageFragment extends BaseFragment {

	private Context ctx;

	@Override
	public View onCreateThisFragment(LayoutInflater inflater,
			ViewGroup container) {
		ctx = getActivity();
		return View.inflate(ctx, R.layout.fragment_message, null);
	}

	@Override
	public void initViews(View view) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

}
