package com.yibingding.haolaiwu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.ybd.app.BaseActivity;
import com.ybd.app.interf.GetDataSuccessListener;
import com.yibingding.haolaiwu.adapter.MessageCenter_Adapter;
import com.yibingding.haolaiwu.domian.MessageSenterBean;
import com.yibingding.haolaiwu.internet.GetMessage_Volley;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.Constants;

public class User_MsgCenterActivity extends BaseActivity implements
		OnClickListener, GetDataSuccessListener, OnItemClickListener {
	private TextView option;
	private List<MessageSenterBean> arrayList;
	private MessageCenter_Adapter messageCenter_Adapter;
	private ListView lv_message_center;
	private boolean isComplete = true;

	@Override
	public void onCreateThisActivity() {
		setContentView(R.layout.user_msgcenter);
	}

	@Override
	public void initViews() {
		option = (TextView) findViewById(R.id.option);
		lv_message_center = (ListView) findViewById(R.id.lv_message_center);
		lv_message_center.setOnItemClickListener(this);
		option.setOnClickListener(this);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResume() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userGuid",
				getSharedPreferences("userinfo", Context.MODE_PRIVATE)
						.getString("guid", ""));
		map.put("Token", AESUtils.encode("userGuid").replaceAll("\n", ""));
		GetMessage_Volley getMessage_Volley = new GetMessage_Volley(this,
				Constants.MY_GETMESSAGE_URL, map, "0");
		getMessage_Volley.setOnGetDataSuccessListener(this);
		super.onResume();
	}

	public void back(View v) {
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.option:
			if (isComplete) {
				option.setText("完成");
				isComplete = false;
				for (int i = 0; i < arrayList.size(); i++) {
					arrayList.get(i).isEdit = 1;
				}
				messageCenter_Adapter.update(arrayList);
				messageCenter_Adapter.notifyDataSetChanged();
			} else {
				option.setText("编辑");
				isComplete = true;
				for (int i = 0; i < arrayList.size(); i++) {
					arrayList.get(i).isEdit = 0;
				}
				messageCenter_Adapter.update(arrayList);
				messageCenter_Adapter.notifyDataSetChanged();
			}
			break;

		}
	}

	@Override
	public void onGetDataSuccess(String tag, Object obj) {
		// TODO Auto-generated method stub
		if (obj != null) {
			if ("0".equals(tag)) {
				arrayList = (List<MessageSenterBean>) obj;
				option.setVisibility(View.VISIBLE);
				messageCenter_Adapter = new MessageCenter_Adapter(this,
						arrayList, null);
				lv_message_center.setAdapter(messageCenter_Adapter);
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (isComplete) {
			Intent intent = new Intent(this, MessageContent_Activity.class);
			intent.putExtra("messGuid", arrayList.get(position).getGuid());
			startActivity(intent);
		}
	}

}
