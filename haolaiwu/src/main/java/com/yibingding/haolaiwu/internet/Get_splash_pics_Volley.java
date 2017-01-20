package com.yibingding.haolaiwu.internet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.ybd.app.tools.Tools;
import com.ybd.app.volley.VolleyPost;
import com.yibingding.haolaiwu.R;
import com.yibingding.haolaiwu.tools.BannerUtils;

/*获取引导页图片url*/

public class Get_splash_pics_Volley extends VolleyPost {
	private String whereFrom;
	private List<String> pics;

	public Get_splash_pics_Volley(Context context, String url,
			Map<String, String> map) {
		super(context, url, map);
	}

	public Get_splash_pics_Volley(Context context, String url,
			Map<String, String> map, String whereFrom) {
		super(context, url, map);
		this.whereFrom = whereFrom;
	}

	@Override
	public void pullJson(String json) {
		if (json != null) {
			try {
				JSONArray jsonArray = new JSONArray(json)
						.getJSONObject(0).getJSONArray("result");
				pics = new ArrayList<String>();
				for (int i = 0; i < jsonArray.length(); i++) {
					pics.add(jsonArray.getJSONObject(i).getString("t_StartPic"));
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			getDataSuccessListener.onGetDataSuccess(whereFrom, pics);
		} else {
			Tools.showToast(context, context.getString(R.string.lianwangshibai));
		}
	}

	@Override
	public String getPageIndex() {
		return null;
	}

}
