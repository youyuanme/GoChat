package com.yibingding.haolaiwu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import cn.jpush.android.api.b;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ybd.app.BaseActivity;
import com.ybd.app.tools.DensityUtils;
import com.ybd.app.tools.PreferenceHelper;
import com.ybd.app.tools.Tools;
import com.ybd.app.volley.VolleyPost;
import com.yibingding.haolaiwu.domian.BankCard;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.Constants;

public class User_TakeMoneyActivity extends BaseActivity implements
		OnClickListener {

	private TextView value;
	private EditText input;
	private List<BankCard> list;
	private LinearLayout bankcontainer;
	private List<View> cardViews = new ArrayList<View>();
	private BankCard card;
	private ProgressDialog dialog;
	private float reward;

	@Override
	public void onCreateThisActivity() {
		setContentView(R.layout.user_takemoney);
	}

	@Override
	public void initViews() {
		bankcontainer = (LinearLayout) findViewById(R.id.bankcontainer);
		RelativeLayout rl_title = (RelativeLayout) findViewById(R.id.rl_title);
		ScrollView scrollview = (ScrollView) findViewById(R.id.scrollview);
		int contentHeight = DensityUtils.getScreenH(this)
				- DensityUtils.dip2px(this, 65);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, contentHeight);
		lp.addRule(RelativeLayout.BELOW, R.id.rl_title);
		lp.addRule(RelativeLayout.ABOVE, R.id.submit);
		scrollview.setLayoutParams(lp);

		value = (TextView) findViewById(R.id.value);
		input = (EditText) findViewById(R.id.input);
		input.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// String inputSrring = arg0.toString();
				// if (!TextUtils.isEmpty(inputSrring)) {
				// float inputjine = Float.parseFloat(inputSrring);
				// if (inputjine > reward) {
				// input.setText(reward + "");
				// } else {
				// input.setText(inputSrring);
				// }
				// }
			}
		});

	}

	@Override
	public void initData() {
		reward = getIntent().getFloatExtra("zhanghuyue", 0.00f);
		value.setText("￥" + reward);
		getBank();
	}

	public void back(View v) {
		finish();
	}

	public void submit(View v) {
		CharSequence valuestr = input.getText();
		if (valuestr == null || valuestr.toString().equals("")) {
			Tools.showToast(this, "请输入提现金额!");
			return;
		}
		float inputNum = Float.parseFloat(valuestr.toString());
		if (inputNum > reward) {
			Tools.showToast(this, "请输入合适的金额!");
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("userGuid",
				PreferenceHelper.readString(this, "userinfo", "guid"));
		params.put("guid", "");
		params.put("Reamrk", "");
		params.put("BankGuid", card.getGuid());
		params.put("methodType", "add");
		params.put("Money", valuestr.toString());
		params.put("Token", AESUtils.encode("userGuid"));
		if (dialog == null) {
			dialog = new ProgressDialog(this);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setMessage("正在努力加载中...");
		}
		dialog.show();
		System.out.println("========params.toString()===" + params.toString());
		VolleyPost request = new VolleyPost(this, Constants.SERVER_IP
				+ Constants.User_TakeMonyRequst_URL, params) {

			@Override
			public void pullJson(String json) {
				// Log.v("this", "获取的银行列表"+json);
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				if (json == null || json.equals("")) {
					Tools.showToast(context, "网络连接有问题!");
					return;
				}
				JSONArray array = JSONArray.parseArray(json);
				JSONObject obj = array.getJSONObject(0);
				if (obj.getString("state").equals("true")) {
					Tools.showToast(context, obj.getString("result"));
					User_TakeMoneyActivity.this.finish();
					// String v = obj.getString("result");
					// list =JSON.parseArray(v, BankCard.class);
					// addBankView();
				} else {
					Tools.showToast(context, "提示信息：" + obj.getString("result"));
				}
			}

			@Override
			public String getPageIndex() {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}

	private void getBank() {
		if (dialog == null) {
			dialog = new ProgressDialog(this);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setMessage("正在努力加载中...");
		}
		dialog.show();
		Map<String, String> params = new HashMap<String, String>();
		params.put("userGuid",
				PreferenceHelper.readString(this, "userinfo", "guid"));
		params.put("Token", AESUtils.encode("userGuid"));
		System.out
				.println("=========params.toString()====" + params.toString());
		VolleyPost request = new VolleyPost(this, Constants.SERVER_IP
				+ Constants.User_GetUserBank_URL, params) {

			@Override
			public void pullJson(String json) {
				// Log.v("this", "获取的银行列表"+json);
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				if (json == null || json.equals("")) {
					Toast.makeText(User_TakeMoneyActivity.this, "网络连接有问题！",
							Toast.LENGTH_SHORT).show();
					return;
				}
				JSONArray array = JSONArray.parseArray(json);
				JSONObject obj = array.getJSONObject(0);
				if (obj.getString("state").equals("true")) {
					String v = obj.getString("result");
					list = JSON.parseArray(v, BankCard.class);
					System.out.println("=======list.size()===" + list.size());
					addBankView();
				}
			}

			@Override
			public String getPageIndex() {
				return null;
			}
		};

	}

	public void addBankView() {
		for (int i = 0; i < list.size(); i++) {
			BankCard card = list.get(i);
			View view = getLayoutInflater().inflate(R.layout.user_mybankitem,
					null);
			view.setTag(card);
			if (i == 0) {
				card.setSelecked(true);
			}
			if (card.isSelecked()) {
				this.card = card;
				((RadioButton) view.findViewById(R.id.radio)).setChecked(true);
			}
			view.findViewById(R.id.pike).setVisibility(View.GONE);
			view.findViewById(R.id.radio).setVisibility(View.VISIBLE);
			view.setOnClickListener(this);
			cardViews.add(view);

			((TextView) view.findViewById(R.id.name)).setText(card
					.getT_Bank_Name());
			String no = card.getT_Bank_NO();
			String displayString = no.substring(0, 4) + "****"
					+ no.substring(no.length() - 4, no.length());
			((TextView) view.findViewById(R.id.value)).setText("卡号："
					+ displayString);
			bankcontainer.addView(view);
		}
	}

	@Override
	public void onClick(View view) {
		for (View v : cardViews) {
			((BankCard) v.getTag()).setSelecked(false);
		}
		((BankCard) view.getTag()).setSelecked(true);
		for (View v : cardViews) {
			BankCard card2 = (BankCard) v.getTag();
			if (card2.isSelecked()) {
				this.card = card2;
				System.out.println("=========this.card.getT_Bank_Name()===="
						+ this.card.getT_Bank_Name());
				((RadioButton) v.findViewById(R.id.radio)).setChecked(true);
			} else {
				((RadioButton) v.findViewById(R.id.radio)).setChecked(false);
			}
		}
	}
}
