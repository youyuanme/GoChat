package com.yibingding.haolaiwu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.ybd.app.BaseScrollViewActivity;
import com.ybd.app.interf.GetDataSuccessListener;
import com.ybd.app.tools.Tools;
import com.ybd.app.views.MyListView;
import com.yibingding.haolaiwu.adapter.HouseRentAdapter;
import com.yibingding.haolaiwu.domian.City;
import com.yibingding.haolaiwu.domian.ConditionSelection;
import com.yibingding.haolaiwu.domian.District;
import com.yibingding.haolaiwu.domian.HouseRentItem;
import com.yibingding.haolaiwu.domian.Province;
import com.yibingding.haolaiwu.internet.GetConditionSelectionClassifi;
import com.yibingding.haolaiwu.internet.GetOldHouseList;
import com.yibingding.haolaiwu.internet.GetProvinceCity;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.Constants;
import com.yibingding.haolaiwu.tools.MyApplication;
import com.yibingding.haolaiwu.view.MyPopuwindow;
import com.yibingding.haolaiwu.view.MypopuwindowThree;
import com.yibingding.haolaiwu.view.MypopuwindowThree.PopWindowOnSelectedListener;
import com.yibingding.haolaiwu.view.PopuwindowOnSeleckedListener;

public class OldHouseActivity extends BaseScrollViewActivity implements
		OnClickListener, PopWindowOnSelectedListener,
		PopuwindowOnSeleckedListener {

	private EditText editTextSearch;
	private TextView textViewArea, textViewHouse, textViewBuildingType,
			textViewDecorateType;
	private MyListView myListViewOldHouse;

	private MyPopuwindow myPopuwindowHouse, myPopuwindowBuildingType,
			myPopuwindowDecorateType;
	private MypopuwindowThree mypopuwindowArea;

	private String pageSize = "10";
	private String locatedCityName = MyApplication.city;

	private String provinceId = "", cityId = "", districtId = "";
	private String house = "", buildingType = "", decorateType = "";

	private List<HouseRentItem> houseRentItems;
	private HouseRentAdapter houseRentAdapter;

	public void back(View view) {
		finish();
	}

	@Override
	public PullToRefreshScrollView initScrollView() {
		return (PullToRefreshScrollView) this
				.findViewById(R.id.scrollViewOldHouse);
	}

	@Override
	public void refresh(GetDataSuccessListener getDataSuccessListener) {
		String url = Constants.GET_OLD_HOUSE_LIST;
		Map<String, String> map = new HashMap<String, String>();
		map.put("page", pageIndex + "");
		map.put("pagesize", pageSize);
		map.put("cityName", locatedCityName);
		map.put("province", provinceId);
		map.put("city", cityId);
		map.put("district", districtId);
		map.put("decorate", decorateType);
		map.put("model", house);
		map.put("buildType", buildingType);
		map.put("key", "");
		map.put("Token", AESUtils.encode("page").replaceAll("\n", ""));
		System.out.println("===========map.toString()=====" + map.toString());
		GetOldHouseList getOldHouseList = new GetOldHouseList(this, url, map);
		getOldHouseList.setOnGetDataSuccessListener(getDataSuccessListener);
	}

	@Override
	public void loadMoreData(int pageindex,
			GetDataSuccessListener getDataSuccessListener) {
		String url = Constants.GET_OLD_HOUSE_LIST;
		Map<String, String> map = new HashMap<String, String>();
		map.put("page", pageIndex + "");
		map.put("pagesize", pageSize);
		map.put("cityName", locatedCityName);
		map.put("province", provinceId);
		map.put("city", cityId);
		map.put("district", districtId);
		map.put("decorate", decorateType);
		map.put("model", house);
		map.put("buildType", buildingType);
		map.put("key", "");
		map.put("Token", AESUtils.encode("page").replaceAll("\n", ""));
		System.out.println("===========map.toString()=====" + map.toString());
		GetOldHouseList getOldHouseList = new GetOldHouseList(this, url, map);
		getOldHouseList.setOnGetDataSuccessListener(getDataSuccessListener);
	}

	@Override
	public void setDataToView(String tag, Object obj) {
		houseRentItems = (List<HouseRentItem>) obj;
		houseRentAdapter = new HouseRentAdapter(this, houseRentItems,
				Constants.OLD_HOUSE);
		myListViewOldHouse.setAdapter(houseRentAdapter);
	}

	@Override
	public void addItems(Object obj) {
		List<HouseRentItem> list = (List<HouseRentItem>) obj;
		houseRentItems.addAll(list);
		houseRentAdapter.notifyDataSetChanged();
	}

	@Override
	public void onCreateThisActivity() {
		setContentView(R.layout.activity_old_house);
	}

	@Override
	public void initViews() {
		super.initViews();
		editTextSearch = (EditText) this.findViewById(R.id.editTextSearch);
		textViewArea = (TextView) this.findViewById(R.id.textViewArea);
		textViewHouse = (TextView) this.findViewById(R.id.textViewHouse);
		textViewBuildingType = (TextView) this
				.findViewById(R.id.textViewBuildingType);
		textViewDecorateType = (TextView) this
				.findViewById(R.id.textViewDecorateType);
		myListViewOldHouse = (MyListView) this
				.findViewById(R.id.myListViewOldHouse);

		editTextSearch.setOnClickListener(this);
		textViewArea.setOnClickListener(this);
		textViewHouse.setOnClickListener(this);
		textViewBuildingType.setOnClickListener(this);
		textViewDecorateType.setOnClickListener(this);

		mPullRefreshScrollView.setMode(Mode.BOTH);

		initAreaData();
		initHouseData();
		initBuildingTypeData();
		initDecorateTypeData();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.editTextSearch:
			Tools.startActivity(this, SearchActivity.class);
			break;

		case R.id.textViewArea:
			if (mypopuwindowArea == null)
				return;

			if (!mypopuwindowArea.isShowing()) {
				mypopuwindowArea.showAsDropDown(textViewArea, 0, 0);
				mypopuwindowArea.setOnPopWindowClickedListener(this);
			} else {
				mypopuwindowArea.dismiss();
			}
			break;

		case R.id.textViewHouse:
			if (myPopuwindowHouse == null)
				return;

			if (!myPopuwindowHouse.isShowing()) {
				myPopuwindowHouse.showAsDropDown(textViewArea, 0, 0);
				myPopuwindowHouse.setSetOnAddOrSubtractListener(this);
			} else {
				myPopuwindowHouse.dismiss();
			}
			break;

		case R.id.textViewBuildingType:
			if (myPopuwindowBuildingType == null)
				return;

			if (!myPopuwindowBuildingType.isShowing()) {
				myPopuwindowBuildingType.showAsDropDown(textViewArea, 0, 0);
				myPopuwindowBuildingType.setSetOnAddOrSubtractListener(this);
			} else {
				myPopuwindowBuildingType.dismiss();
			}
			break;

		case R.id.textViewDecorateType:
			if (myPopuwindowDecorateType == null)
				return;

			if (!myPopuwindowDecorateType.isShowing()) {
				myPopuwindowDecorateType.showAsDropDown(textViewArea, 0, 0);
				myPopuwindowDecorateType.setSetOnAddOrSubtractListener(this);
			} else {
				myPopuwindowDecorateType.dismiss();
			}
			break;
		}

	}

	private void initAreaData() {
		List<Map<String, String>> maps = new ArrayList<Map<String, String>>();
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("id", "0");
		map1.put("name", "不限");
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("id", "2");
		map2.put("name", "招标中");
		Map<String, String> map3 = new HashMap<String, String>();
		map3.put("id", "3");
		map3.put("name", "还款中");
		Map<String, String> map4 = new HashMap<String, String>();
		map4.put("id", "4");
		map2.put("name", "已还清");
		String url = Constants.GET_PROVINCE_CITY;
		Map<String, String> map = new HashMap<String, String>();
		map.put("Token", AESUtils.encode("Token").replaceAll("\n", ""));
		GetProvinceCity getProvinceCity = new GetProvinceCity(this, url, map);
		getProvinceCity
				.setOnGetDataSuccessListener(new GetDataSuccessListener() {

					@Override
					public void onGetDataSuccess(String tag, Object obj) {
						List<Province> provinces = (List<Province>) obj;
						mypopuwindowArea = new MypopuwindowThree(
								OldHouseActivity.this, provinces);
					}
				});
	}

	private void initHouseData() {
		List<Map<String, String>> maps = new ArrayList<Map<String, String>>();
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("id", "0");
		map1.put("name", "不限");
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("id", "2");
		map2.put("name", "招标中");
		Map<String, String> map3 = new HashMap<String, String>();
		map3.put("id", "3");
		map3.put("name", "还款中");
		Map<String, String> map4 = new HashMap<String, String>();
		map4.put("id", "4");
		map2.put("name", "已还清");
		String url = Constants.GET_CONDITION_SELECTION_CLASSIFI;
		Map<String, String> map = new HashMap<String, String>();
		map.put("typeGuid",
				Constants.CONDITION_SELECTION_CLASSIFI_ID_HOUSE_TYPE);
		map.put("Token", AESUtils.encode("typeGuid").replaceAll("\n", ""));
		GetConditionSelectionClassifi getConditionSelectionClassifi = new GetConditionSelectionClassifi(
				this, url, map);
		getConditionSelectionClassifi
				.setOnGetDataSuccessListener(new GetDataSuccessListener() {

					@Override
					public void onGetDataSuccess(String tag, Object obj) {
						List<ConditionSelection> conditionSelections = (List<ConditionSelection>) obj;
						List<Map<String, String>> maps = new ArrayList<Map<String, String>>();

						for (int i = 0; i < conditionSelections.size(); i++) {
							Map<String, String> map = new HashMap<String, String>();
							map.put("id", conditionSelections.get(i).getGuid());
							map.put("name", conditionSelections.get(i)
									.getT_Style_Name());
							maps.add(map);
						}
						myPopuwindowHouse = new MyPopuwindow(
								OldHouseActivity.this, maps, "house");
					}
				});

	}

	private void initBuildingTypeData() {
		List<Map<String, String>> maps = new ArrayList<Map<String, String>>();
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("id", "0");
		map1.put("name", "不限");
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("id", "2");
		map2.put("name", "招标中");
		Map<String, String> map3 = new HashMap<String, String>();
		map3.put("id", "3");
		map3.put("name", "还款中");
		Map<String, String> map4 = new HashMap<String, String>();
		map4.put("id", "4");
		map2.put("name", "已还清");
		String url = Constants.GET_CONDITION_SELECTION_CLASSIFI;
		Map<String, String> map = new HashMap<String, String>();
		map.put("typeGuid", Constants.CONDITION_SELECTION_CLASSIFI_ID_TYPE);
		map.put("Token", AESUtils.encode("typeGuid").replaceAll("\n", ""));
		GetConditionSelectionClassifi getConditionSelectionClassifi = new GetConditionSelectionClassifi(
				this, url, map);
		getConditionSelectionClassifi
				.setOnGetDataSuccessListener(new GetDataSuccessListener() {
					@Override
					public void onGetDataSuccess(String tag, Object obj) {
						List<ConditionSelection> conditionSelections = (List<ConditionSelection>) obj;
						List<Map<String, String>> maps = new ArrayList<Map<String, String>>();
						for (int i = 0; i < conditionSelections.size(); i++) {
							Map<String, String> map = new HashMap<String, String>();
							map.put("id", conditionSelections.get(i).getGuid());
							map.put("name", conditionSelections.get(i)
									.getT_Style_Name());
							maps.add(map);
						}
						myPopuwindowBuildingType = new MyPopuwindow(
								OldHouseActivity.this, maps, "buildingType");
					}
				});
	}

	private void initDecorateTypeData() {
		List<Map<String, String>> maps = new ArrayList<Map<String, String>>();
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("id", "0");
		map1.put("name", "不限");
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("id", "2");
		map2.put("name", "招标中");
		Map<String, String> map3 = new HashMap<String, String>();
		map3.put("id", "3");
		map3.put("name", "还款中");
		Map<String, String> map4 = new HashMap<String, String>();
		map4.put("id", "4");
		map2.put("name", "已还清");
		String url = Constants.GET_CONDITION_SELECTION_CLASSIFI;
		Map<String, String> map = new HashMap<String, String>();
		map.put("typeGuid",
				Constants.CONDITION_SELECTION_CLASSIFI_ID_DECORATE_TYPE);
		map.put("Token", AESUtils.encode("typeGuid").replaceAll("\n", ""));
		GetConditionSelectionClassifi getConditionSelectionClassifi = new GetConditionSelectionClassifi(
				this, url, map);
		getConditionSelectionClassifi
				.setOnGetDataSuccessListener(new GetDataSuccessListener() {
					@Override
					public void onGetDataSuccess(String tag, Object obj) {
						List<ConditionSelection> conditionSelections = (List<ConditionSelection>) obj;

						List<Map<String, String>> maps = new ArrayList<Map<String, String>>();
						for (int i = 0; i < conditionSelections.size(); i++) {
							Map<String, String> map = new HashMap<String, String>();
							map.put("id", conditionSelections.get(i).getGuid());
							map.put("name", conditionSelections.get(i)
									.getT_Style_Name());
							maps.add(map);
						}
						myPopuwindowDecorateType = new MyPopuwindow(
								OldHouseActivity.this, maps, "decorateType");
					}
				});
	}

	@Override
	public void popWindowOnClicked(int position, Province province, City city,
			District district) {
		provinceId = province.getProvinceID();
		cityId = city.getCityID();
		districtId = district.getDistrictID();
		textViewArea.setText(district.getDistrictName());
		locatedCityName = "";
		pageIndex = 1;
		initData();
	}

	@Override
	public void onPopuwindowOnClick(String name, String id, String type) {
		if ("house".equals(type)) {
			house = id;
			locatedCityName = "";
			textViewHouse.setText(name);
		}

		if ("buildingType".equals(type)) {
			buildingType = id;
			locatedCityName = "";
			textViewBuildingType.setText(name);
		}

		if ("decorateType".equals(type)) {
			decorateType = id;
			locatedCityName = "";
			textViewDecorateType.setText(name);
		}

		pageIndex = 1;
		initData();

	}

}
