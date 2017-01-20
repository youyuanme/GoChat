package com.yibingding.haolaiwu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ybd.app.BaseActivity;
import com.ybd.app.tools.PreferenceHelper;
import com.ybd.app.volley.VolleyPost;
import com.yibingding.haolaiwu.domian.BankCard;
import com.yibingding.haolaiwu.domian.User;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.Constants;

public class User_MyBankActivity extends BaseActivity implements
		OnClickListener {
	List<BankCard> list;
	LinearLayout bankcontainer;
	ProgressDialog dialog;

	@Override
	public void onCreateThisActivity() {
		setContentView(R.layout.user_mybank);
	}

	@Override
	public void initViews() {
		bankcontainer = (LinearLayout) findViewById(R.id.bankcontainer);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		getBank();
	}

	public void back(View v) {
		finish();
	}

	public void addbank(View v) {
		Intent intent = new Intent(this, User_MyBank_AddBankActivity.class);
		startActivityForResult(intent, 1);
	}

	private void getBank() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userGuid",
				PreferenceHelper.readString(this, "userinfo", "guid"));
		params.put("Token", AESUtils.encode("userGuid"));
		if (dialog == null) {
			dialog = new ProgressDialog(this);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setMessage("正在努力加载中...");
		}
		dialog.show();
		VolleyPost request = new VolleyPost(this, Constants.SERVER_IP
				+ Constants.User_GetUserBank_URL, params) {
			@Override
			public void pullJson(String json) {
				// Log.v("this", "获取的银行列表"+json);
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				if (json == null || json.equals("")) {
					Toast.makeText(User_MyBankActivity.this, "网络连接有问题！",
							Toast.LENGTH_SHORT).show();
					return;
				}

				JSONArray array = JSONArray.parseArray(json);
				JSONObject obj = array.getJSONObject(0);
				if (obj.getString("state").equals("true")) {
					String v = obj.getString("result");
					list = JSON.parseArray(v, BankCard.class);

					addBankView();
				}
			}

			@Override
			public String getPageIndex() {
				// TODO Auto-generated method stub
				return null;
			}
		};

	}

	public void addBankView() {
		for (int i = 0; i < list.size(); i++) {
			BankCard card = list.get(i);
			View view = getLayoutInflater().inflate(R.layout.user_mybankitem,
					null);
			view.setOnClickListener(this);
			view.setTag(i);
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

	// class BankItemAdapter extends BaseAdapter{
	// @Override
	// public int getCount() {
	// // TODO Auto-generated method stub
	// return list.size();
	// }
	//
	// @Override
	// public BankCard getItem(int arg0) {
	// // TODO Auto-generated method stub
	// return list.get(arg0);
	// }
	//
	// @Override
	// public long getItemId(int arg0) {
	// // TODO Auto-generated method stub
	// return arg0;
	// }
	//
	// @Override
	// public View getView(int arg0, View arg1, ViewGroup arg2) {
	// // TODO Auto-generated method stub
	// ViewHolder holder = null;
	// if(arg1==null){
	// holder = new ViewHolder();
	// arg1 = getLayoutInflater().inflate(R.layout.user_mybankitem,null);
	// holder.name = (TextView) arg1.findViewById(R.id.name);
	// holder.value = (TextView) arg1.findViewById(R.id.value);
	// holder.image = (ImageView) arg1.findViewById(R.id.image);
	// arg1.setTag(holder);
	// }
	// holder = (ViewHolder) arg1.getTag();
	// BankCard card = getItem(arg0);
	// holder.name.setText(card.getT_Bank_Name());
	// String no = card.getT_Bank_NO();
	// if(no.length()>4){
	// holder.value.setText(no.substring(no.length()-4,no.length()));
	// }else{
	// holder.value.setText(no);
	// }
	// switch (card.getBanktype()) {
	// case 0:
	// holder.image.setImageResource(R.drawable.user_mybank_gsbank_img);
	// break;
	// case 1:
	// holder.image.setImageResource(R.drawable.user_mybank_jsbank_img);
	// break;
	// case 2:
	// holder.image.setImageResource(R.drawable.user_mybank_jtbank_img);
	// break;
	// case 3:
	// holder.image.setImageResource(R.drawable.user_mybank_zgbank_img);
	// break;
	// case 4:
	// holder.image.setImageResource(R.drawable.user_mybank_nybank_img);
	// break;
	// case 5:
	// holder.image.setImageResource(R.drawable.user_mybank_zxbank_img);
	// break;
	// default:
	// holder.image.setImageResource(R.drawable.user_mybank_gsbank_img);
	// break;
	// }
	// return arg1;
	// }
	//
	//
	// }
	// class ViewHolder{
	// TextView name,value;
	// ImageView image;
	// }
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		int index = (Integer) arg0.getTag();
		if (index > -1 && index < list.size()) {
			BankCard card = list.get(index);
			Intent intent = new Intent(this, User_MyBankDetailsActivity.class);
			intent.putExtra("index", index);
			intent.putExtra("guid", card.getGuid());
			intent.putExtra("name", card.getT_Bank_Name());
			intent.putExtra("type", card.getBanktype());
			intent.putExtra("no", card.getT_Bank_NO());
			intent.putExtra("accountname", card.getT_Bank_OpenUser());
			intent.putExtra("openaddress", card.getT_Bank_OpenAddress());
			startActivityForResult(intent, 1);
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg0 == 1 && arg1 == 1) {
			int index = arg2.getIntExtra("index", 0);
			list.remove(index);
			bankcontainer.removeViewAt(index);
			// getBank();
		} else if (arg0 == 1 && arg1 == 2) {
			BankCard card = new BankCard();
			card.setT_Bank_Name(arg2.getStringExtra("name"));
			card.setT_Bank_NO(arg2.getStringExtra("no"));
			card.setT_Bank_OpenUser(arg2.getStringExtra("accountname"));
			list.add(card);
			bankcontainer.removeAllViews();
			addBankView();
		}

	}
}
