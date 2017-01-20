package com.yibingding.haolaiwu.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.ybd.app.BaseActivity;
import com.yibingding.haolaiwu.MainActivity;
import com.yibingding.haolaiwu.R;
import com.yibingding.haolaiwu.activity.baidumaplistener.MyLocationListener;
import com.yibingding.haolaiwu.activity.baidumaplistener.MyLocationListener.LocationResuteListener;
import com.yibingding.haolaiwu.adapter.listview.Host_HisCityList;
import com.yibingding.haolaiwu.adapter.listview.SortAdapter;
import com.yibingding.haolaiwu.dialog.Dialognetwork;
import com.yibingding.haolaiwu.domian.CityInfo;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.CharacterParser;
import com.yibingding.haolaiwu.tools.Constants;
import com.yibingding.haolaiwu.tools.MyApplication;
import com.yibingding.haolaiwu.tools.MyConstant;
import com.yibingding.haolaiwu.tools.MyUtils;
import com.yibingding.haolaiwu.weight.NoScrollGridView;
import com.yibingding.haolaiwu.weight.PinyinComparator;
import com.yibingding.haolaiwu.weight.SideBar;
import com.yibingding.haolaiwu.weight.SideBar.OnTouchingLetterChangedListener;

public class GetLocationActivity extends BaseActivity implements
		OnItemClickListener {

	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener(
			new LocationResuteListener() {
				@Override
				public void onResult(final BDLocation location) {
					tv_mylocation.setText(location.getCity());
					tv_mylocation.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							sp.edit()
									.putString(MyConstant.CITY_NAME,
											location.getCity()).commit();
							MyApplication.city = location.getCity();
							setResult(RESULT_OK);
							finish();
						}
					});
				}

				@Override
				public void onFault() {
					tv_mylocation.setText("定位失败请手动选择。");
				}
			});
	private SideBar sideBar;

	private void show() {
		if (dialog == null) {
			dialog = new Dialognetwork(this);
		}
		dialog.show();
	}

	private void dismiss() {
		if (dialog != null) {
			dialog.dismiss();
		}
	}

	@Override
	public void initViews() {
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		int span = 1000;
		option.setScanSpan(0);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(false);// 可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
		option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		mLocationClient.setLocOption(option);
		mLocationClient.start();
		lv_citys = (ListView) findViewById(R.id.lv_citys);
		// TODO
		ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
		setBack(iv_back);

		View listHead = View.inflate(ctx, R.layout.citys_headview, null);
		lv_citys.addHeaderView(listHead);
		sideBar = (SideBar) findViewById(R.id.sidrbar);
		// TODO
		// adapter = new SortAdapter(ctx, cityList);

		grid_citys_hittory = (NoScrollGridView) findViewById(R.id.grid_citys_hittory);

		tv_mylocation = (TextView) listHead.findViewById(R.id.tv_mylocation);

		sp = getSharedPreferences(MyConstant.SP_CONFIG_NAME,
				Activity.MODE_PRIVATE);
		show();
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				if (cityList != null && cityList.size() != 0) {
					int position = adapter.getPositionForSection(s.charAt(0));
					if (position != -1) {
						lv_citys.setSelection(position);
					}
				}
			}
		});

		RelativeLayout.LayoutParams lpara = (LayoutParams) sideBar
				.getLayoutParams();
		lpara.height = (int) (MyUtils.getWindowHeight(ctx) * 0.8);
		sideBar.setLayoutParams(lpara);

		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		initHistoryList();
	}

	private void setBack(ImageView iv_back) {
		iv_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// setResult(RESULT_OK);
				finish();
			}
		});
	}

	private void initHistoryList() {
		// grid_citys_hittory
		String towns = sp.getString(MyConstant.HISTORY_CITY_NAME, "");
		if (TextUtils.isEmpty(towns)) {
			return;
		}
		String[] townArr = towns.split("#");
		historyCity = new ArrayList<CityInfo>();
		for (int i = 0; i < townArr.length; i++) {
			CityInfo city = new CityInfo();
			city.name = townArr[i];
			historyCity.add(city);
		}
		grid_citys_hittory.setAdapter(new Host_HisCityList(ctx, historyCity));
		grid_citys_hittory.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				sp.edit()
						.putString(MyConstant.CITY_NAME,
								historyCity.get(position).name).commit();
				MyApplication.city = historyCity.get(position).name;
				setResult(RESULT_OK);
				finish();
			}
		});
	}

	private void initList() {
		Collections.sort(cityList, pinyinComparator);
		adapter = new SortAdapter(ctx, cityList);
		lv_citys.setAdapter(adapter);
		lv_citys.setOnItemClickListener(this);
	}

	private List<CityInfo> cityList;
	private ListView lv_citys;
	private PinyinComparator pinyinComparator;
	private CharacterParser characterParser;
	private SortAdapter adapter;
	private TextView tv_mylocation;

	@Override
	public void initData() {
		RequestQueue mRequestQueue = Volley.newRequestQueue(this);
		System.out.println(AESUtils.encode("Token"));
		StringRequest result = new StringRequest(Constants.GETCITY_URL
				+ "?Token=" + AESUtils.encode("Token"), new Listener<String>() {

			@Override
			public void onResponse(String response) {
				// 请求的结果
				dialog.dismiss();
				try {
					JSONObject resultJson = new JSONArray(response)
							.getJSONObject(0);
					if ("true".equals(resultJson.getString("state"))) {
						getCitys(resultJson.getJSONArray("result"));

					} else {

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			private void getCitys(JSONArray jsonArray) throws JSONException {
				cityList = new ArrayList<CityInfo>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					CityInfo city = new CityInfo();
					city.name = jsonObject.getString("CityName");
					String pinyin = characterParser.getSelling(city.name);
					String sortString = pinyin.substring(0, 1).toUpperCase();

					// 正则表达式，判断首字母是否是英文字母
					if (sortString.matches("[A-Z]")) {
						city.firstN = sortString.toUpperCase();
					} else {
						city.firstN = "#";
					}
					cityList.add(city);
				}
				initList();

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// 网络请求失败
				VolleyLog.e("Error: ", error.getMessage());
			}
		});
		mRequestQueue.add(result);
	}

	/**
	 * 历史城镇进行缓存
	 * 
	 * @param name
	 * @param id
	 */
	private void save(String name) {
		String town_history = sp.getString(MyConstant.HISTORY_CITY_NAME, "");
		if (TextUtils.isEmpty(town_history)) {
			sp.edit().putString(MyConstant.HISTORY_CITY_NAME, name).commit();
		} else {
			if (town_history.contains(name)) {
				return;
			} else {
				town_history += ("#" + name);
				sp.edit().putString(MyConstant.HISTORY_CITY_NAME, town_history)
						.commit();
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position < 1) {
			return;
		}
		// TODO 选定城市
		save(cityList.get(position - 1).name);
		sp.edit()
				.putString(MyConstant.CITY_NAME,
						cityList.get(position - 1).name).commit();
		MyApplication.city = cityList.get(position - 1).name;
		setResult(RESULT_OK);
		finish();
	}

	private SharedPreferences sp;
	private NoScrollGridView grid_citys_hittory;
	private List<CityInfo> historyCity;
	private Context ctx;
	private Dialognetwork dialog;

	@Override
	public void onCreateThisActivity() {
		ctx = this;
		setContentView(R.layout.activity_selecttown);
	}

	@Override
	protected void onStart() {
		// 开启图层定位
		// mBaiduMap.setMyLocationEnabled(true);
		if (!mLocationClient.isStarted()) {
			mLocationClient.start();
		}
		super.onStart();
	}

	@Override
	protected void onStop() {
		// 关闭图层定位
		// mBaiduMap.setMyLocationEnabled(false);
		mLocationClient.stop();

		super.onStop();
	}
}
