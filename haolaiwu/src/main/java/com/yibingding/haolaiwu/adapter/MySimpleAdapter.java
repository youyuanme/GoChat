package com.yibingding.haolaiwu.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ybd.app.MyBaseAdapter;
import com.yibingding.haolaiwu.R;

public class MySimpleAdapter extends MyBaseAdapter {

	public MySimpleAdapter(Context context, List<Map<String, String>> data,
			Object object) {
		super(context, data, object);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.popu_item, null);
			holder.tv_popu_item = (TextView) convertView
					.findViewById(R.id.tv_popu_item);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final List<Map<String, String>> datas = data;
		holder.tv_popu_item.setText(datas.get(position).get("name"));
		String isTextRed = datas.get(position).get("isTextRed");
		if (TextUtils.equals("true", isTextRed)) {
			holder.tv_popu_item.setTextColor(context.getResources().getColor(
					R.color.new_red));
		}
		// convertView.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// holder.tv_popu_item.setTextColor(context.getResources().getColor(
		// R.color.new_red));
		// }
		// });
		// holder.tv_popu_item.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// // Map<String, String> map = datas.get(position);
		// // for (String str : map.keySet()) {
		// // if (str.equals("isTextRed")) {
		// // map.put(str, "true");
		// // }
		// // }
		// }
		// });
		return convertView;
	}

	final class ViewHolder {
		private TextView tv_popu_item;
	}

}
