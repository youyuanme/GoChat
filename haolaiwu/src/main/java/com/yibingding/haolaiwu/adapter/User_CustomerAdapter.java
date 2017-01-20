package com.yibingding.haolaiwu.adapter;

import java.util.List;

import com.ybd.app.tools.PreferenceHelper;
import com.yibingding.haolaiwu.R;
import com.yibingding.haolaiwu.dialog.User_SimpleDialog;
import com.yibingding.haolaiwu.domian.Customer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class User_CustomerAdapter extends BaseAdapter implements
		OnClickListener {
	private List<Customer> list;
	private Context cxt;
	private int type;
	private boolean b;
	private OnClickListener listener;

	public OnClickListener getListener() {
		return listener;
	}

	public void setListener(OnClickListener listener) {
		this.listener = listener;
	}

	public User_CustomerAdapter(Context cxt, List<Customer> list, int type) {
		this.list = list;
		this.cxt = cxt;
		this.type = type;
		Log.v("adapter", list.toString());
	}

	public User_CustomerAdapter(Context cxt, List<Customer> list, int type,
			boolean b) {
		this.list = list;
		this.cxt = cxt;
		this.type = type;
		this.b = b;
		Log.v("adapter", list.toString());
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Customer getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder holder = null;
		if (arg1 == null) {
			holder = new ViewHolder();
			arg1 = LayoutInflater.from(cxt).inflate(
					R.layout.user_myconsumer_item, null);
			arg1.setTag(holder);
			holder.name = (TextView) arg1.findViewById(R.id.name);
			holder.phone = (TextView) arg1.findViewById(R.id.phone);
			holder.time = (TextView) arg1.findViewById(R.id.time);
			holder.btn1 = (TextView) arg1.findViewById(R.id.btn1);
			holder.btn2 = (TextView) arg1.findViewById(R.id.btn2);

			holder.btn1.setOnClickListener(listener);
			holder.btn2.setOnClickListener(listener);

		} else {
			holder = (ViewHolder) arg1.getTag();
		}
		Customer c = getItem(arg0);
		holder.name.setText(arg0 + 1 + "、" + c.getT_Client_Name());
		if (TextUtils.equals(c.getT_Client_eState(), "1")) {
			if (type == 3) {
				holder.btn1.setVisibility(View.GONE);
				holder.btn2.setVisibility(View.GONE);
			}
			holder.time.setText("审核时间：" + c.getT_Client_dDate() + "\n访问时间："
					+ c.getT_Client_sDate() + "\n大定时间：" + c.getT_Client_mDate()
					+ "\n签约时间：" + c.getT_Client_eDate());
		} else if (TextUtils.equals(c.getT_Client_mState(), "1")) {
			if (type == 3) {
				holder.btn1.setVisibility(View.GONE);
				holder.btn2.setVisibility(View.VISIBLE);
				holder.btn2.setText("客户签约");
			}
			holder.time
					.setText("审核时间：" + c.getT_Client_dDate() + "\n访问时间："
							+ c.getT_Client_sDate() + "\n大定时间："
							+ c.getT_Client_mDate());
		} else if (TextUtils.equals(c.getT_Client_sState(), "1")) {
			if (type == 3) {
				holder.btn1.setVisibility(View.GONE);
				holder.btn2.setVisibility(View.VISIBLE);
				holder.btn2.setText("客户大定");
			}
			holder.time.setText("审核时间：" + c.getT_Client_dDate() + "\n访问时间："
					+ c.getT_Client_sDate());
		} else if (TextUtils.equals(c.getT_Client_dState(), "1")) {
			if (type == 3) {
				holder.btn1.setVisibility(View.GONE);
				holder.btn2.setVisibility(View.VISIBLE);
				holder.btn2.setText("客户到访");
			}
			holder.time.setText("审核时间：" + c.getT_Client_dDate());
		}
		holder.btn1.setTag(c);
		holder.btn2.setTag(c);
		String phoneString = c.getT_Client_Phone();
		String tStyle = PreferenceHelper.readString(cxt, "userinfo", "tStyle",
				"");
		if (b && TextUtils.equals("1", tStyle)) {
			holder.phone.setText(phoneString.substring(0, 3) + "****"
					+ phoneString.substring(7, 11));
			holder.time.setText(c.getT_Client_dDate());
		} else {
			holder.phone.setText(phoneString);
			holder.phone.setTextColor(cxt.getResources().getColor(
					R.color.callphonecolor));
			holder.phone.setTag(phoneString);
			holder.phone.setOnClickListener(this);
		}
		if (type == 2) {
			if (c.getT_Client_dState().equals("0")) { // 0表示未审核
				holder.btn1.setVisibility(View.VISIBLE);
				holder.btn2.setVisibility(View.VISIBLE);
				holder.btn1.setText(" 首访 ");
				holder.btn2.setText("非首访");
			} else {
				holder.btn1.setVisibility(View.VISIBLE);
				holder.btn2.setVisibility(View.VISIBLE);
				holder.btn1.setText(" 查看 ");
				if (c.getT_Client_Consultant().equals("")) { // 空的表示未分配
					holder.btn2.setText("未分配");
				} else {
					holder.btn2.setText("已分配");
				}
			}

		} /*
		 * else { holder.btn1.setVisibility(View.GONE); if
		 * (c.getT_Client_sState().equals("0")) { holder.btn2.setText("客户到访"); }
		 * else if (c.getT_Client_mState().equals("0")) {
		 * holder.btn2.setText("客户大定"); } else { holder.btn2.setText("客户签约"); }
		 * // Log.v("严重问题", "数据逻辑有问题"); }
		 */
		// }
		return arg1;
	}

	class ViewHolder {
		TextView name, phone, time, btn1, btn2;
	}

	@Override
	public void onClick(View v) {
		final String phoneString = (String) v.getTag();
		final User_SimpleDialog dialogCallPhone = new User_SimpleDialog(cxt);
		dialogCallPhone.setTitleText("是否呼叫：" + phoneString);
		dialogCallPhone.setConfirmListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ phoneString));
				cxt.startActivity(intent);
				dialogCallPhone.dismiss();
			}
		});
		dialogCallPhone.show();
	}

}
