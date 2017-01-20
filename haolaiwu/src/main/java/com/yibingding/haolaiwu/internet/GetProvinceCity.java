package com.yibingding.haolaiwu.internet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.ybd.app.tools.Tools;
import com.ybd.app.volley.VolleyPost;
import com.yibingding.haolaiwu.R;
import com.yibingding.haolaiwu.domian.City;
import com.yibingding.haolaiwu.domian.District;
import com.yibingding.haolaiwu.domian.Province;

public class GetProvinceCity extends VolleyPost {

	private List<Province> provinces = new ArrayList<Province>();

	public GetProvinceCity(Context context, String url, Map<String, String> map) {
		super(context, url, map);
	}

	@Override
	public void pullJson(String json) {
		if (json != null) {
			try {
				JSONArray jsonArray = new JSONArray(json);
				JSONObject jsonObject = jsonArray.getJSONObject(0);
				String state = jsonObject.getString("state");
				if ("true".equals(state)) {
					JSONArray jsonArray2 = jsonObject.getJSONArray("result");
					if (jsonArray2.length() > 0) {
						for (int i = 0; i < jsonArray2.length(); i++) {
							JSONObject obj_province = jsonArray2
									.getJSONObject(i);
							String provinceID = obj_province
									.getString("ProvinceID");
							String provinceName = obj_province
									.getString("ProvinceName");
							if (i == 0) {
								// provinces.add(new Province(provinceID, provinceName));
								continue;
							}
							List<City> cities = new ArrayList<City>();
							JSONArray array_city = obj_province
									.getJSONArray("city");
							if (array_city.length() > 0) {
								for (int j = 0; j < array_city.length(); j++) {
									JSONObject object_city = array_city
											.getJSONObject(j);
									String cityID = object_city
											.getString("CityID");
									String cityName = object_city
											.getString("CityName");
									if (TextUtils.equals("全部", cityName)) {
										// cities.add(new City(cityID, cityName));
										continue;
									}
									List<District> districts = new ArrayList<District>();
									JSONArray array_district = object_city
											.getJSONArray("district");
									if (array_district.length() > 0) {
										for (int k = 0; k < array_district
												.length(); k++) {
											JSONObject obj_district = array_district
													.getJSONObject(k);
											String DistrictID = obj_district
													.getString("DistrictID");
											String DistrictName = obj_district
													.getString("DistrictName");
											District district = new District(
													DistrictID, DistrictName);
											districts.add(district);
										}
									}

									City city = new City(cityID, cityName,
											districts);
									cities.add(city);
								}

							}

							Province province = new Province(provinceID,
									provinceName, cities);
							provinces.add(province);
						}
					}

				}
				getDataSuccessListener.onGetDataSuccess(url, provinces);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Tools.showToast(context, context.getString(R.string.lianwangshibai));
		}

	}

	@Override
	public String getPageIndex() {
		// TODO Auto-generated method stub
		return null;
	}

	// try {
	// JSONArray jsonArray = new JSONArray(json);
	// JSONObject jsonObject = jsonArray.getJSONObject(0);
	// String state = jsonObject.getString("state");
	// if ("true".equals(state)) {
	// JSONArray array = jsonObject.getJSONArray("result");
	// }
	//
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }

}
