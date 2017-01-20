package com.yibingding.haolaiwu;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ybd.app.BaseActivity;
import com.ybd.app.tools.PreferenceHelper;
import com.ybd.app.volley.VolleyPost;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.Constants;

public class User_MyBank_AddBankActivity extends BaseActivity {
	EditText accountname, bankno, et_banktype;
	TextView banktype;
	ProgressDialog dialog;

	@Override
	public void onCreateThisActivity() {
		setContentView(R.layout.user_mybank_addbank);
	}

	@Override
	public void initViews() {
		accountname = (EditText) findViewById(R.id.accountname);
		bankno = (EditText) findViewById(R.id.bankcardno);
		et_banktype = (EditText) findViewById(R.id.et_banktype);
		// banktype =(TextView) findViewById(R.id.banktype);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	public void back(View v) {
		finish();
	}

	public void submit(View v) {
		CharSequence accountnamestr = accountname.getText().toString();
		if (accountnamestr == null || accountnamestr.toString().equals("")) {
			Toast.makeText(this, "请输入开户账号！", Toast.LENGTH_SHORT).show();
			return;
		}
		String banknostr = bankno.getText().toString();
		if (banknostr == null || banknostr.equals("")
				|| banknostr.length() <= 10) {
			Toast.makeText(this, "请输入正确的银行卡号！", Toast.LENGTH_SHORT).show();
			return;
		}
		String bankttypestr = et_banktype.getText().toString();
		if (bankttypestr == null || bankttypestr.equals("")) {
			Toast.makeText(this, "请输入银行卡类型！", Toast.LENGTH_SHORT).show();
			return;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("userGuid",
				PreferenceHelper.readString(this, "userinfo", "guid"));
		params.put("guid", "");
		params.put("bankName", bankttypestr);
		params.put("bankNO", banknostr);
		params.put("openAddress", "");
		params.put("openUser", accountnamestr.toString());
		params.put("methodType", "add");
		params.put("Token", AESUtils.encode("userGuid"));
		// params.put("userPwd",pwdstr.toString());
		if (dialog == null) {
			dialog = new ProgressDialog(this);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setMessage("正在努力加载中...");
		}
		dialog.show();
		System.out.println("===========params.toString()===="
				+ params.toString());
		VolleyPost request = new VolleyPost(this, Constants.SERVER_IP
				+ Constants.User_AddOREditBank_URL, params) {
			@Override
			public void pullJson(String json) {
				// Log.v("this", "添加银行结果"+json);
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				if (json == null || json.equals("")) {
					Toast.makeText(User_MyBank_AddBankActivity.this,
							"网络连接有问题！", Toast.LENGTH_SHORT).show();
					return;
				}
				JSONArray array = JSONArray.parseArray(json);
				JSONObject obj = array.getJSONObject(0);
				if (obj.getString("state").equals("true")) {
					Toast.makeText(User_MyBank_AddBankActivity.this, "添加成功！",
							Toast.LENGTH_SHORT).show();
					Intent intent = getIntent();
					intent.putExtra("no", bankno.getText().toString());
					intent.putExtra("name", et_banktype.getText().toString());
					intent.putExtra("accountname", accountname.getText()
							.toString());
					setResult(2, intent);
					finish();
				} else if (obj.getString("state").equals("false")) {
					Toast.makeText(User_MyBank_AddBankActivity.this,
							"提示信息：" + obj.getString("result"),
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public String getPageIndex() {
				// TODO Auto-generated method stub
				return null;
			}
		};

	}

	public void selecbank(View v) {
		// Intent intent = new Intent(this,
		// User_MyBank_SelectbankActivity.class);
		// startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		if (arg0 == 1 && arg1 == 1) {
			String banktypename = arg2.getStringExtra("banktype");
			banktype.setText(banktypename);
		}
		super.onActivityResult(arg0, arg1, arg2);
	}
}
