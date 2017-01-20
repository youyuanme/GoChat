package com.yibingding.haolaiwu.internet;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.ybd.app.tools.Tools;
import com.ybd.app.volley.VolleyPost;
import com.yibingding.haolaiwu.R;

public class Comment_Volley extends VolleyPost {
	private String whereFrom;
	Map<String, String> map;

	public Comment_Volley(Context context, String url, Map<String, String> map) {
		super(context, url, map);
	}

	public Comment_Volley(Context context, String url, Map<String, String> map,
			String whereFrom) {
		super(context, url, map);
		this.whereFrom = whereFrom;
	}

	@Override
	public void pullJson(String json) {
		if (json != null) {
			try {
				JSONArray array = new JSONArray(json);
				JSONObject jsonObject = array.getJSONObject(0);
				String state = jsonObject.getString("state");
				String result = jsonObject.getString("result");
				map = new HashMap<String, String>();
				map.put("state", state);
				map.put("result", result);
				getDataSuccessListener.onGetDataSuccess(whereFrom, map);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Tools.showToast(context, context.getString(R.string.lianwangshibai));
		}
	}

	@Override
	public String getPageIndex() {
		return null;
	}

}
