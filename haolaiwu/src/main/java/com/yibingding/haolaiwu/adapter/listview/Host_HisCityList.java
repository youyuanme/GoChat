package com.yibingding.haolaiwu.adapter.listview;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yibingding.haolaiwu.R;
import com.yibingding.haolaiwu.domian.CityInfo;
import com.yibingding.haolaiwu.tools.MyUtils;

public class Host_HisCityList extends BaseAdapter {

	public Host_HisCityList(Context ctx, List<CityInfo> hotCityList) {
		this.ctx = ctx;
		this.hotCityList = hotCityList;
	}

	Context ctx;
	List<CityInfo> hotCityList;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView tv = new TextView(ctx);
		tv.setPadding(MyUtils.dip2px(ctx, 16), MyUtils.dip2px(ctx, 8),
				MyUtils.dip2px(ctx, 16), MyUtils.dip2px(ctx, 8));
		tv.setBackgroundColor(Color.WHITE);
		tv.setText(hotCityList.get(position).name);
		tv.setGravity(Gravity.CENTER);
		tv.setSingleLine(true);
		tv.setEllipsize(TruncateAt.MIDDLE);
		return tv;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public int getCount() {
		if (hotCityList == null) {
			return 0;
		}
		return hotCityList.size();
	}
}
