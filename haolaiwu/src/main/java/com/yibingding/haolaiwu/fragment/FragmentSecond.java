package com.yibingding.haolaiwu.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.ybd.app.BaseScrollViewFragment;
import com.ybd.app.interf.GetDataSuccessListener;
import com.ybd.app.tools.DensityUtils;
import com.ybd.app.tools.Tools;
import com.ybd.app.views.MyListView;
import com.yibingding.haolaiwu.R;
import com.yibingding.haolaiwu.SearchActivity;
import com.yibingding.haolaiwu.adapter.HouseRentAdapter;
import com.yibingding.haolaiwu.domian.City;
import com.yibingding.haolaiwu.domian.ConditionSelection;
import com.yibingding.haolaiwu.domian.District;
import com.yibingding.haolaiwu.domian.HouseRentItem;
import com.yibingding.haolaiwu.domian.Province;
import com.yibingding.haolaiwu.internet.GetBuildingList;
import com.yibingding.haolaiwu.internet.GetConditionSelectionClassifi;
import com.yibingding.haolaiwu.internet.GetProvinceCity;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.Constants;
import com.yibingding.haolaiwu.tools.MyApplication;
import com.yibingding.haolaiwu.view.MyPopuwindow;
import com.yibingding.haolaiwu.view.MypopuwindowThree;
import com.yibingding.haolaiwu.view.MypopuwindowThree.PopWindowOnSelectedListener;
import com.yibingding.haolaiwu.view.PopuwindowOnSeleckedListener;

