package com.yibingding.haolaiwu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.baidu.mapapi.map.Text;
import com.ybd.app.BaseActivity;
import com.ybd.app.interf.GetDataSuccessListener;
import com.yibingding.haolaiwu.adapter.HouseRentAdapter;
import com.yibingding.haolaiwu.adapter.My_NEW_Adapter;
import com.yibingding.haolaiwu.domian.HouseRentItem;
import com.yibingding.haolaiwu.domian.NewBean;
import com.yibingding.haolaiwu.internet.GetBuildingList;
import com.yibingding.haolaiwu.internet.GetNewsList;
import com.yibingding.haolaiwu.internet.GetOldHouseList;
import com.yibingding.haolaiwu.internet.GetRentHouseList;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.Constants;
import com.yibingding.haolaiwu.tools.MyApplication;

public class SearchActivity extends BaseActivity implements
		OnEditorActionListener, GetDataSuccessListener {

	private Spinner spinnerSearchWhat;
	private EditText editTextSearch;
	private ListView listViewSearchResult;

	private String searchType;
	private final String SEARCH_BUILDING = "SEARCH_BUILDING";
	private final String SEARCH_HOUSE_RENT = "SEARCH_HOUSE_RENT";
	private final String SEARCH_HOUSE_OLD = "SEARCH_HOUSE_OLD";
	private final String SEARCH_NEW = "SEARCH_NEW";

	private String cityName = MyApplication.city;

	public void back(View view) {
		finish();
	}

	@Override
	public void onCreateThisActivity() {
		setContentView(R.layout.activity_search);
	}

	@Override
	public void initViews() {
		spinnerSearchWhat = (Spinner) this.findViewById(R.id.spinnerSearchWhat);
		editTextSearch = (EditText) this.findViewById(R.id.editTextSearch);
		listViewSearchResult = (ListView) this
				.findViewById(R.id.listViewSearchResult);

		editTextSearch.setOnEditorActionListener(this);

		spinnerSearchWhat
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						switch (position) {
						case 0:
							searchType = SEARCH_BUILDING;
							break;
						case 1:
							searchType = SEARCH_HOUSE_RENT;
							break;
						case 2:
							searchType = SEARCH_HOUSE_OLD;
							break;

						case 3:
							searchType = SEARCH_NEW;
							break;
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_SEND
				|| (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

			((InputMethodManager) editTextSearch.getContext().getSystemService(
					Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
					getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);

			String search_content = editTextSearch.getText().toString().trim();
			if (!"".equals(search_content)) {
				// TODO 搜索
				toSearch(search_content);
			}
			return false;
		}
		return false;
	}

	private void toSearch(String search_content) {
		if (SEARCH_BUILDING.equals(searchType)) {// 楼盘
			String url = Constants.GET_BUILDING_LIST;
			Map<String, String> map = new HashMap<String, String>();
			map.put("page", "1");
			map.put("pagesize", "1000");
			map.put("cityName", cityName);
			map.put("province", "");
			map.put("city", "");
			map.put("district", "");
			map.put("totlePrice", "");
			map.put("model", "");
			map.put("buildType", "");
			map.put("key", search_content);
			map.put("Token", AESUtils.encode("page").replaceAll("\n", ""));
			GetBuildingList getBuildingList = new GetBuildingList(this, url,
					map);
			getBuildingList.setOnGetDataSuccessListener(this);
		} else if (SEARCH_HOUSE_RENT.equals(searchType)) {// 租赁
			String url = Constants.GET_RENT_HOUSE_LIST;
			Map<String, String> map = new HashMap<String, String>();
			map.put("page", "1");
			map.put("pagesize", "1000");
			map.put("cityName", cityName);
			map.put("province", "");
			map.put("city", "");
			map.put("district", "");
			map.put("decorate", "");
			map.put("model", "");
			map.put("rentalType", "");
			map.put("key", search_content);
			map.put("Token", AESUtils.encode("page").replaceAll("\n", ""));
			GetRentHouseList getRentHouseList = new GetRentHouseList(this, url,
					map);
			getRentHouseList.setOnGetDataSuccessListener(this);
		} else if (TextUtils.equals(SEARCH_NEW, searchType)) {// 新闻
			String url = Constants.GET_SEL_NEWS;
			Map<String, String> map = new HashMap<String, String>();
			map.put("page", "1");
			map.put("pagesize", "1000");
			map.put("wordCount", "100");
			map.put("key", search_content);
			map.put("Token", AESUtils.encode("page").replaceAll("\n", ""));
			GetNewsList getNewList = new GetNewsList(this, url, map);
			getNewList.setOnGetDataSuccessListener(this);
		} else {
			String url = Constants.GET_OLD_HOUSE_LIST;// 二手房
			Map<String, String> map = new HashMap<String, String>();
			map.put("page", "1");
			map.put("pagesize", "1000");
			map.put("cityName", cityName);
			map.put("province", "");
			map.put("city", "");
			map.put("district", "");
			map.put("decorate", "");
			map.put("model", "");
			map.put("buildType", "");
			map.put("key", search_content);
			map.put("Token", AESUtils.encode("page").replaceAll("\n", ""));
			GetOldHouseList getOldHouseList = new GetOldHouseList(this, url,
					map);
			getOldHouseList.setOnGetDataSuccessListener(this);
		}
	}

	@Override
	public void onGetDataSuccess(String tag, Object obj) {
		if (TextUtils.equals(Constants.GET_SEL_NEWS, tag)) {// 新闻
			List<NewBean> list_new = (List<NewBean>) obj;
			My_NEW_Adapter<NewBean> mAdapter = new My_NEW_Adapter<NewBean>(
					this, list_new, null);
			listViewSearchResult.setAdapter(mAdapter);
			return;
		}
		List<HouseRentItem> houseRentItems = (List<HouseRentItem>) obj;
		if (Constants.GET_BUILDING_LIST.equals(tag)) {// 楼盘
			HouseRentAdapter houseRentAdapter = new HouseRentAdapter(this,
					houseRentItems, Constants.BUILDING);
			listViewSearchResult.setAdapter(houseRentAdapter);
		} else if (Constants.GET_OLD_HOUSE_LIST.equals(tag)) {// 二手房
			HouseRentAdapter houseRentAdapter = new HouseRentAdapter(this,
					houseRentItems, Constants.OLD_HOUSE);
			listViewSearchResult.setAdapter(houseRentAdapter);
		} else {// 房屋租赁
			HouseRentAdapter houseRentAdapter = new HouseRentAdapter(this,
					houseRentItems, Constants.HOUSE_RENT);
			listViewSearchResult.setAdapter(houseRentAdapter);
		}
	}

}
