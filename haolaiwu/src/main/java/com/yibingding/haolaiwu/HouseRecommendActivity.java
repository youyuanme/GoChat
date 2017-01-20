package com.yibingding.haolaiwu;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ybd.app.BaseActivity;
import com.ybd.app.interf.GetDataSuccessListener;
import com.ybd.app.tools.PreferenceHelper;
import com.ybd.app.tools.Tools;
import com.yibingding.haolaiwu.dialog.Dialognetwork;
import com.yibingding.haolaiwu.domian.HouseRentItem;
import com.yibingding.haolaiwu.internet.JustDoSth;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.Constants;
import com.yibingding.haolaiwu.tools.MyApplication;
import com.yibingding.haolaiwu.tools.MyConstant;

public class HouseRecommendActivity extends BaseActivity {

	private ImageView imageViewPic;
	private TextView textViewTitle, textViewSth, textViewRooms, textViewMoney,
			textViewAddress, tv_tijiao;
	private EditText editTextName, editTextTel, editTextRemark;
	private String whereFrom;
	private HouseRentItem houseRentItem;
	private String userId;
	private Dialognetwork dialog;

	public void back(View view) {
		finish();
	}

	@Override
	public void onCreateThisActivity() {
		setContentView(R.layout.activity_house_recommend);
		Intent intent = getIntent();
		whereFrom = intent.getStringExtra("whereFrom");
		houseRentItem = (HouseRentItem) intent.getSerializableExtra("item");
		userId = PreferenceHelper.readString(this, "userinfo", "guid", "");
		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		editTextTel = (EditText) this.findViewById(R.id.editTextTel);
		setTitle(tv_title);
	}

	private void setTitle(TextView tv_title) {
		String mobile = this.getSharedPreferences("userinfo",
				Context.MODE_PRIVATE).getString("mobile", "");
		if (Constants.RENT_RECOMMEND.equals(whereFrom)) {
			tv_title.setText("立即推荐");
		} else if (Constants.RENT_WANT.equals(whereFrom)) {
			tv_title.setText("我要租房");
			editTextTel.setText(mobile);
		} else if (Constants.OLD_RECOMMEND.equals(whereFrom)) {
			tv_title.setText("立即推荐");
		} else if (Constants.OLD_WANT.equals(whereFrom)) {
			tv_title.setText("我要买房");
			editTextTel.setText(mobile);
		} else if (Constants.BUILDING_RECOMMEND.equals(whereFrom)) {
			tv_title.setText("立即推荐");
		} else if (Constants.BUILDING_WANT_LOOK.equals(whereFrom)) {
			tv_title.setText("我要买房");
			editTextTel.setText(mobile);
		}
	}

	@Override
	public void initViews() {
		imageViewPic = (ImageView) this.findViewById(R.id.imageViewPic);
		textViewTitle = (TextView) this.findViewById(R.id.textViewTitle);
		tv_tijiao = (TextView) this.findViewById(R.id.tv_tijiao);
		tv_tijiao.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				commit(v);
			}
		});
		textViewSth = (TextView) this.findViewById(R.id.textViewSth);
		textViewRooms = (TextView) this.findViewById(R.id.textViewRooms);
		textViewMoney = (TextView) this.findViewById(R.id.textViewMoney);
		textViewAddress = (TextView) this.findViewById(R.id.textViewAddress);
		editTextName = (EditText) this.findViewById(R.id.editTextName);
		editTextRemark = (EditText) this.findViewById(R.id.editTextRemark);

		ImageLoader.getInstance().displayImage(
				Constants.IMAGE_URL + houseRentItem.getHouseRentItemPic(),
				imageViewPic);
		textViewTitle.setText(houseRentItem.getHouseRentItemTitle());
		textViewSth.setText(houseRentItem.getHouseRentItemSth());
		textViewRooms.setText(houseRentItem.getHouseRentItemRooms());
		textViewMoney.setText(houseRentItem.getHouseRentItemMoney());
		textViewAddress.setText(houseRentItem.getHouseRentItemAddress());
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
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

	public void commit(View view) {
		String name = editTextName.getText().toString();
		String tel = editTextTel.getText().toString();
		String remark = editTextRemark.getText().toString().trim();
		if ("".equals(name)) {
			Tools.showToast(this, "请输入姓名！");
		} else if ("".equals(tel)) {
			Tools.showToast(this, "请输入手机号码！");
		} else {
			Map<String, String> map = new HashMap<String, String>();
			map.put("assicoateGuid", houseRentItem.getHouseRentItemId());
			map.put("guid", "");
			map.put("userGuid", userId);
			map.put("clientName", name);
			map.put("clientPhone", tel);
			map.put("clientCardId", "");
			map.put("clientQQ", "");
			map.put("clientEmail", "");
			map.put("clientRemark", remark);
			map.put("methodType", "add");
			map.put("Token",
					AESUtils.encode("assicoateGuid").replaceAll("\n", ""));
			show();
			System.out.println("========map.toString()=====" + map.toString());
			JustDoSth justDoSth = new JustDoSth(this, Constants.RECOMMEND, map);
			justDoSth.setOnGetDataSuccessListener(new GetDataSuccessListener() {
				@Override
				public void onGetDataSuccess(String tag, Object obj) {
					dismiss();
					if (obj != null) {
						Tools.showToast(HouseRecommendActivity.this, "信息提交成功!");
						finish();
					}
				}
			});
		}
	}

	@Override
	protected void onDestroy() {
		dismiss();
		super.onDestroy();
	}
}