/*楼盘*/
public class FragmentSecond extends BaseScrollViewFragment implements
		OnClickListener, PopWindowOnSelectedListener,
		PopuwindowOnSeleckedListener {

	private ImageView imageViewBack;
	private EditText editTextSearch;
	private TextView textViewArea, textViewMoney, textViewHouse, textViewType;
	private MyListView myListViewBuilding;

	private MyPopuwindow myPopuwindowMoney, myPopuwindowHouse,
			myPopuwindowType;
	private MypopuwindowThree mypopuwindowArea;
	private String pageSize = "10";
	private String locatedCityName = MyApplication.city;
	private String provinceId = "", cityId = "", districtId = "";
	private String money = "", house = "", type = "";
	private List<HouseRentItem> houseRentItems;
	private HouseRentAdapter houseRentAdapter;
	private Context context;

	@Override
	public PullToRefreshScrollView initScrollView() {
		return (PullToRefreshScrollView) viewFragment
				.findViewById(R.id.scrollViewBuilding);
	}

	@Override
	public void refresh(GetDataSuccessListener getDataSuccessListener) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("page", pageIndex + "");
		map.put("pagesize", pageSize);
		map.put("cityName", locatedCityName);
		map.put("province", provinceId);
		map.put("city", cityId);
		map.put("district", districtId);
		map.put("totlePrice", money);
		map.put("model", house);
		map.put("buildType", type);
		map.put("key", "");
		map.put("Token", AESUtils.encode("page").replaceAll("\n", ""));
		GetBuildingList getBuildingList = new GetBuildingList(getActivity(),
				Constants.GET_BUILDING_LIST, map);
		getBuildingList.setOnGetDataSuccessListener(getDataSuccessListener);
	}

	@Override
	public void loadMoreData(int pageindex,
			GetDataSuccessListener getDataSuccessListener) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("page", pageIndex + "");
		map.put("pagesize", pageSize);
		map.put("cityName", locatedCityName);
		map.put("province", provinceId);
		map.put("city", cityId);
		map.put("district", districtId);
		map.put("totlePrice", money);
		map.put("model", house);
		map.put("buildType", type);
		map.put("key", "");
		map.put("Token", AESUtils.encode("page").replaceAll("\n", ""));
		GetBuildingList getBuildingList = new GetBuildingList(getActivity(),
				Constants.GET_BUILDING_LIST, map);
		getBuildingList.setOnGetDataSuccessListener(getDataSuccessListener);
	}

	@Override
	public void setDataToView(String tag, Object obj) {
		houseRentItems = (List<HouseRentItem>) obj;
		houseRentAdapter = new HouseRentAdapter(getActivity(), houseRentItems,
				Constants.BUILDING);
		myListViewBuilding.setAdapter(houseRentAdapter);

	}

	@Override
	public void addItems(Object obj) {
		List<HouseRentItem> list = (List<HouseRentItem>) obj;
		houseRentItems.addAll(list);
		houseRentAdapter.notifyDataSetChanged();
	}

	@Override
	public View onCreateThisFragment(LayoutInflater inflater,
			ViewGroup container) {
		this.context = getActivity();
		return inflater.inflate(R.layout.fragment_second, null);
	}

	@Override
	public void initViews(View view) {
		super.initViews(view);
		imageViewBack = (ImageView) view.findViewById(R.id.imageViewBack);
		editTextSearch = (EditText) view.findViewById(R.id.editTextSearch);
		textViewArea = (TextView) view.findViewById(R.id.textViewArea);
		textViewMoney = (TextView) view.findViewById(R.id.textViewMoney);
		textViewHouse = (TextView) view.findViewById(R.id.textViewHouse);
		textViewType = (TextView) view.findViewById(R.id.textViewType);
		myListViewBuilding = (MyListView) view
				.findViewById(R.id.myListViewBuilding);

		imageViewBack.setOnClickListener(this);
		editTextSearch.setOnClickListener(this);
		textViewArea.setOnClickListener(this);
		textViewMoney.setOnClickListener(this);
		textViewHouse.setOnClickListener(this);
		textViewType.setOnClickListener(this);

		mPullRefreshScrollView.setMode(Mode.BOTH);
		Bundle bundle = getArguments();
		if (bundle != null) {
			imageViewBack.setVisibility(View.VISIBLE);
			int contentHeight = DensityUtils.getScreenH(getActivity())
					- DensityUtils.dip2px(getActivity(), 105);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, contentHeight);
			mPullRefreshScrollView.setLayoutParams(lp);
		} else {
			int contentHeight = DensityUtils.getScreenH(getActivity())
					- DensityUtils.dip2px(getActivity(), 155);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, contentHeight);
			mPullRefreshScrollView.setLayoutParams(lp);
		}
		initAreaData();
		initMoneyData();
		initHouseData();
		initTypeData();
	}

	@Override
	public void onResume() {
		locatedCityName = MyApplication.city;
		super.onResume();
	}

	@Override
	public void onDestroy() {
		if (mypopuwindowArea != null && mypopuwindowArea.isShowing()) {
			mypopuwindowArea.dismiss();
		}
		if (myPopuwindowMoney != null && myPopuwindowMoney.isShowing()) {
			myPopuwindowMoney.dismiss();
		}
		if (myPopuwindowHouse != null && myPopuwindowHouse.isShowing()) {
			myPopuwindowHouse.dismiss();
		}
		if (myPopuwindowType != null && myPopuwindowType.isShowing()) {
			myPopuwindowType.dismiss();
		}
		super.onDestroy();
	}

	private void initTypeData() {
		String url = Constants.GET_CONDITION_SELECTION_CLASSIFI;
		Map<String, String> map = new HashMap<String, String>();
		map.put("typeGuid", Constants.CONDITION_SELECTION_CLASSIFI_ID_TYPE);
		map.put("Token", AESUtils.encode("typeGuid").replaceAll("\n", ""));
		GetConditionSelectionClassifi getConditionSelectionClassifi = new GetConditionSelectionClassifi(
				getActivity(), url, map);
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
						myPopuwindowType = new MyPopuwindow(context, maps,
								"type");
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
				getActivity(), url, map);
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
						myPopuwindowHouse = new MyPopuwindow(context, maps,
								"house");
					}
				});
	}

	private void initMoneyData() {
		String url = Constants.GET_CONDITION_SELECTION_CLASSIFI;
		Map<String, String> map = new HashMap<String, String>();
		map.put("typeGuid",
				Constants.CONDITION_SELECTION_CLASSIFI_ID_TOTAL_MONEY);
		map.put("Token", AESUtils.encode("typeGuid").replaceAll("\n", ""));
		GetConditionSelectionClassifi getConditionSelectionClassifi = new GetConditionSelectionClassifi(
				getActivity(), url, map);
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
						myPopuwindowMoney = new MyPopuwindow(context, maps,
								"money");
					}
				});
	}

	private void initAreaData() {
		String url = Constants.GET_PROVINCE_CITY;
		Map<String, String> map = new HashMap<String, String>();
		map.put("Token", AESUtils.encode("Token").replaceAll("\n", ""));
		GetProvinceCity getProvinceCity = new GetProvinceCity(getActivity(),
				url, map);
		getProvinceCity
				.setOnGetDataSuccessListener(new GetDataSuccessListener() {
					@Override
					public void onGetDataSuccess(String tag, Object obj) {
						List<Province> provinces = (List<Province>) obj;
						mypopuwindowArea = new MypopuwindowThree(context,
								provinces);
					}
				});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageViewBack:
			getActivity().finish();
			break;

		case R.id.editTextSearch:
			Tools.startActivity(getActivity(), SearchActivity.class);
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

		case R.id.textViewMoney:
			if (myPopuwindowMoney == null)
				return;

			if (!myPopuwindowMoney.isShowing()) {
				myPopuwindowMoney.showAsDropDown(textViewArea, 0, 0);
				myPopuwindowMoney.setSetOnAddOrSubtractListener(this);
			} else {
				myPopuwindowMoney.dismiss();
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

		case R.id.textViewType:
			if (myPopuwindowType == null)
				return;
			if (!myPopuwindowType.isShowing()) {
				myPopuwindowType.showAsDropDown(textViewArea, 0, 0);
				myPopuwindowType.setSetOnAddOrSubtractListener(this);
			} else {
				myPopuwindowType.dismiss();
			}
			break;
		}

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
		if ("money".equals(type)) {
			locatedCityName = "";
			money = id;
			textViewMoney.setText(name);
		}
		if ("house".equals(type)) {
			locatedCityName = "";
			house = id;
			textViewHouse.setText(name);
		}

		if ("type".equals(type)) {
			locatedCityName = "";
			this.type = id;
			textViewType.setText(name);
		}

		pageIndex = 1;
		initData();
	}

}
