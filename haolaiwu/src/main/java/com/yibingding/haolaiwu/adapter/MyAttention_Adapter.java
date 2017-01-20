package com.yibingding.haolaiwu.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.raw;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ybd.app.MyBaseAdapter;
import com.ybd.app.interf.GetDataSuccessListener;
import com.ybd.app.tools.Tools;
import com.yibingding.haolaiwu.R;
import com.yibingding.haolaiwu.domian.MyAttentionBean;
import com.yibingding.haolaiwu.internet.Return_result_Volley;
import com.yibingding.haolaiwu.tools.AESUtils;
import com.yibingding.haolaiwu.tools.Constants;
import com.yibingding.haolaiwu.view.ListItemDelete;
import com.yibingding.haolaiwu.view.ScrollListviewDelete.ItemClickListener;

public class MyAttention_Adapter<T> extends MyBaseAdapter<T> {
	public ArrayList<MyAttentionBean> arrayList;
	private Context mContext;
	public static ListItemDelete listItemDelete = null;

	public MyAttention_Adapter(Context context, List<T> data, Object object) {
		super(context, data, object);
		arrayList = (ArrayList<MyAttentionBean>) data;
		this.mContext = context;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_my_attention, null);
			holder.ll_item_view = (LinearLayout) convertView
					.findViewById(R.id.ll_item_view);
			holder.iv_new_item = (ImageView) convertView
					.findViewById(R.id.iv_new_item);
			holder.tv_my_attention_type = (ImageView) convertView
					.findViewById(R.id.tv_my_attention_type);
			holder.tv_my_attention_name = (TextView) convertView
					.findViewById(R.id.tv_my_attention_name);
			holder.tv_my_attention_time = (TextView) convertView
					.findViewById(R.id.tv_my_attention_time);
			holder.tv_my_attention_del = (TextView) convertView
					.findViewById(R.id.tv_my_attention_del);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();

		}
		MyAttentionBean bean = arrayList.get(position);

		String imageString = bean.getPic();
		String displayImage = "";
		if (!TextUtils.isEmpty(imageString)) {
			displayImage = Constants.IMAGE_URL + imageString;
		}
		ImageLoader.getInstance()
				.displayImage(displayImage, holder.iv_new_item);

		holder.tv_my_attention_name.setText(bean.getNames());
		String style = bean.getStyle();
		if ("租赁".equals(style)) {
			holder.tv_my_attention_type
					.setImageResource(R.drawable.my_attention_lease);
		} else if ("新闻".equals(style)) {
			holder.tv_my_attention_type
					.setImageResource(R.drawable.my_attention_news);
		} else if ("二手房".equals(style)) {
			holder.tv_my_attention_type
					.setImageResource(R.drawable.my_attention_ershou);
		} else if ("楼盘".equals(style)) {
			holder.tv_my_attention_type
					.setImageResource(R.drawable.my_attention_loupan);
		}
		holder.tv_my_attention_time.setText(bean.getT_Collection_Date());
		holder.tv_my_attention_del.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MyAttention_Adapter.this.ItemDeleteReset();
				Map<String, String> map = new HashMap<String, String>();
				map.put("collectionGuid", arrayList.get(position).getGuid());
				map.put("Token",
						AESUtils.encode("collectionGuid").replaceAll("\n", ""));
				Return_result_Volley result_Volley = new Return_result_Volley(
						mContext, Constants.MY_ATTENTION_DELCOLLECION_URL, map);
				result_Volley
						.setOnGetDataSuccessListener(new GetDataSuccessListener() {
							@Override
							public void onGetDataSuccess(String tag, Object obj) {
								if (obj != null) {
									Map<String, String> map = (Map<String, String>) obj;
									if ("true".equals(map.get("state"))) {
										arrayList.remove(position);
										MyAttention_Adapter.this
												.notifyDataSetInvalidated();
										Tools.showToast(
												mContext,
												mContext.getString(R.string.delete_succeed));
									} else {
										Tools.showToast(
												mContext,
												mContext.getString(R.string.delete_defeated));
									}
								}
							}
						});
			}
		});
		return convertView;
	}

	final class ViewHolder {
		private ImageView iv_new_item, tv_my_attention_type;
		private TextView tv_my_attention_name, tv_my_attention_time,
				tv_my_attention_del;
		private LinearLayout ll_item_view;
	}

	public static void ItemDeleteReset() {
		if (listItemDelete != null) {
			listItemDelete.reSet();
		}
	}

	// public interface ItemClickListener {
	// void onItemClick(int position);
	// }
	//
	// private ItemClickListener onItemClickListener;
	//
	// public void setOnItemClickListener(ItemClickListener onItemClickListener)
	// {
	// this.onItemClickListener = onItemClickListener;
	// }
}
