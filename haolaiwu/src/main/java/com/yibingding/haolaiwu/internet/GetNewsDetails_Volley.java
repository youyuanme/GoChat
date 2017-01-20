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
import com.yibingding.haolaiwu.domian.NewBean;

/*房产新闻品论
 */

public class GetNewsDetails_Volley extends VolleyPost {
	private String whereFrom;
	private int pageIndex;
	private List<CommentBean> arrayList;

	public GetNewsDetails_Volley(Context context, String url,
			Map<String, String> map) {
		super(context, url, map);
	}

	public GetNewsDetails_Volley(Context context, String url,
			Map<String, String> map, String whereFrom) {
		super(context, url, map);
		this.whereFrom = whereFrom;
	}

	public GetNewsDetails_Volley(Context context, String url,
			Map<String, String> map, String whereFrom, int pageIndex) {
		super(context, url, map);
		this.whereFrom = whereFrom;
		this.pageIndex = pageIndex;
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
					arrayList = new ArrayList<CommentBean>();
					for (int i = 0; i < jsonContentArray.length(); i++) {
						CommentBean bean = new CommentBean();
						JSONObject contentJsonObject = jsonContentArray
								.getJSONObject(i);
						bean.setGuid(contentJsonObject.getString("Guid"));
						bean.setT_Associate_Guid(contentJsonObject
								.getString("t_Associate_Guid"));
						bean.setT_Talk_FromUserGuid(contentJsonObject
								.getString("t_Talk_FromUserGuid"));
						bean.setT_Talk_FromContent(contentJsonObject
								.getString("t_Talk_FromContent"));
						bean.setT_Talk_FromDate(contentJsonObject
								.getString("t_Talk_FromDate"));
						bean.setT_Talk_Good(contentJsonObject
								.getString("t_Talk_Good"));
						bean.setT_Talk_Bad(contentJsonObject
								.getString("t_Talk_Bad"));
						bean.setT_Talk_Audit(contentJsonObject
								.getString("t_Talk_Audit"));
						bean.setT_DelState(contentJsonObject
								.getString("t_DelState"));
						bean.setFromLoginId(contentJsonObject
								.getString("fromLoginId"));
						bean.setFromRealName(contentJsonObject
								.getString("fromRealName"));
						bean.setFromNickName(contentJsonObject
								.getString("fromNickName"));
						bean.setFromPic(contentJsonObject.getString("fromPic"));
						bean.setFromUserStyle(contentJsonObject
								.getString("fromUserStyle"));
						bean.setReplyCount(contentJsonObject
								.getString("replyCount"));
						arrayList.add(bean);
					}
				} else if (pageIndex != 1) {
					String result = jsonObject.getString("result");
					Tools.showToast(context, result);
				} else {
					// Tools.showToast(context, "暂无评论信息!");
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
