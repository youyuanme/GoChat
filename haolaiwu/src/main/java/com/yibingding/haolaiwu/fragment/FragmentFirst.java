package com.yibingding.haolaiwu.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ybd.app.BaseFragment;
import com.ybd.app.interf.GetDataSuccessListener;
import com.ybd.app.tools.Tools;
import com.yibingding.haolaiwu.BuildingActivity;
import com.yibingding.haolaiwu.CaiYouLikeActivity;
import com.yibingding.haolaiwu.HouseRentActivity;
import com.yibingding.haolaiwu.MainActivity;
import com.yibingding.haolaiwu.OldHouseActivity;
import com.yibingding.haolaiwu.R;
import com.yibingding.haolaiwu.SearchActivity;
import com.yibingding.haolaiwu.activity.BuildingCommendActivity;
import com.yibingding.haolaiwu.activity.GetLocationActivity;
import com.yibingding.haolaiwu.adapter.HouseRentAdapter;
import com.yibingding.haolaiwu.adapter.My_NEW_Adapter;
import com.yibingding.haolaiwu.adapter.page.AdPageAdapter;
import com.yibingding.haolaiwu.dialog.Dialognetwork;
import com.yibingding.haolaiwu.domian.AdInfo;
import com.yibingding.haolaiwu.domian.HouseRentItem;
import com.yibingding.haolaiwu.domian.NewBean;
import com.yibingding.haolaiwu.houseparopertynew.HousePropertyNewDetailsActivity;
import com.yibingding.haolaiwu.internet.GetBuildingList;
import com.yibingding.haolaiwu.internet.GetNews_Volley;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.Constants;
import com.yibingding.haolaiwu.tools.MyApplication;
import com.yibingding.haolaiwu.tools.MyUtils;
import com.yibingding.haolaiwu.weight.HomeRollViewPager;
import com.yibingding.haolaiwu.weight.NoScrollListView;

/*首页*/
public class FragmentFirst extends BaseFragment implements OnClickListener {

	private Context ctx;
	private List<NewBean> list_new;
	private My_NEW_Adapter<NewBean> mAdapter;
	private String lastCityname = "";
	private HomeRollViewPager pager_homepage_top;
	private LinearLayout ll_points;

