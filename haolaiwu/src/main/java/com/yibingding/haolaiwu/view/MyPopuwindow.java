package com.yibingding.haolaiwu.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.yibingding.haolaiwu.R;
import com.yibingding.haolaiwu.adapter.MySimpleAdapter;

public class MyPopuwindow extends PopupWindow {

	private View rootView; // 总的布局
	private ListView lv_popu_one_listview;
	private PopuwindowOnSeleckedListener setOnPopuwindowListener;

	public MyPopuwindow(final Context context,
			final List<Map<String, String>> datas, final String type) {
		super(context);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// ColorDrawable dw = new ColorDrawable(0xb0000000);
		// this.setBackgroundDrawable(dw);
		this.setBackgroundDrawable(new BitmapDrawable());
		this.setTouchable(true);
		this.setFocusable(true);
		this.setAnimationStyle(R.style.PopupWindowAnimation);
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		rootView = layoutInflater.inflate(R.layout.popu_one_listview, null);

		lv_popu_one_listview = (ListView) rootView
				.findViewById(R.id.lv_popu_one_listview);

		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("id", "");
		map1.put("name", "不限");
		map1.put("isTextRed", "flase");
		datas.add(0, map1);
		SimpleAdapter simpleAdapter = new SimpleAdapter(context, datas,
				R.layout.popu_item, new String[] { "name" },
				new int[] { R.id.tv_popu_item });
		// final MySimpleAdapter simpleAdapter = new MySimpleAdapter(context,
		// datas,
		// null);
		lv_popu_one_listview.setAdapter(simpleAdapter);
		// for (int i = 0; i < datas.size(); i++) {
		// System.out.println("------=====----"+datas.get(i).get("isTextRed"));;
		//
		// }
		lv_popu_one_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				for (int i = 0; i < datas.size(); i++) {
					Map<String, String> dataMap = (Map<String, String>) datas
							.get(i);
					// for (String string: dataMap.keySet()) {
					// if (string.equals("isTextRed") && i == position) {
					// System.out.println("===========");
					// dataMap.put(string, "true");
					// }else {
					// dataMap.put(string, "false");
					// }
					// break;
					// }
				}
				// Map<String, String> dataMap = (Map<String, String>)
				// datas.get(position);
				// for (String string: dataMap.keySet()) {
				// if (string.equals("isTextRed")) {
				// dataMap.put(string, "true");
				// break;
				// }
				// }
				// for (int i = 0; i < datas.size(); i++) {
				// System.out.println("----------"+datas.get(i).get("isTextRed"));;
				//
				// }
				// ((TextView) view.findViewById(R.id.tv_popu_item))
				// .setTextColor(context.getResources().getColor(
				// R.color.new_red));
				// simpleAdapter.notifyDataSetChanged();
				Map<String, String> map = datas.get(position);
				setOnPopuwindowListener.onPopuwindowOnClick(map.get("name"),
						map.get("id"), type);
				dismiss();
			}
		});
		this.setContentView(rootView);
	}

	public void setSetOnAddOrSubtractListener(
			PopuwindowOnSeleckedListener setOnPopuwindowListener) {
		this.setOnPopuwindowListener = setOnPopuwindowListener;
	}

}
