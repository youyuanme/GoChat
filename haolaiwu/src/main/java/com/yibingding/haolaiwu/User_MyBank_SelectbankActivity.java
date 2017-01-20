package com.yibingding.haolaiwu;

import android.content.Intent;
import android.view.View;

import com.ybd.app.BaseActivity;

public class User_MyBank_SelectbankActivity extends BaseActivity {

	@Override
	public void onCreateThisActivity() {
		// TODO Auto-generated method stub
		setContentView(R.layout.user_mybank_selectbank);

	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}
	public void back(View v){
		finish(); 
	}
	public void select(View v){
		Intent intent = getIntent();
		switch (v.getId()) {
		case R.id.bank_gs:
//			intent.putExtra("banktype", "");
			intent.putExtra("banktype","工商银行");
			setResult(1, intent);
			break;
		case R.id.bank_js:
			intent.putExtra("banktype","建设银行");
			setResult(1, intent);
			break;
		case R.id.bank_jt:
			intent.putExtra("banktype","交通银行");
			setResult(1, intent);
			break;
		case R.id.bank_zg:
			intent.putExtra("banktype","中国银行");
			setResult(1, intent);
			break;
		case R.id.bank_ny:
			intent.putExtra("banktype","农业银行");
			setResult(1, intent);
			break;
		case R.id.bank_zx:
			intent.putExtra("banktype","中信银行");
			setResult(1, intent);
			break;
		default:
			break;
		}
		finish();
	}
}
