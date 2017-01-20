package com.yibingding.haolaiwu.internet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.google.gson.Gson;
import com.ybd.app.tools.Tools;
import com.ybd.app.volley.VolleyPost;
import com.yibingding.haolaiwu.R;
import com.yibingding.haolaiwu.domian.BaseDataConditionSelection;
import com.yibingding.haolaiwu.domian.ConditionSelection;

public class GetConditionSelectionClassifi extends VolleyPost {

	private List<ConditionSelection> conditionSelections = new ArrayList<ConditionSelection>();

	public GetConditionSelectionClassifi(Context context, String url,
			Map<String, String> map) {
		super(context, url, map);
	}

	@Override
	public void pullJson(String json) {
		if (json != null) {
			try {
				JSONArray jsonArray = new JSONArray(json);
				JSONObject jsonObject = jsonArray.getJSONObject(0);
				BaseDataConditionSelection baseData = new Gson()
						.fromJson(jsonObject.toString(),
								BaseDataConditionSelection.class);
				if ("true".equals(baseData.getState())) {
					conditionSelections = baseData.getResult();
				}
				getDataSuccessListener.onGetDataSuccess(url,
						conditionSelections);

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
