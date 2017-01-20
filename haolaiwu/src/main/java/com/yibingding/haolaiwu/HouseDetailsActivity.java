package com.yibingding.haolaiwu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ybd.app.BaseActivity;
import com.ybd.app.interf.GetDataSuccessListener;
import com.ybd.app.tools.PreferenceHelper;
import com.ybd.app.tools.Tools;
import com.yibingding.haolaiwu.activity.BuildingInfoActivity;
import com.yibingding.haolaiwu.activity.NavigationActivity;
import com.yibingding.haolaiwu.dialog.Dialognetwork;
import com.yibingding.haolaiwu.domian.HouseRentItem;
import com.yibingding.haolaiwu.internet.GetHouseDetails;
import com.yibingding.haolaiwu.internet.JustDoSth;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.BannerEvent;
import com.yibingding.haolaiwu.tools.BannerUtils;
import com.yibingding.haolaiwu.tools.Constants;

public class HouseDetailsActivity extends BaseActivity implements
		OnClickListener, GetDataSuccessListener {

	private TextView textViewTitle1, textViewBottomLeft, textViewBottomRight,
			textViewTitle2, textViewTip_old, textViewUpDateTime_Rent,
			textViewYouhui, textViewMoney_old, textViewMoney_rent,
			textViewRentMethord_rent, textViewHouseStyle, textViewHouseArea,
			textView_HouseArea, textViewDecorate, textViewTowards,
			textViewFloor, textViewTime_old, textViewProperty_old,
			textViewSupportingFacilities_rent, textViewPublishTime_old,
			textViewVillageName, textViewRegion, textViewHouseInstruction,
			textViewHouseAddress, textViewHouseAroundSet, tv_weizhi,
			tv_zhibian;
	private RelativeLayout rlMoney_rent, rlRentMethord_rent, rlTime_old,
			rlProperty_old, rlSupportingFacilities_rent, rlPublishTime_old,
			rl_HouseArea;
	private ImageView imageViewCollect;
	private ViewPager viewpager;
	private LinearLayout indicator, ll_ScrollViewdown;
	private View view_weizhi;

	private String whereFrom;
	private String id;
	private HouseRentItem houseRentItem;
	private String userId;

	private String isCollected = "";
	private String collectionGuid = "";
	private Dialognetwork dialog;
	private String latlon;

	public void back(View view) {
		finish();
	}

	@Override
	public void onCreateThisActivity() {
		setContentView(R.layout.activity_house_details);
		Intent intent = getIntent();
		whereFrom = intent.getStringExtra("whereFrom");
		id = intent.getStringExtra("id");
		// houseRentItem = (HouseRentItem) intent.getSerializableExtra("item");
		userId = PreferenceHelper.readString(this, "userinfo", "guid", "");
	}

	@Override
	public void initViews() {
		view_weizhi = (View) this.findViewById(R.id.view_weizhi);
		tv_zhibian = (TextView) this.findViewById(R.id.tv_zhibian);
		tv_weizhi = (TextView) this.findViewById(R.id.tv_weizhi);
		textViewTitle1 = (TextView) this.findViewById(R.id.textViewTitle1);
		textViewTitle2 = (TextView) this.findViewById(R.id.textViewTitle2);
		textViewBottomLeft = (TextView) this
				.findViewById(R.id.textViewBottomLeft);
		textViewBottomRight = (TextView) this
				.findViewById(R.id.textViewBottomRight);
		RelativeLayout rt_bottom = (RelativeLayout) this
				.findViewById(R.id.rt_bottom);
		LinearLayout bottom = (LinearLayout) this.findViewById(R.id.bottom);
		// 0：普通用户，1：楼盘主管，2：销售顾问，3：团队经理
		String tStyle = PreferenceHelper.readString(this, "userinfo", "tStyle",
				"");
		System.out.println("====tStyle====" + tStyle);
		if (TextUtils.equals("1", tStyle) || TextUtils.equals("2", tStyle)) {
			rt_bottom.removeView(bottom);
		}
		textViewTip_old = (TextView) this.findViewById(R.id.textViewTip_old);
		textViewUpDateTime_Rent = (TextView) this
				.findViewById(R.id.textViewUpDateTime_Rent);
		textViewYouhui = (TextView) this.findViewById(R.id.textViewYouhui);
		textViewMoney_old = (TextView) this
				.findViewById(R.id.textViewMoney_old);
		textViewMoney_rent = (TextView) this
				.findViewById(R.id.textViewMoney_rent);
		textViewRentMethord_rent = (TextView) this
				.findViewById(R.id.textViewRentMethord_rent);
		textViewHouseStyle = (TextView) this
				.findViewById(R.id.textViewHouseStyle);
		textViewHouseArea = (TextView) this
				.findViewById(R.id.textViewHouseArea);
		textView_HouseArea = (TextView) this
				.findViewById(R.id.textView_HouseArea);
		textViewDecorate = (TextView) this.findViewById(R.id.textViewDecorate);
		textViewTowards = (TextView) this.findViewById(R.id.textViewTowards);
		textViewFloor = (TextView) this.findViewById(R.id.textViewFloor);
		textViewTime_old = (TextView) this.findViewById(R.id.textViewTime_old);
		textViewProperty_old = (TextView) this
				.findViewById(R.id.textViewProperty_old);
		textViewSupportingFacilities_rent = (TextView) this
				.findViewById(R.id.textViewSupportingFacilities_old);
		textViewPublishTime_old = (TextView) this
				.findViewById(R.id.textViewPublishTime_old);
		textViewVillageName = (TextView) this
				.findViewById(R.id.textViewVillageName);
		textViewRegion = (TextView) this.findViewById(R.id.textViewRegion);
		textViewHouseInstruction = (TextView) this
				.findViewById(R.id.textViewHouseInstruction);
		textViewHouseAddress = (TextView) this
				.findViewById(R.id.textViewHouseAddress);
		textViewHouseAroundSet = (TextView) this
				.findViewById(R.id.textViewHouseAroundSet);
		rl_HouseArea = (RelativeLayout) this.findViewById(R.id.rl_HouseArea);
		rlMoney_rent = (RelativeLayout) this.findViewById(R.id.rlMoney_rent);
		rlRentMethord_rent = (RelativeLayout) this
				.findViewById(R.id.rlRentMethord_rent);
		rlTime_old = (RelativeLayout) this.findViewById(R.id.rlTime_old);
		rlProperty_old = (RelativeLayout) this
				.findViewById(R.id.rlProperty_old);
		rlSupportingFacilities_rent = (RelativeLayout) this
				.findViewById(R.id.rlSupportingFacilities_old);
		rlPublishTime_old = (RelativeLayout) this
				.findViewById(R.id.rlPublishTime_old);
		imageViewCollect = (ImageView) this.findViewById(R.id.imageViewCollect);
		viewpager = (ViewPager) this.findViewById(R.id.viewpager);
		indicator = (LinearLayout) this.findViewById(R.id.indicator);
		ll_ScrollViewdown = (LinearLayout) this
				.findViewById(R.id.ll_ScrollViewdown);
		if (Constants.HOUSE_RENT.equals(whereFrom)) {
			textViewTip_old.setVisibility(View.GONE);
			textViewMoney_old.setVisibility(View.GONE);
			rlTime_old.setVisibility(View.GONE);
			rlProperty_old.setVisibility(View.GONE);
			rlPublishTime_old.setVisibility(View.GONE);
			textViewBottomLeft.setText("推荐租房");
			textViewBottomRight.setText("我要租房");
		} else if (Constants.OLD_HOUSE.equals(whereFrom)) {
			textViewUpDateTime_Rent.setVisibility(View.GONE);
			rlMoney_rent.setVisibility(View.GONE);
			rlRentMethord_rent.setVisibility(View.GONE);
			rlSupportingFacilities_rent.setVisibility(View.GONE);
			textViewBottomLeft.setText("推荐购房");
			textViewBottomRight.setText("我要买房");
		}
		textViewBottomLeft.setOnClickListener(this);
		textViewBottomRight.setOnClickListener(this);
		imageViewCollect.setOnClickListener(this);
		textViewHouseAddress.setOnClickListener(this);
		textViewRegion.setOnClickListener(this);
	}

	private void show() {
		if (dialog == null) {
			dialog = new Dialognetwork(this);
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

	@Override
	public void initData() {
		show();
		if (Constants.HOUSE_RENT.equals(whereFrom)) {
			String url = Constants.GET_RENT_HOUSE_DETAILS;
			Map<String, String> map = new HashMap<String, String>();
			map.put("Guid", id);
			map.put("userGuid", userId);
			map.put("Token", AESUtils.encode("Guid").replaceAll("\n", ""));
			GetHouseDetails getHouseDetails = new GetHouseDetails(this, url,
					map);
			getHouseDetails.setOnGetDataSuccessListener(this);
		} else if (Constants.OLD_HOUSE.equals(whereFrom)) {
			String url = Constants.GET_OLD_HOUSE_DETAILS;
			Map<String, String> map = new HashMap<String, String>();
			map.put("Guid", id);
			map.put("userGuid", userId);
			map.put("Token", AESUtils.encode("Guid").replaceAll("\n", ""));
			GetHouseDetails getHouseDetails = new GetHouseDetails(this, url,
					map);
			getHouseDetails.setOnGetDataSuccessListener(this);
		}
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textViewRegion:
			if (latlon != null) {
				startActivity(new Intent(this, NavigationActivity.class)
						.putExtra("latlon", latlon));
				// overridePendingTransition(R.anim.abc_fade_in,
				// R.anim.abc_fade_out);
			}
			break;
		case R.id.textViewHouseAddress:
			if (latlon != null) {
				startActivity(new Intent(this, NavigationActivity.class)
						.putExtra("latlon", latlon));
				// overridePendingTransition(R.anim.abc_fade_in,
				// R.anim.abc_fade_out);
			}
			break;

		case R.id.textViewBottomLeft:
			if (TextUtils.isEmpty(PreferenceHelper.readString(this, "userinfo",
					"guid", ""))) {
				// Tools.showToast(this, "用户未登录！");
				startActivity(new Intent(this, User_LoginActivity.class));
			} else {
				Intent intent = new Intent(this, HouseRecommendActivity.class);
				if (Constants.HOUSE_RENT.equals(whereFrom)) {
					intent.putExtra("whereFrom", Constants.RENT_RECOMMEND);
				} else {
					intent.putExtra("whereFrom", Constants.OLD_RECOMMEND);
				}
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
				Intent intent = new Intent(this, HouseRecommendActivity.class);
				if (Constants.HOUSE_RENT.equals(whereFrom)) {
					intent.putExtra("whereFrom", Constants.RENT_WANT);
				} else {
					intent.putExtra("whereFrom", Constants.OLD_WANT);
				}
				intent.putExtra("item", houseRentItem);
				startActivity(intent);
			}

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
				map.put("remark", Constants.OLD_HOUSE.equals(whereFrom) ? "二手房"
						: "房屋租赁");
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
									Tools.showToast(HouseDetailsActivity.this,
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
								Tools.showToast(HouseDetailsActivity.this,
										"取消成功!");
							}
						});
			}

			break;
		}

	}

	@Override
	public void onGetDataSuccess(String tag, Object obj) {
		dismiss();
		String jsonResult = (String) obj;
		try {
			JSONObject jsonObject = new JSONArray(jsonResult).getJSONObject(0)
					.getJSONArray("result").getJSONObject(0);

			if (Constants.GET_RENT_HOUSE_DETAILS.equals(tag)) {
				textViewTitle1.setText(jsonObject
						.optString("t_RentalHouse_Title"));
				textViewTitle2.setText(jsonObject
						.optString("t_RentalHouse_Title"));
				textViewUpDateTime_Rent.setText("更新时间："
						+ jsonObject.optString("t_AddDate"));
				textViewYouhui.setText(jsonObject.optString("Sale"));
				textViewMoney_rent.setText(jsonObject
						.optString("t_RentalHouse_Price"));
				textViewRentMethord_rent.setText(jsonObject
						.optString("RentalType"));
				textViewHouseStyle.setText(jsonObject.optString("Model"));
				ll_ScrollViewdown.removeView(rl_HouseArea);// 隐藏建筑面积
				ll_ScrollViewdown.removeView(view_weizhi);
				ll_ScrollViewdown.removeView(tv_weizhi);
				ll_ScrollViewdown.removeView(textViewHouseAddress);
				ll_ScrollViewdown.removeView(tv_zhibian);
				ll_ScrollViewdown.removeView(textViewHouseAroundSet);
				// textViewHouseArea.setText(jsonObject
				// .optString("t_Community_Area"));
				textViewDecorate.setText(jsonObject.optString("Decorate"));
				textViewTowards.setText(jsonObject
						.optString("t_RentalHouse_Toward"));
				textViewFloor.setText(jsonObject
						.optString("t_RentalHouse_Floor"));
				textViewVillageName.setText(jsonObject
						.optString("t_Community_Name"));
				textViewRegion.setText(jsonObject.optString("ProvinceName")
						+ jsonObject.optString("CityName")
						+ jsonObject.optString("DistrictName")
						+ jsonObject.optString("t_Community_Street"));
				textViewHouseInstruction.setText(jsonObject.optString(
						"t_RentalHouse_Instruction").replaceAll("===", "\n"));
				textViewHouseAddress.setText("地址："
						+ jsonObject.optString("t_Community_Street"));
				textViewSupportingFacilities_rent.setText(jsonObject
						.optString("Set"));
				textViewHouseAroundSet.setText("生活："
						+ jsonObject.optString("t_Community_Street") + "\n\n"
						+ "教育：" + jsonObject.optString("t_Community_SchoolSet")
						+ "\n\n" + "交通："
						+ jsonObject.optString("t_Community_TrafficSet")
						+ "\n\n" + "医疗："
						+ jsonObject.optString("t_Community_MedicalSet"));

				// double optDouble =
				// jsonObject.optDouble("t_RentalHouse_MoneyPlot");
				((TextView) findViewById(R.id.textViewMoney_commision))
						.setText(jsonObject
								.getString("t_RentalHouse_MoneyPlot"));

				List<String> pics = new ArrayList<String>();
				JSONArray array_pic = jsonObject.getJSONArray("Pic");
				for (int i = 0; i < array_pic.length(); i++) {
					pics.add(array_pic.getJSONObject(i).optString("t_Pic_Url"));
				}
				new BannerUtils(this, bannerEvent, pics, viewpager, indicator,
						true);

				String longitude = jsonObject
						.optString("t_RentalHouse_Longitude");
				String latitude = jsonObject
						.optString("t_RentalHouse_Latitude");
				latlon = latitude + "#" + longitude;

				houseRentItem = new HouseRentItem(jsonObject.optString("Guid"),
						pics.get(0),
						jsonObject.optString("t_RentalHouse_Title"),
						jsonObject.optString("PriceStyle"),
						jsonObject.optString("Model"),
						jsonObject.optString("t_RentalHouse_Price"),
						jsonObject.optString("t_Community_Street"), "", "", "",
						"", "");
			}

			if (Constants.GET_OLD_HOUSE_DETAILS.equals(tag)) {
				System.out.println(jsonObject);
				textViewTitle1.setText(jsonObject.optString("t_SecHouse_Name"));
				textViewTitle2.setText(jsonObject.optString("t_SecHouse_Name"));
				textViewTip_old.setText(jsonObject.optString("Tip"));
				textViewYouhui.setText(jsonObject.optString("Sale"));
				textViewMoney_old.setText(jsonObject
						.optString("t_SecHouse_TotlePrice"));
				textViewHouseStyle.setText(jsonObject.optString("Model"));
				textViewHouseArea.setText(jsonObject
						.optString("t_SecHouse_Area"));
				textViewDecorate.setText(jsonObject.optString("Decorate"));
				textViewTowards.setText(jsonObject
						.optString("t_SecHouse_Toward"));
				textViewFloor.setText(jsonObject.optString("t_SecHouse_Floor"));
				textViewTime_old.setText(jsonObject
						.optString("t_SecHouse_Year"));
				textViewProperty_old.setText(jsonObject.optString("Rights"));
				textViewPublishTime_old.setText(jsonObject
						.optString("t_AddDate"));
				textViewVillageName.setText(jsonObject
						.optString("t_Community_Name"));
				textViewRegion.setText(jsonObject.optString("ProvinceName")
						+ jsonObject.optString("CityName")
						+ jsonObject.optString("DistrictName")
						+ jsonObject.optString("t_Community_Street"));
				textViewHouseInstruction.setText(jsonObject.optString(
						"t_SecHouse_Instruction").replaceAll("===", "\n"));
				textViewHouseAddress.setText("地址："
						+ jsonObject.optString("t_Community_Street"));
				textViewHouseAroundSet.setText("生活："
						+ jsonObject.optString("t_Community_Street") + "\n\n"
						+ "教育：" + jsonObject.optString("t_Community_SchoolSet")
						+ "\n\n" + "交通："
						+ jsonObject.optString("t_Community_TrafficSet")
						+ "\n\n" + "医疗："
						+ jsonObject.optString("t_Community_MedicalSet"));
				// textViewMoney_commision
				// double optDouble =
				// jsonObject.optDouble("t_SecHouse_MoneyPlot");
				((TextView) findViewById(R.id.textViewMoney_commision))
						.setText(jsonObject.getString("t_SecHouse_MoneyPlot"));

				List<String> pics = new ArrayList<String>();
				JSONArray array_pic = jsonObject.getJSONArray("Pic");
				for (int i = 0; i < array_pic.length(); i++) {
					pics.add(array_pic.getJSONObject(i).optString("t_Pic_Url"));
				}
				new BannerUtils(this, bannerEvent, pics, viewpager, indicator,
						true);
				String longitude = jsonObject.optString("t_SecHouse_Longitude");
				String latitude = jsonObject.optString("t_SecHouse_Latitude");
				latlon = latitude + "#" + longitude;
				houseRentItem = new HouseRentItem(jsonObject.optString("Guid"),
						pics.get(0), jsonObject.optString("t_SecHouse_Name"),
						jsonObject.optString("t_SecHouse_Area"),
						jsonObject.optString("Model"),
						jsonObject.optString("t_SecHouse_TotlePrice"),
						jsonObject.optString("t_Community_Street"), "", "", "",
						"", "");
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	BannerEvent bannerEvent = new BannerEvent() {
		@Override
		public <T> void disPlayBannerImages(List<T> list, int position,
				ImageView iv) {
			String pic = (String) list.get(position);
			ImageLoader.getInstance().displayImage(Constants.IMAGE_URL + pic,
					iv);
		}

		@Override
		public <T> void clickPic(List<T> list, int position, ImageView iv) {
			// TODO Auto-generated method stub
		}
	};

}
