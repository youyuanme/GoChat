package com.yibingding.haolaiwu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

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
import com.yibingding.haolaiwu.internet.GetProvinceCity;
import com.yibingding.haolaiwu.internet.GetRentHouseList;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.Constants;
import com.yibingding.haolaiwu.tools.MyApplication;
import com.yibingding.haolaiwu.view.MyPopuwindow;
import com.yibingding.haolaiwu.view.MypopuwindowThree;
import com.yibingding.haolaiwu.view.MypopuwindowThree.PopWindowOnSelectedListener;
import com.yibingding.haolaiwu.view.PopuwindowOnSeleckedListener;

public class HouseRentActivity extends BaseScrollViewActivity implements
		OnClickListener, PopWindowOnSelectedListener,
		PopuwindowOnSeleckedListener {

	private EditText editTextSearch;
	private TextView textViewArea, textViewHouse, textViewRentMoney,
			textViewDecorateType;
	private MyListView myListViewHouseRent;

	private MyPopuwindow myPopuwindowHouse, myPopuwindowRentMoneyType,
			myPopuwindowDecorateType;
	private MypopuwindowThree mypopuwindowArea;

	private String pageSize = "10";
	private String locatedCityName = MyApplication.city;

	private String provinceId = "", cityId = "", districtId = "";
	private String house = "", rentMoneyType = "", decorateType = "";

	private List<HouseRentItem> houseRentItems;
	private HouseRentAdapter houseRentAdapter;

	public void back(View view) {
		finish();
	}

	@Override
	public PullToRefreshScrollView initScrollView() {
		return (PullToRefreshScrollView) this
				.findViewById(R.id.scrollViewHouseRent);
	}

	@Override
	public void refresh(GetDataSuccessListener getDataSuccessListener) {
		String url = Constants.GET_RENT_HOUSE_LIST;
		Map<String, String> map = new HashMap<String, String>();
		map.put("page", pageIndex + "");
		map.put("pagesize", pageSize);
		map.put("cityName", locatedCityName);
		map.put("province", provinceId);
		map.put("city", cityId);
		map.put("district", districtId);
		map.put("decorate", decorateType);
		map.put("model", house);
		map.put("rentalType", rentMoneyType);
		map.put("key", "");
		map.put("Token", AESUtils.encode("page").replaceAll("\n", ""));
		System.out.println("===========map.toString()===="+map.toString());
		GetRentHouseList getRentHouseList = new GetRentHouseList(this, url, map);
		getRentHouseList.setOnGetDataSuccessListener(getDataSuccessListener);
	}

	@Override
	public void loadMoreData(int pageindex,
			GetDataSuccessListener getDataSuccessListener) {
		String url = Constants.GET_RENT_HOUSE_LIST;
		Map<String, String> map = new HashMap<String, String>();
		map.put("page", pageIndex + "");
		map.put("pagesize", pageSize);
		map.put("cityName", locatedCityName);
		map.put("province", provinceId);
		map.put("city", cityId);
		map.put("district", districtId);
		map.put("decorate", decorateType);
		map.put("model", house);
		map.put("rentalType", rentMoneyType);
		map.put("key", "");
		map.put("Token", AESUtils.encode("page").replaceAll("\n", ""));
		System.out.println("===========map.toString()===="+map.toString());
		GetRentHouseList getRentHouseList = new GetRentHouseList(this, url, map);
		getRentHouseList.setOnGetDataSuccessListener(getDataSuccessListener);
	}

	@Override
	public void setDataToView(String tag, Object obj) {
		houseRentItems = (List<HouseRentItem>) obj;
		houseRentAdapter = new HouseRentAdapter(this, houseRentItems,
				Constants.HOUSE_RENT);
		myListViewHouseRent.setAdapter(houseRentAdapter);
	}

	@Override
	public void addItems(Object obj) {
		List<HouseRentItem> list = (List<HouseRentItem>) obj;
		houseRentItems.addAll(list);
		houseRentAdapter.notifyDataSetChanged();
	}

	@Override
	public void onCreateThisActivity() {
		setContentView(R.layout.activity_house_rent);
	}

	@Override
	public void initViews() {
		super.initViews();
		editTextSearch = (EditText) this.findViewById(R.id.editTextSearch);
		textViewArea = (TextView) this.findViewById(R.id.textViewArea);
		textViewHouse = (TextView) this.findViewById(R.id.textViewHouse);
		textViewRentMoney = (TextView) this
				.findViewById(R.id.textViewRentMoney);
		textViewDecorateType = (TextView) this
				.findViewById(R.id.textViewDecorateType);
		myListViewHouseRent = (MyListView) this
				.findViewById(R.id.myListViewHouseRent);

		editTextSearch.setOnClickListener(this);
		textViewArea.setOnClickListener(this);
		textViewHouse.setOnClickListener(this);
		textViewRentMoney.setOnClickListener(this);
		textViewDecorateType.setOnClickListener(this);

		initAreaData();
		initHouseData();
		initRentMoneyData();
		initDecorateTypeData();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (myPopuwindowHouse != null && myPopuwindowHouse.isShowing()) {
			myPopuwindowHouse.dismiss();
		}
		if (myPopuwindowRentMoneyType != null && myPopuwindowRentMoneyType.isShowing()) {
			myPopuwindowRentMoneyType.dismiss();
		}
		if (myPopuwindowDecorateType != null && myPopuwindowDecorateType.isShowing()) {
			myPopuwindowDecorateType.dismiss();
		}
		if (mypopuwindowArea != null && mypopuwindowArea.isShowing()) {
			mypopuwindowArea.dismiss();
		}
		super.onDestroy();
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

		case R.id.textViewRentMoney:
			if (myPopuwindowRentMoneyType == null)
				return;

			if (!myPopuwindowRentMoneyType.isShowing()) {
				myPopuwindowRentMoneyType.showAsDropDown(textViewArea, 0, 0);
				myPopuwindowRentMoneyType.setSetOnAddOrSubtractListener(this);
			} else {
				myPopuwindowRentMoneyType.dismiss();
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
								HouseRentActivity.this, provinces);
					}
				});
	}

	private void initHouseData() {
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
								HouseRentActivity.this, maps, "house");
					}
				});

	}

	private void initRentMoneyData() {
		String url = Constants.GET_CONDITION_SELECTION_CLASSIFI;
		Map<String, String> map = new HashMap<String, String>();
		map.put("typeGuid",
				Constants.CONDITION_SELECTION_CLASSIFI_ID_RENT_MONEY_TYPE);
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
						myPopuwindowRentMoneyType = new MyPopuwindow(
								HouseRentActivity.this, maps, "rentMoneyType");
					}
				});

	}

	private void initDecorateTypeData() {
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
							map.put("isTextRed", "flase");
							maps.add(map);
						}
						myPopuwindowDecorateType = new MyPopuwindow(
								HouseRentActivity.this, maps, "decorateType");
					}
				});

	}

	@Override
	public void popWindowOnClicked(int position, Province province,
			City city, District district) {
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

		if ("rentMoneyType".equals(type)) {
			rentMoneyType = id;
			locatedCityName = "";
			textViewRentMoney.setText(name);
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
