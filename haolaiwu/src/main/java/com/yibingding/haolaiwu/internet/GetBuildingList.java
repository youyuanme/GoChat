package com.yibingding.haolaiwu.internet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.ybd.app.tools.Tools;
import com.ybd.app.volley.VolleyPost;
import com.yibingding.haolaiwu.R;
import com.yibingding.haolaiwu.domian.HouseRentItem;

public class GetBuildingList extends VolleyPost {

	private List<HouseRentItem> houseRentItems = new ArrayList<HouseRentItem>();

	public GetBuildingList(Context context, String url, Map<String, String> map) {
		super(context, url, map);
	}

	@Override
	public void pullJson(String json) {
		if (json != null) {
			System.out.println(json);
			try {
				JSONArray jsonArray = new JSONArray(json);
				JSONObject jsonObject = jsonArray.getJSONObject(0);
				String state = jsonObject.getString("state");
				if ("true".equals(state)) {
					JSONArray array = jsonObject.getJSONArray("result");
					for (int i = 0; i < array.length(); i++) {
						JSONObject object_item = array.getJSONObject(i);
						String houseRentItemId = object_item.optString("Guid");
						System.out.println(houseRentItemId);
						String houseRentItemTitle = object_item
								.optString("t_Build_Name");
						String houseRentItemAddress = object_item
								.optString("t_Build_Street");
						String houseRentItemSth = object_item
								.optString("t_Build_HouseArea");
						String houseRentItemMoney = object_item
								.optString("t_Build_AveragePrice");
						String houseRentItemCommission = object_item
								.optString("t_Build_MoneyPlot");
						String houseRentItemPic = object_item
								.getJSONArray("Pic").getJSONObject(0)
								.optString("t_Pic_Url");
						JSONArray ModelArray = object_item
								.getJSONArray("Model");
						String houseRentItemRooms = "";
						if (ModelArray.length() != 0) {
							houseRentItemRooms = ModelArray.getJSONObject(0)
									.optString("ModelName");
						}

						String houseRentItemTip1 = "";
						String houseRentItemTip2 = "";
						String houseRentItemTip3 = "";
						String houseRentItemTip4 = "";
						JSONArray array_tip = object_item.getJSONArray("Tip");
						if (array_tip.length() > 0) {
							for (int j = 0; j < array_tip.length(); j++) {
								JSONObject object_tip = array_tip
										.getJSONObject(j);
								String tipName = object_tip
										.optString("TipName");
								if (j == 0) {
									houseRentItemTip1 = tipName;
								}
								if (j == 1) {
									houseRentItemTip2 = tipName;
								}
								if (j == 2) {
									houseRentItemTip1 = tipName;
								}
								if (j == 3) {
									houseRentItemTip4 = tipName;
								}
							}
						}
						HouseRentItem houseRentItem = new HouseRentItem(
								houseRentItemId, houseRentItemPic,
								houseRentItemTitle, houseRentItemSth,
								houseRentItemRooms, houseRentItemMoney,
								houseRentItemAddress, houseRentItemCommission,
								houseRentItemTip1, houseRentItemTip2,
								houseRentItemTip3, houseRentItemTip4);
						houseRentItems.add(houseRentItem);
					}
				}
				getDataSuccessListener.onGetDataSuccess(url, houseRentItems);
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

}