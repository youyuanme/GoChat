package com.yibingding.haolaiwu.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

import com.ybd.app.interf.IsNetConnectedListener;
import com.yibingding.haolaiwu.R;
import com.yibingding.haolaiwu.adapter.MySimpleAdapter;
import com.yibingding.haolaiwu.domian.City;
import com.yibingding.haolaiwu.domian.District;
import com.yibingding.haolaiwu.domian.Province;

public class MypopuwindowThree extends PopupWindow {

	private View rootView;
	private ListView lv_popu_left_listview;
	private ListView lv_popu_middle_listview;
	private ListView lv_popu_right_listview;
	private PopWindowOnSelectedListener popWindowSelectedListener;

	int provincePosition = 0;
	int cityPosition = 0;

	public <T> MypopuwindowThree(final Context context,
			final List<Province> list) {
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
		rootView = layoutInflater.inflate(R.layout.popu_three_listview, null);
		lv_popu_left_listview = (ListView) rootView
				.findViewById(R.id.lv_popu_left_listview);
		lv_popu_right_listview = (ListView) rootView
				.findViewById(R.id.lv_popu_right_listview);
		lv_popu_middle_listview = (ListView) rootView
				.findViewById(R.id.lv_popu_middle_listview);
		lv_popu_middle_listview.setVisibility(View.INVISIBLE);
		lv_popu_right_listview.setVisibility(View.INVISIBLE);

		final List<Map<String, String>> datas = new ArrayList<Map<String, String>>();
		for (int i = 0; i < list.size(); i++) {
			Province province = list.get(i);
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", province.getProvinceID());
			map.put("name", province.getProvinceName());
			map.put("isTextRed", "flase");
			datas.add(map);
		}
		SimpleAdapter simpleAdapter = new SimpleAdapter(context, datas,
				R.layout.popu_three_item, new String[] { "name" },
				new int[] { R.id.tv_popu_item });
		// final MySimpleAdapter simpleAdapter = new MySimpleAdapter(context,
		// datas, null);
		lv_popu_left_listview.setAdapter(simpleAdapter);
		lv_popu_left_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				provincePosition = position;
				// Map<String, String> map1 = datas.get(position);
				// for (String str : map1.keySet()) {
				// if (str.equals("isTextRed")) {
				// map1.put(str, "true");
				// }
				// }
				// simpleAdapter.notifyDataSetChanged();

				final List<Map<String, String>> datasSecond = new ArrayList<Map<String, String>>();

				final List<City> list_city = list.get(position).getCities();
				for (int i = 0; i < list_city.size(); i++) {
					City city = list_city.get(i);
					Map<String, String> map = new HashMap<String, String>();
					map.put("id", city.getCityID());
					map.put("name", city.getCityName());
					datasSecond.add(map);
				}
				if (datasSecond.size() > 0) {
					lv_popu_middle_listview.setVisibility(View.VISIBLE);
					SimpleAdapter simpleAdapter = new SimpleAdapter(context,
							datasSecond, R.layout.popu_item,
							new String[] { "name" },
							new int[] { R.id.tv_popu_item });
					lv_popu_middle_listview.setAdapter(simpleAdapter);
					lv_popu_middle_listview
							.setOnItemClickListener(new OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
									cityPosition = position;
									final List<Map<String, String>> datasThird = new ArrayList<Map<String, String>>();

									final List<District> list_district = list_city
											.get(position).getDistricts();
									for (int i = 0; i < list_district.size(); i++) {
										District district = list_district
												.get(i);
										Map<String, String> map = new HashMap<String, String>();
										map.put("id", district.getDistrictID());
										map.put("name",
												district.getDistrictName());
										datasThird.add(map);
									}

									if (datasThird.size() > 0) {
										lv_popu_right_listview
												.setVisibility(View.VISIBLE);
										SimpleAdapter simpleAdapter = new SimpleAdapter(
												context, datasThird,
												R.layout.popu_item,
												new String[] { "name" },
												new int[] { R.id.tv_popu_item });
										lv_popu_right_listview
												.setAdapter(simpleAdapter);
										lv_popu_right_listview
												.setOnItemClickListener(new OnItemClickListener() {

													@Override
													public void onItemClick(
															AdapterView<?> parent,
															View view,
															int position,
															long id) {
														Map<String, String> mapThird = datasThird
																.get(position);
														if (popWindowSelectedListener != null) {
															popWindowSelectedListener
																	.popWindowOnClicked(
																			position,
																			list.get(provincePosition),
																			list_city
																					.get(cityPosition),
																			list_district
																					.get(position));

														}
														dismiss();
													}

												});
									}

								}
							});
					lv_popu_middle_listview.performItemClick(
							simpleAdapter.getView(0, null, null), 0,
							lv_popu_middle_listview.getItemIdAtPosition(0));
				}

			}
		});
		lv_popu_left_listview.performItemClick(
				simpleAdapter.getView(0, null, null), 0,
				lv_popu_left_listview.getItemIdAtPosition(0));
		this.setContentView(rootView);
	}

	public interface PopWindowOnSelectedListener {
		public void popWindowOnClicked(int position, Province province,
				City city, District district);
	}

	public void setOnPopWindowClickedListener(
			PopWindowOnSelectedListener popWindowSelectedListener) {
		this.popWindowSelectedListener = popWindowSelectedListener;
	}

}
