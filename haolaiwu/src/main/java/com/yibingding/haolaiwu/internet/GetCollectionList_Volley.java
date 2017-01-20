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
import com.yibingding.haolaiwu.domian.CommentBean;
import com.yibingding.haolaiwu.domian.CommentReplyBean;
import com.yibingding.haolaiwu.domian.MyAttentionBean;
import com.yibingding.haolaiwu.domian.NewBean;

/*获取个人关注收藏列表
 */

public class GetCollectionList_Volley extends VolleyPost {
	private String whereFrom;
	private List<MyAttentionBean> arrayList;

	public GetCollectionList_Volley(Context context, String url,
			Map<String, String> map) {
		super(context, url, map);
	}

	public GetCollectionList_Volley(Context context, String url,
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
					arrayList = new ArrayList<MyAttentionBean>();
					for (int i = 0; i < jsonContentArray.length(); i++) {
						MyAttentionBean bean = new MyAttentionBean();
						JSONObject contentJsonObject = jsonContentArray
								.getJSONObject(i);
						bean.setGuid(contentJsonObject.getString("Guid"));
						bean.setNames(contentJsonObject.getString("names"));
						bean.setStyle(contentJsonObject.getString("style"));
						bean.setPic(contentJsonObject.getString("pic"));
						bean.setAssoctateGuid(contentJsonObject
								.getString("assoctateGuid"));
						bean.setT_Collection_Date(contentJsonObject
								.getString("t_Collection_Date"));
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
