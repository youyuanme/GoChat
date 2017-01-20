package com.yibingding.haolaiwu.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ybd.app.MyBaseAdapter;
import com.yibingding.haolaiwu.R;
import com.yibingding.haolaiwu.domian.NewBean;
import com.yibingding.haolaiwu.houseparopertynew.HousePropertyNewDetailsActivity;
import com.yibingding.haolaiwu.tools.Constants;

public class My_NEW_Adapter<T> extends MyBaseAdapter<T> {
	public ArrayList<NewBean> arrayList;

	public My_NEW_Adapter(Context context, List<T> data, Object object) {
		super(context, data, object);
		arrayList = (ArrayList<NewBean>) data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.new_list_item, null);
			holder.iv_new_item = (ImageView) convertView
					.findViewById(R.id.iv_new_item);
			holder.tv_new_item_title = (TextView) convertView
					.findViewById(R.id.tv_new_item_title);
			holder.tv_new_item_cotent = (TextView) convertView
					.findViewById(R.id.tv_new_item_cotent);
			holder.tv_new_item_date = (TextView) convertView
					.findViewById(R.id.tv_new_item_date);
			holder.tv_new_item_pinglun = (TextView) convertView
					.findViewById(R.id.tv_new_item_pinglun);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();

		}
		final NewBean bean = arrayList.get(position);
		String imageString = bean.getT_News_Pic();
		String displayImage = "";
		if (!TextUtils.isEmpty(imageString)) {
			displayImage = Constants.IMAGE_URL + imageString;
		}
		ImageLoader.getInstance()
				.displayImage(displayImage, holder.iv_new_item);
		holder.tv_new_item_title.setText(bean.getT_News_Title());
		holder.tv_new_item_cotent.setText(bean.getT_News_Contents());
		holder.tv_new_item_date.setText(bean.getT_News_Date());
		holder.tv_new_item_pinglun.setText("评论(" + bean.getReplyCount() + ")");
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,
						HousePropertyNewDetailsActivity.class);
				intent.putExtra("Guid", bean.getGuid());
				intent.putExtra("T_News_Title", bean.getT_News_Title());
				intent.putExtra("T_News_Contents", bean.getT_News_Contents());
				intent.putExtra("T_News_Pic", bean.getT_News_Pic());
				context.startActivity(intent);
				
			}
		});
		return convertView;
	}

	final class ViewHolder {
		private ImageView iv_new_item;
		TextView tv_new_item_title, tv_new_item_cotent, tv_new_item_date,
				tv_new_item_pinglun;
	}

}
