package com.yibingding.haolaiwu.adapter;

import java.util.List;

import com.yibingding.haolaiwu.R;
import com.yibingding.haolaiwu.domian.Commission;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class User_CommissionAdapter extends BaseAdapter {
	private List<Commission> list;
	private Context cxt;

	public User_CommissionAdapter(List<Commission> list, Context cxt) {
		this.list = list;
		this.cxt = cxt;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Commission getItem(int arg0) {
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
					R.layout.user_myscoredetails_listitem, null);
			holder = new ViewHolder();
			holder.date = (TextView) arg1.findViewById(R.id.date);
			holder.type = (TextView) arg1.findViewById(R.id.type);
			holder.value = (TextView) arg1.findViewById(R.id.value);
			arg1.setTag(holder);
		}
		holder = (ViewHolder) arg1.getTag();
		Commission c = getItem(arg0);
		holder.date.setText(c.getAdddatestr());
		if (c.getType() == 0) {
			holder.type.setText("增加");
			holder.value.setText("+" + c.getT_UserAccount_AddReward());
		} else {
			holder.value.setText("+" + c.getT_UserAccount_ReduceReward());
			holder.type.setText("减少");
		}
		return arg1;
	}

	class ViewHolder {
		private TextView date, type, value;
	}
}