	@Override
	public void initViews(View view) {
		lastCityname = MyApplication.city;
		tv_city = (TextView) view.findViewById(R.id.tv_city);
		tv_city.setOnClickListener(this);
		RelativeLayout rl_search = (RelativeLayout) view
				.findViewById(R.id.rl_search);
		rl_search.setOnClickListener(this);
		setWid(rl_search);
		pager_homepage_top = (HomeRollViewPager) view
				.findViewById(R.id.pager_homepage_top);
		ll_points = (LinearLayout) view.findViewById(R.id.ll_points);
		lv_commbuild = (NoScrollListView) view.findViewById(R.id.lv_commbuild);
		lv_commnews = (NoScrollListView) view.findViewById(R.id.lv_commnews);
		lv_commbuild.setFocusable(false);
		lv_commnews.setFocusable(false);
		lv_commnews.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(ctx,
						HousePropertyNewDetailsActivity.class);
				intent.putExtra("Guid", list_new.get(position).getGuid());
				startActivity(intent);
			}
		});
		buildResult = new BuildResult();

		LinearLayout ll_commbuildings = (LinearLayout) view
				.findViewById(R.id.ll_commbuildings);
		ll_commbuildings.setOnClickListener(this);
		LinearLayout ll_buildnews = (LinearLayout) view
				.findViewById(R.id.ll_buildnews);
		ll_buildnews.setOnClickListener(this);

		TextView textViewBuilding = (TextView) view
				.findViewById(R.id.textViewBuilding);
		TextView textViewHouseRent = (TextView) view
				.findViewById(R.id.textViewHouseRent);
		TextView textViewOldHouse = (TextView) view
				.findViewById(R.id.textViewOldHouse);
		textViewBuilding.setOnClickListener(this);
		textViewHouseRent.setOnClickListener(this);
		textViewOldHouse.setOnClickListener(this);
		if (TextUtils.isEmpty(MyApplication.city)) {
			for (int i = 0; i < MyApplication.application.list_activities
					.size(); i++) {
				Activity activity = MyApplication.application.list_activities
						.get(i);
				if (activity instanceof GetLocationActivity) {
					return;
				}
			}
			tv_city.setText("城市");
			startActivityForResult(new Intent(ctx, GetLocationActivity.class),
					1);
			return;
		} else {
			tv_city.setText(MyApplication.city);
		}
	}

	private void show() {
		if (dialog == null) {
			dialog = new Dialognetwork(ctx);
		}
		if (!dialog.isShowing()) {
			dialog.show();
		}
	}

	private void dismiss() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	private void setWid(RelativeLayout rl_search) {
		RelativeLayout.LayoutParams layoutParams = (android.widget.RelativeLayout.LayoutParams) rl_search
				.getLayoutParams();
		layoutParams.width = (int) (MyUtils.getWindowWidth(ctx) * 0.58);
		rl_search.setLayoutParams(layoutParams);
	}

	@Override
	public void onResume() {
		super.onResume();
		// if (!lastCityname.equals(MyApplication.city)) {
		// // 重新联网
		// show();
		// tv_city.setText(MyApplication.city);
		// // getData(buildResult);
		// lastCityname = MyApplication.city;
		// }
		// getData(buildResult);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == getActivity().RESULT_OK) {
			if (requestCode == 1) {
				tv_city.setText(MyApplication.city);
				getData(buildResult);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void getData(GetDataSuccessListener getDataSuccessListener) {
		String url = Constants.COMMANDBUILDING_URL;
		Map<String, String> map = new HashMap<String, String>();
		map.put("top", "5");
		map.put("cityName", MyApplication.city);
		map.put("Token", AESUtils.encode("top").replaceAll("\n", ""));
		System.out.println("====楼盘推荐=====map.toString()====" + map.toString());
		GetBuildingList getBuildingList = new GetBuildingList(ctx, url, map);
		getBuildingList.setOnGetDataSuccessListener(getDataSuccessListener);

		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("top", "10");
		map2.put("wordCount", MyApplication.wordCount);
		map2.put("token", AESUtils.encode("top").replaceAll("\n", ""));
		System.out.println("==猜你喜欢==map2.toString()====" + map2.toString());
		GetNews_Volley list_Volley = new GetNews_Volley(getActivity(),
				Constants.COMMANDNEWS_URL, map2);
		list_Volley.setOnGetDataSuccessListener(new GetDataSuccessListener() {
			@Override
			public void onGetDataSuccess(String tag, Object obj) {
				dismiss();
				if (obj != null) {
					list_new = (List<NewBean>) obj;
					mAdapter = new My_NEW_Adapter<NewBean>(getActivity(),
							list_new, null);
					lv_commnews.setAdapter(mAdapter);
				}
			}
		});
	}

	@Override
	public void initData() {
		show();
		getAds();
		getData(buildResult);
	}

	class BuildResult implements GetDataSuccessListener {
		@Override
		public void onGetDataSuccess(String tag, Object obj) {
			dismiss();
			setDataToView(tag, obj);
		}
	}

	private void getAds() {
		RequestQueue mRequestQueue = Volley.newRequestQueue(ctx);
		System.out.println(AESUtils.encode("Token"));
		StringRequest result = new StringRequest(Constants.HOMEADS_URL
				+ AESUtils.encode("Token"), new Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONArray dataArr = new JSONArray(response)
							.getJSONObject(0).getJSONArray("result");
					// Attach
					List<AdInfo> ads = getAds(dataArr);
					pager_homepage_top.initPointList(ads.size(), ll_points);
					AdPageAdapter adapter = new AdPageAdapter(ctx, ads, true);
					pager_homepage_top.setAdapter(adapter);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			private List<AdInfo> getAds(JSONArray dataArr) throws JSONException {
				List<AdInfo> ads = new ArrayList<AdInfo>();
				for (int i = 0; i < dataArr.length(); i++) {
					JSONObject adJson = dataArr.getJSONObject(i);
					AdInfo adi = new AdInfo();
					adi.guid = adJson.getString("Guid");
					adi.imgUrl = Constants.IMAGE_URL
							+ adJson.getString("t_Ad_Pic");
					adi.url = adJson.getString("t_Ad_Url");
					adi.title = adJson.getString("t_Ad_Title");
					adi.contents = adJson.getString("t_Ad_Contents");
					ads.add(adi);
				}
				return ads;
			}

		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// 网络请求失败
				VolleyLog.e("Error: ", error.getMessage());
			}
		});

		mRequestQueue.add(result);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_city:
			startActivityForResult(new Intent(ctx, GetLocationActivity.class),
					1);
			break;
		case R.id.rl_search:
			startActivity(new Intent(ctx, SearchActivity.class));
			break;
		case R.id.ll_commbuildings:
			startActivity(new Intent(ctx, BuildingCommendActivity.class));
			// startActivity(new Intent(ctx, BuildingInfoActivity.class));
			break;
		case R.id.ll_buildnews:
			// ((MainActivity) ctx).toNewsFragment();
			startActivity(new Intent(ctx, CaiYouLikeActivity.class));
			break;

		case R.id.textViewBuilding:
			startActivity(new Intent(ctx, BuildingActivity.class));
			break;

		case R.id.textViewHouseRent:
			RequestQueue mRequestQueue = Volley.newRequestQueue(ctx);
			StringRequest result = new StringRequest(
					Constants.GET_ON_OFF_RENTAL + AESUtils.encode("Token"),
					new Listener<String>() {
						@Override
						public void onResponse(String response) {
							try {
								String result = new JSONArray(response)
										.getJSONObject(0).getString("result");
								if (TextUtils.equals(result, "0")) {
									startActivity(new Intent(ctx,
											HouseRentActivity.class));
								} else {
									Tools.showToast(ctx, "敬请期待...");
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Tools.showToast(
									ctx,
									ctx.getResources().getString(
											R.string.lianwangshibai));
							VolleyLog.e("Error: ", error.getMessage());
						}
					});
			mRequestQueue.add(result);
			break;

		case R.id.textViewOldHouse:
			RequestQueue mRequestQueue1 = Volley.newRequestQueue(ctx);
			StringRequest result1 = new StringRequest(Constants.GET_ON_OFF_SEC
					+ AESUtils.encode("Token"), new Listener<String>() {
				@Override
				public void onResponse(String response) {
					try {
						String result = new JSONArray(response)
								.getJSONObject(0).getString("result");
						if (TextUtils.equals(result, "0")) {
							startActivity(new Intent(ctx,
									OldHouseActivity.class));
						} else {
							Tools.showToast(ctx, "敬请期待...");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					Tools.showToast(
							ctx,
							ctx.getResources().getString(
									R.string.lianwangshibai));
					VolleyLog.e("Error: ", error.getMessage());
				}
			});
			mRequestQueue1.add(result1);

			break;
		}
	}

	private List<HouseRentItem> houseRentItems;
	private HouseRentAdapter houseRentAdapter;
	private NoScrollListView lv_commbuild;
	private TextView tv_city;
	private NoScrollListView lv_commnews;
	private BuildResult buildResult;
	private Dialognetwork dialog;

	public void setDataToView(String tag, Object obj) {
		houseRentItems = (List<HouseRentItem>) obj;
		houseRentAdapter = new HouseRentAdapter(getActivity(), houseRentItems,
				Constants.BUILDING);
		lv_commbuild.setAdapter(houseRentAdapter);
	}

	@Override
	public View onCreateThisFragment(LayoutInflater inflater,
			ViewGroup container) {
		ctx = getActivity();
		return View.inflate(ctx, R.layout.fragment_first, null);
	}

}
