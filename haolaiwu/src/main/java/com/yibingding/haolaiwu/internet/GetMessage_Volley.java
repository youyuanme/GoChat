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
import com.yibingding.haolaiwu.domian.CommentBean;
import com.yibingding.haolaiwu.domian.CommentReplyBean;
import com.yibingding.haolaiwu.domian.MessageSenterBean;
import com.yibingding.haolaiwu.domian.NewBean;

public class GetMessage_Volley extends VolleyPost {
	private String whereFrom;
	private List<MessageSenterBean> arrayList;

	public GetMessage_Volley(Context context, String url,
			Map<String, String> map) {
		super(context, url, map);
	}

	public GetMessage_Volley(Context context, String url,
			Map<String, String> map, String whereFrom) {
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
				if (state.equals("true")) {
					JSONArray jsonContentArray = jsonObject
							.getJSONArray("result");
					arrayList = new ArrayList<MessageSenterBean>();
					for (int i = 0; i < jsonContentArray.length(); i++) {
						MessageSenterBean bean = new MessageSenterBean();
						JSONObject contentJsonObject = jsonContentArray
								.getJSONObject(i);
						bean.setGuid(contentJsonObject.getString("Guid"));
						bean.setT_Messages_Title(contentJsonObject
								.getString("t_Messages_Title"));
						bean.setT_Messages_Style(contentJsonObject
								.getString("t_Messages_Style"));
						bean.setT_Messages_Contents(contentJsonObject
								.getString("t_Messages_Contents"));
						bean.setT_Messages_Index(contentJsonObject
								.getString("t_Messages_Index"));
						bean.setT_AddDate(contentJsonObject
								.getString("t_AddDate"));
						bean.setT_AddBy(contentJsonObject.getString("t_AddBy"));
						bean.setT_ModifydDate(contentJsonObject
								.getString("t_ModifydDate"));
						bean.setT_ModifyBy(contentJsonObject
								.getString("t_ModifyBy"));
						if ("0".equals(whereFrom)) {
							bean.setIfRead(contentJsonObject
									.getString("ifRead"));
						}
						arrayList.add(bean);
					}
				} else {
					String result = jsonObject.getString("result");
					Tools.showToast(context, result);
				}
				getDataSuccessListener.onGetDataSuccess(whereFrom, arrayList);

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
