package com.yibingding.haolaiwu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ybd.app.BaseActivity;
import com.ybd.app.interf.GetDataSuccessListener;
import com.yibingding.haolaiwu.adapter.MessageCenter_Adapter;
import com.yibingding.haolaiwu.domian.MessageSenterBean;
import com.yibingding.haolaiwu.internet.GetMessage_Volley;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.Constants;

public class MessageContent_Activity extends BaseActivity implements
		GetDataSuccessListener {

	private List<MessageSenterBean> arrayList;
	private TextView tv_title, tv_time, tv_content;
	
	@Override
	public void onCreateThisActivity() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_message_content);
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_content = (TextView) findViewById(R.id.tv_content);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		Map<String, String> map = new HashMap<String, String>();
		map.put("messGuid", getIntent().getStringExtra("messGuid"));
		map.put("userGuid",
				getSharedPreferences("userinfo", Context.MODE_PRIVATE)
						.getString("guid", ""));
		map.put("Token", AESUtils.encode("messGuid").replaceAll("\n", ""));
		GetMessage_Volley getMessage_Volley = new GetMessage_Volley(this,
				Constants.MY_GETMESSAGEDETAILS_URL, map, "1");
		getMessage_Volley.setOnGetDataSuccessListener(this);
	}

	public void back(View view) {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	public void onGetDataSuccess(String tag, Object obj) {
		// TODO Auto-generated method stub
		if (obj != null) {
			if ("1".equals(tag)) {
				arrayList = (List<MessageSenterBean>) obj;
				MessageSenterBean bean = arrayList.get(0); 
				tv_title.setText(bean.getT_Messages_Title());
				tv_time.setText(bean.getT_AddDate());
				tv_content.setText(bean.getT_Messages_Contents());
			}
		}
	}

}
