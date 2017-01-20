package com.yibingding.haolaiwu;

import java.util.HashMap;
import java.util.Map;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.ybd.app.BaseActivity;
import com.ybd.app.interf.GetDataSuccessListener;
import com.ybd.app.tools.PreferenceHelper;
import com.ybd.app.tools.Tools;
import com.yibingding.haolaiwu.dialog.Dialognetwork;
import com.yibingding.haolaiwu.internet.JustDoSth;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.Constants;

public class WoyaotuijianActivity extends BaseActivity {

	private TextView tv_tijiao;
	private EditText editTextName, editTextTel, editTextRemark;
	private Dialognetwork dialog;

	public void back(View view) {
		finish();
	}

	@Override
	public void onCreateThisActivity() {
		setContentView(R.layout.activity_woyaotuijian);
	}

	@Override
	public void initViews() {
		editTextTel = (EditText) this.findViewById(R.id.editTextTel);
		tv_tijiao = (TextView) this.findViewById(R.id.tv_tijiao);
		tv_tijiao.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				commit(v);
			}
		});
		editTextName = (EditText) this.findViewById(R.id.editTextName);
		editTextRemark = (EditText) this.findViewById(R.id.editTextRemark);
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
			map.put("assicoateGuid", "");
			map.put("guid", "");
			map.put("userGuid",
					PreferenceHelper.readString(this, "userinfo", "guid", ""));
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
			JustDoSth justDoSth = new JustDoSth(this, Constants.RECOMMEND, map);
			justDoSth.setOnGetDataSuccessListener(new GetDataSuccessListener() {
				@Override
				public void onGetDataSuccess(String tag, Object obj) {
					dismiss();
					if (obj != null) {
						Tools.showToast(WoyaotuijianActivity.this, "信息提交成功!");
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
