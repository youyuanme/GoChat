package com.yibingding.haolaiwu.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.integer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ybd.app.MyBaseAdapter;
import com.ybd.app.interf.GetDataSuccessListener;
import com.ybd.app.tools.Tools;
import com.yibingding.haolaiwu.R;
import com.yibingding.haolaiwu.domian.MessageSenterBean;
import com.yibingding.haolaiwu.internet.Comment_Volley;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.Constants;
import com.yibingding.haolaiwu.view.ListItemDelete1;

public class MessageCenter_Adapter<T, V> extends MyBaseAdapter<T> {

	public ArrayList<MessageSenterBean> arrayList;
	private Context mContext;
	public static ListItemDelete1 listItemDelete = null;

	public MessageCenter_Adapter(Context context, List<T> data, Object object) {
		super(context, data, object);
		arrayList = (ArrayList<MessageSenterBean>) data;
		this.mContext = context;
	}

	public void update(List<T> data) {
		arrayList = (ArrayList<MessageSenterBean>) data;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView != null /*
								 * && ((ViewHolder)
								 * convertView.getTag()).getPosition() ==
								 * position
								 */) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			holder = new ViewHolder();
			holder.position = position;
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_message_center, null);

			holder.ll_list_item_del = (ListItemDelete1) convertView
					.findViewById(R.id.ll_list_item_del);
			holder.tv_message_center_title = (TextView) convertView
					.findViewById(R.id.tv_message_center_title);
			holder.tv_message_center_content = (TextView) convertView
					.findViewById(R.id.tv_message_center_content);
			holder.tv_message_center_time = (TextView) convertView
					.findViewById(R.id.tv_message_center_time);
			holder.tv_del = (TextView) convertView.findViewById(R.id.tv_del);
			convertView.setTag(holder);
		}
		MessageSenterBean bean = arrayList.get(position);

		holder.tv_message_center_title.setText(bean.getT_Messages_Title());
		String ifRead = bean.getIfRead();
		if ("0".equals(ifRead)) {
			holder.tv_message_center_title
					.setCompoundDrawablesWithIntrinsicBounds(
							mContext.getResources().getDrawable(
									R.drawable.message_center_redround), null,
							null, null);
		} else {
			holder.tv_message_center_title
					.setCompoundDrawablesWithIntrinsicBounds(null, null, null,
							null);
		}
		holder.tv_message_center_content.setText(bean.getT_Messages_Contents());
		holder.tv_message_center_time.setText(bean.getT_AddDate());
		holder.tv_del.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("userGuid",
						mContext.getSharedPreferences("userinfo",
								Context.MODE_PRIVATE).getString("guid", ""));
				map.put("messageGuid", arrayList.get(position).getGuid());
				map.put("Token",
						AESUtils.encode("userGuid").replaceAll("\n", ""));
				Comment_Volley result_Volley = new Comment_Volley(mContext,
						Constants.MY_DELMESSAGE_URL, map);
				result_Volley
						.setOnGetDataSuccessListener(new GetDataSuccessListener() {
							@Override
							public void onGetDataSuccess(String tag, Object obj) {
								if (obj != null) {
									Map<String, String> map = (Map<String, String>) obj;
									if ("true".equals(map.get("state"))) {
										arrayList.remove(position);
										MessageCenter_Adapter.this
												.notifyDataSetChanged();
									}
									Tools.showToast(mContext, map.get("result"));
								}
							}
						});
			}
		});

		if (bean.isEdit == 0) {
			holder.ll_list_item_del.reSet();
		} else {
			holder.ll_list_item_del.openDel();
		}

		return convertView;
	}

	final class ViewHolder {
		private TextView tv_message_center_title, tv_message_center_content,
				tv_message_center_time, tv_del;
		private ListItemDelete1 ll_list_item_del;
		private int position;

		public int getPosition() {
			return position;
		}
	}

}
