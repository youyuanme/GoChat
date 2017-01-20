package com.yibingding.haolaiwu.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ybd.app.BaseActivity;
import com.ybd.app.interf.GetDataSuccessListener;
import com.ybd.app.tools.PreferenceHelper;
import com.ybd.app.tools.Tools;
import com.yibingding.haolaiwu.HouseRecommendActivity;
import com.yibingding.haolaiwu.R;
import com.yibingding.haolaiwu.User_LoginActivity;
import com.yibingding.haolaiwu.adapter.page.AdPageAdapter;
import com.yibingding.haolaiwu.dialog.Dialognetwork;
import com.yibingding.haolaiwu.dialog.User_SimpleDialog;
import com.yibingding.haolaiwu.domian.AdInfo;
import com.yibingding.haolaiwu.domian.HouseRentItem;
import com.yibingding.haolaiwu.internet.GetHouseDetails;
import com.yibingding.haolaiwu.internet.JustDoSth;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.Constants;
import com.yibingding.haolaiwu.weight.HomeRollViewPager;

public class BuildingInfoActivity extends BaseActivity implements
		GetDataSuccessListener, OnClickListener {
	private String userId;
	private String id;
	private String url;
	private TextView tv_propertypay;
	private User_SimpleDialog dialogCallPhone;

	/*
	 * 24489fbd-b356-4d71-a052-f669ca92f512 nGWvZizT0JMrdy5+kxxFPw==
	 */
	@Override
	public void onCreateThisActivity() {
		setContentView(R.layout.activity_buildinginfo);
		userId = PreferenceHelper.readString(this, "userinfo", "guid", "");
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		System.out.println(AESUtils.encode("Guid").replaceAll("\n", ""));
		System.out.println(id);
	}

	@Override
	public void initViews() {
		dialogCallPhone = new User_SimpleDialog(this);
		tv_propertypay = (TextView) findViewById(R.id.tv_propertypay);
		tv_propertypay.setOnClickListener(this);
		viewpager = (HomeRollViewPager) findViewById(R.id.viewpager);
		ll_points = (LinearLayout) findViewById(R.id.ll_points);
		ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		imageViewCollect = (ImageView) this.findViewById(R.id.imageViewCollect);
		imageViewCollect.setOnClickListener(this);
		initTextViews();
		textViews.get(3).setOnClickListener(new OnClickListener() {// 跳转地图页面
					@Override
					public void onClick(View v) {
						startActivity(new Intent(BuildingInfoActivity.this,
								NavigationActivity.class).putExtra("latlon",
								latlon));
						// overridePendingTransition(R.anim.abc_fade_in,
						// R.anim.abc_fade_out);
					}
				});
		TextView textViewBottomLeft = (TextView) findViewById(R.id.textViewBottomLeft);
		TextView textViewBottomRight = (TextView) findViewById(R.id.textViewBottomRight);
		textViewBottomLeft.setOnClickListener(this);
		textViewBottomRight.setOnClickListener(this);
		RelativeLayout rt_bottom = (RelativeLayout) this
				.findViewById(R.id.rt_bottom);
		LinearLayout bottom = (LinearLayout) this.findViewById(R.id.bottom);
		String tStyle = PreferenceHelper.readString(this, "userinfo", "tStyle",
				"");
		if (TextUtils.equals("1", tStyle) || TextUtils.equals("2", tStyle)) {
			rt_bottom.removeView(bottom);
		}
	}

	private void initTextViews() {
		textViews = new ArrayList<TextView>();
		for (int i = 0; i < ids.length; i++) {
			textViews.add((TextView) findViewById(ids[i]));
		}
	}

	@Override
	public void initData() {
		show();
		String url = Constants.HOUSEVIEW_URL;
		Map<String, String> map = new HashMap<String, String>();
		map.put("Guid", id);
		map.put("userGuid", userId);
		map.put("Token", AESUtils.encode("Guid").replaceAll("\n", ""));
		GetHouseDetails getHouseDetails = new GetHouseDetails(this, url, map);
		getHouseDetails.setOnGetDataSuccessListener(this);
	}

	@Override
	protected void onResume() {
		iscollected();
		super.onResume();
	}

	private void iscollected() {
		if (!"".equals(PreferenceHelper
				.readString(this, "userinfo", "guid", ""))) {
			String url = Constants.IS_COLLECTED;
			Map<String, String> map = new HashMap<String, String>();
			map.put("collectionGuid", id);
			map.put("userGuid", userId);
			map.put("Token",
					AESUtils.encode("collectionGuid").replaceAll("\n", ""));
			JustDoSth justDoSth = new JustDoSth(this, url, map);
			justDoSth.setOnGetDataSuccessListener(new GetDataSuccessListener() {
				@Override
				public void onGetDataSuccess(String tag, Object obj) {
					String json = (String) obj;
					try {
						JSONObject jsonObject = new JSONArray(json)
								.getJSONObject(0).getJSONArray("result")
								.getJSONObject(0);
						String Guid = jsonObject.optString("Guid");
						String IfCollection = jsonObject
								.optString("IfCollection");
						if ("否".equals(IfCollection)) {
							imageViewCollect.setImageDrawable(getResources()
									.getDrawable(R.drawable.icon_collected));
						} else {
							imageViewCollect
									.setImageDrawable(getResources()
											.getDrawable(
													R.drawable.icon_collected_not));
							collectionGuid = Guid;
						}
						isCollected = IfCollection;
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			});
		}
	}

	List<TextView> textViews;
	List<String> infos;
	List<AdInfo> ads;

	@Override
	public void onGetDataSuccess(String tag, Object obj) {
		String jsonResult = (String) obj;
		dismiss();
		try {
			JSONObject jsonObject = new JSONArray(jsonResult).getJSONObject(0)
					.getJSONArray("result").getJSONObject(0);
			infos = new ArrayList<String>();
			ads = new ArrayList<AdInfo>();
			JSONArray jsonArray = jsonObject.getJSONArray("Pic");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject2 = jsonArray.getJSONObject(i);
				AdInfo ad = new AdInfo();
				System.out.println("t_Pic_Url::::"
						+ jsonObject2.optString("t_Pic_Url"));
				ad.imgUrl = Constants.IMAGE_URL
						+ jsonObject2.optString("t_Pic_Url");
				ads.add(ad);
			}
			url = jsonArray.getJSONObject(0).getString("t_Pic_Url");
			double optDouble = jsonObject.optDouble("t_Build_MoneyPlot");
			((TextView) findViewById(R.id.tv_commission)).setText(jsonObject
					.getString("t_Build_MoneyPlot"));

			latlon = jsonObject.optString("t_Build_Latitude") + "#"
					+ jsonObject.optString("t_Build_Longitude");
			infos.add(jsonObject.optString("t_Build_Name"));
			infos.add(jsonObject.optString("t_Build_AveragePrice"));
			infos.add(jsonObject.optString("t_Build_StartDate"));
			infos.add(jsonObject.optString("ProvinceName")
					+ jsonObject.optString("CityName")
					+ jsonObject.optString("DistrictName")
					+ jsonObject.optString("t_Build_Street"));

			infos.add(jsonObject.optString("Sale"));
			infos.add(jsonObject.optString("t_Build_HandDate"));
			infos.add(jsonObject.optString("Model"));

			infos.add(jsonObject.optString("BuildType"));
			infos.add(jsonObject.optString("Decorate"));
			infos.add(jsonObject.optString("t_Build_CoverArea"));
			infos.add(jsonObject.optString("t_Build_Area"));
			infos.add(jsonObject.optString("Developer"));

			infos.add(jsonObject.optString("t_Build_Plot"));
			infos.add(jsonObject.optString("t_Build_Green"));
			infos.add(jsonObject.optString("t_Build_Plan"));
			infos.add(jsonObject.optString("t_Build_Park"));
			infos.add(jsonObject.optString("t_Build_ParkRatio"));
			infos.add(jsonObject.optString("t_Build_Equity"));
			infos.add(jsonObject.optString("t_Build_Standard"));
			infos.add(jsonObject.optString("t_Build_PropertyCommany"));
			infos.add(jsonObject.optString("t_Build_PropertyPrice"));

			infos.add(jsonObject.optString("t_Build_LiveSet").replaceAll("===",
					"\n"));
			infos.add(jsonObject.optString("t_Build_SchoolSet").replaceAll(
					"===", "\n"));
			infos.add(jsonObject.optString("t_Build_TrafficSet").replaceAll(
					"===", "\n"));
			infos.add(jsonObject.optString("t_Build_MedicalSet").replaceAll(
					"===", "\n"));
			infos.add(jsonObject.optString("t_Build_Instruction").replaceAll(
					"===", "\n"));
			setData();
			houseRentItem = new HouseRentItem(jsonObject.optString("Guid"),
					url, jsonObject.optString("t_Build_Name"),
					jsonObject.optString("t_Build_Area"),
					jsonObject.optString("Model"),
					jsonObject.optString("t_Build_AveragePrice"),
					jsonObject.optString("t_Build_Street"), "", "", "", "", "");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void show() {
		if (dialog == null) {
			dialog = new Dialognetwork(this);
		}
		dialog.show();
	}

	private void dismiss() {
		if (dialog != null) {
			dialog.dismiss();
		}
	}

	private void setData() {
		for (int i = 0; i < textViews.size(); i++) {
			textViews.get(i).setText(infos.get(i));
		}
		// 设置轮播图
		viewpager.initPointList(ads.size(), ll_points);
		viewpager.setAdapter(new AdPageAdapter(this, ads));
	}

	int[] ids = {
			R.id.tv_title,
			R.id.tv_price,
			R.id.tv_kaipan_time,
			R.id.tv_address,
			R.id.tv_cutprice,
			R.id.tv_getbuildtime,
			R.id.tv_homecate,
			// R.id.tv_hometype,
			R.id.tv_buildtype, R.id.tv_zhuangxiustatus, R.id.tv_size,
			R.id.tv_buildsize, R.id.tv_bussiness, R.id.tv_sizepercent,
			R.id.tv_greenpercent, R.id.tv_count, R.id.tv_carcount,
			R.id.tv_carcountpercent, R.id.tv_houselimit, R.id.tv_zhuangxiurule,
			R.id.tv_propertycompany, R.id.tv_propertypay, R.id.tv_lifething,
			R.id.tv_studentthing, R.id.tv_traficthing, R.id.tv_medicthing,
			R.id.tv_contruct };
	private HomeRollViewPager viewpager;
	private LinearLayout ll_points;
	private String latlon;
	private Dialognetwork dialog;
	private String isCollected = "";
	private String collectionGuid = "";
	private ImageView imageViewCollect;
	private HouseRentItem houseRentItem;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.imageViewCollect:
			if (TextUtils.isEmpty(PreferenceHelper.readString(this, "userinfo",
					"guid", ""))) {
				// Tools.showToast(this, "用户未登录！");
				startActivity(new Intent(this, User_LoginActivity.class));
			} else if ("否".equals(isCollected)) {
				String url = Constants.COLLECT;
				Map<String, String> map = new HashMap<String, String>();
				map.put("collectionGuid", id);
				map.put("remark", Constants.BUILDING);
				map.put("userGuid", userId);
				map.put("Token",
						AESUtils.encode("collectionGuid").replaceAll("\n", ""));
				JustDoSth justDoSth = new JustDoSth(this, url, map);
				justDoSth
						.setOnGetDataSuccessListener(new GetDataSuccessListener() {

							@Override
							public void onGetDataSuccess(String tag, Object obj) {
								String json = (String) obj;
								try {
									JSONObject jsonObject = new JSONArray(json)
											.getJSONObject(0)
											.getJSONArray("result")
											.getJSONObject(0);
									collectionGuid = jsonObject
											.optString("Guid");

									isCollected = "是";
									imageViewCollect
											.setImageDrawable(getResources()
													.getDrawable(
															R.drawable.icon_collected_not));
									Tools.showToast(BuildingInfoActivity.this,
											"收藏成功!");
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						});

			} else if ("是".equals(isCollected)) {
				String url = Constants.UNCOLLECT;
				Map<String, String> map = new HashMap<String, String>();
				map.put("collectionGuid", collectionGuid);
				map.put("Token",
						AESUtils.encode("collectionGuid").replaceAll("\n", ""));
				JustDoSth justDoSth = new JustDoSth(this, url, map);
				justDoSth
						.setOnGetDataSuccessListener(new GetDataSuccessListener() {

							@Override
							public void onGetDataSuccess(String tag, Object obj) {
								isCollected = "否";
								imageViewCollect
										.setImageDrawable(getResources()
												.getDrawable(
														R.drawable.icon_collected));
								Tools.showToast(BuildingInfoActivity.this,
										"取消成功!");
							}
						});
			}

			break;
		case R.id.textViewBottomLeft:
			if ("".equals(PreferenceHelper.readString(this, "userinfo", "guid",
					""))) {
				// Tools.showToast(this, "用户未登录！");
				startActivity(new Intent(this, User_LoginActivity.class));
			} else {
				if (houseRentItem == null) {
					return;
				}
				Intent intent = new Intent(this, HouseRecommendActivity.class);
				intent.putExtra("whereFrom", Constants.BUILDING_RECOMMEND);
				intent.putExtra("item", houseRentItem);
				startActivity(intent);
			}

			break;

		case R.id.textViewBottomRight:
			if ("".equals(PreferenceHelper.readString(this, "userinfo", "guid",
					""))) {
				// Tools.showToast(this, "用户未登录！");
				startActivity(new Intent(this, User_LoginActivity.class));
			} else {
				if (houseRentItem == null) {
					return;
				}
				Intent intent = new Intent(this, HouseRecommendActivity.class);
				intent.putExtra("whereFrom", Constants.BUILDING_WANT_LOOK);
				intent.putExtra("item", houseRentItem);
				startActivity(intent);
			}
			break;
		case R.id.tv_propertypay:
			final String phoneString = tv_propertypay.getText().toString()
					.trim();
			// if (!MyUtils.isPhoneNumber(phoneString)) {
			// Tools.showToast(this, "这不是一个可用的手机号码");
			// break;
			// }
			dialogCallPhone.setTitleText("是否呼叫：" + phoneString);
			dialogCallPhone.setConfirmListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_CALL, Uri
							.parse("tel:" + phoneString));
					startActivity(intent);
					dialogCallPhone.dismiss();
				}
			});
			dialogCallPhone.show();
			break;

		}
	}

	@Override
	protected void onDestroy() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
		super.onDestroy();
	}
}
