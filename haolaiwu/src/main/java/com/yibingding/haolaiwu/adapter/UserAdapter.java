package com.yibingding.haolaiwu.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yibingding.haolaiwu.R;
import com.yibingding.haolaiwu.dialog.User_SimpleDialog;
import com.yibingding.haolaiwu.domian.User;
import com.yibingding.haolaiwu.tools.Constants;

public class UserAdapter extends BaseAdapter implements OnClickListener {

	private List<User> list;
	private Context cxt;
	private ImageLoader imageLoader;

	public UserAdapter(List<User> list, Context cxt) {
		this.list = list;
		this.cxt = cxt;
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public User getItem(int arg0) {
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
			arg1 = LayoutInflater.from(cxt).inflate(R.layout.user_myteamitem,
					null);
			holder.name = (TextView) arg1.findViewById(R.id.name);
			holder.phone = (TextView) arg1.findViewById(R.id.phone);
			holder.typename = (TextView) arg1.findViewById(R.id.tyepname);
			holder.img = (ImageView) arg1.findViewById(R.id.image);
			arg1.setTag(holder);
		} else {
			holder = (ViewHolder) arg1.getTag();
		}
		User u = getItem(arg0);
		holder.name.setText(u.getT_User_RealName());
		String phone = u.getT_User_Mobile();
		if (!TextUtils.isEmpty(phone)) {
			holder.phone.setTextColor(cxt.getResources().getColor(
					R.color.callphonecolor));
			holder.phone.setText(phone);
			holder.phone.setTag(phone);
			holder.phone.setOnClickListener(this);
		}
		holder.typename.setText(u.getUserStyle());
		String imageString = u.getT_User_Pic();
		String displayImage = "";
		if (!TextUtils.isEmpty(imageString)) {
			displayImage = Constants.IMAGE_URL + imageString;
		}
		ImageLoader.getInstance().displayImage(displayImage, holder.img);

		return arg1;
	}

	private final class ViewHolder {
		TextView name, typename, phone;
		ImageView img;
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
