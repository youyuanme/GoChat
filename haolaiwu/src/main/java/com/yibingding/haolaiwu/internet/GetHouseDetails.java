package com.yibingding.haolaiwu.internet;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.ybd.app.tools.Tools;
import com.ybd.app.volley.VolleyPost;
import com.yibingding.haolaiwu.R;

public class GetHouseDetails extends VolleyPost {

	public GetHouseDetails(Context context, String url, Map<String, String> map) {
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
					getDataSuccessListener.onGetDataSuccess(url, json);
				} else {
					Tools.showToast(context, jsonObject.getString("result"));
				}

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
