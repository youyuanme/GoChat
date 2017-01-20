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
import com.yibingding.haolaiwu.domian.NewBean;

public class GetNewsList extends VolleyPost {

	private List<NewBean> arrayList;

	public GetNewsList(Context context, String url, Map<String, String> map) {
		super(context, url, map);
	}

	@Override
	public void pullJson(String json) {
		if (json != null) {
			try {
				JSONArray array = new JSONArray(json);
				JSONObject jsonObject = array.getJSONObject(0);
				String state = jsonObject.getString("state");
				if (state.equals("true")) {
					arrayList = new ArrayList<NewBean>();
					JSONArray jsonContentArray = jsonObject
							.getJSONArray("result");
					for (int i = 0; i < jsonContentArray.length(); i++) {
						NewBean bean = new NewBean();
						JSONObject contentJsonObject = jsonContentArray
								.getJSONObject(i);
						bean.setGuid(contentJsonObject.getString("Guid"));
						bean.setReplyCount(contentJsonObject
								.getString("replyCount"));
						bean.setT_News_Author(contentJsonObject
								.getString("t_News_Author"));
						bean.setT_News_Contents(contentJsonObject
								.getString("t_News_Contents"));
						bean.setT_News_Counts(contentJsonObject
								.getString("t_News_Counts"));
						bean.setT_News_Date(contentJsonObject
								.getString("t_News_Date"));
						bean.setT_News_Index(contentJsonObject
								.getString("t_News_Index"));
						bean.setT_News_Pic(contentJsonObject
								.getString("t_News_Pic"));
						bean.setT_News_Recommand(contentJsonObject
								.getString("t_News_Recommand"));
						bean.setT_News_Style(contentJsonObject
								.getString("t_News_Style"));
						bean.setT_News_Title(contentJsonObject
								.getString("t_News_Title"));
						bean.setT_Style_Name(contentJsonObject
								.getString("t_Style_Name"));
						arrayList.add(bean);
					}
				} else {
					String result = jsonObject.getString("result");
					// Tools.showToast(context, result);
				}
				getDataSuccessListener.onGetDataSuccess(url, arrayList);

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
