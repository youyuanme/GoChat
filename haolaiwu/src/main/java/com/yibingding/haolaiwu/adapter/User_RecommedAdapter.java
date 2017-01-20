package com.yibingding.haolaiwu.adapter;

import java.util.List;

import com.yibingding.haolaiwu.R;
import com.yibingding.haolaiwu.domian.Customer;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class User_RecommedAdapter extends BaseAdapter {
	private List<Customer> list;
	private Context cxt;

	public User_RecommedAdapter(Context cxt, List<Customer> list) {
		this.cxt = cxt;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Customer getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder holder = null;
		if (arg1 == null) {
			arg1 = LayoutInflater.from(cxt).inflate(
					R.layout.user_myrecommed_listitem, null);
			holder = new ViewHolder();
			holder.name = (TextView) arg1.findViewById(R.id.name);
			holder.state = (TextView) arg1.findViewById(R.id.state);
			arg1.setTag(holder);
		}
		holder = (ViewHolder) arg1.getTag();
		Customer c = getItem(arg0);
		holder.name.setText(c.getT_Client_Name());
		if (TextUtils.equals(c.getT_Client_dState(), "2")) {
			holder.state.setText("此客户非首访");
		} else if (TextUtils.equals(c.getT_Client_eState(), "1")) {
			holder.state.setText("签订协议");
		} else if (TextUtils.equals(c.getT_Client_mState(), "1")) {
			holder.state.setText("大定");
		} else if (TextUtils.equals(c.getT_Client_sState(), "1")) {
			holder.state.setText("到访");
		} else if (TextUtils.equals(c.getT_Client_dState(), "1")) {
			holder.state.setText("首访");
		} else {
			holder.state.setText("正在审核");
		}
		return arg1;
	}

	private final class ViewHolder {
		private TextView name, state;
	}
}
