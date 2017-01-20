package com.yibingding.haolaiwu.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ybd.app.MyBaseAdapter;
import com.ybd.app.views.CircularImage;
import com.ybd.app.views.RoundImageView;
import com.yibingding.haolaiwu.R;
import com.yibingding.haolaiwu.domian.CommentBean;
import com.yibingding.haolaiwu.domian.NewBean;
import com.yibingding.haolaiwu.tools.Constants;

public class Comment_Adapter<T> extends MyBaseAdapter<T> {

	private ArrayList<CommentBean> arrayList;

	public Comment_Adapter(Context context, List<T> data, Object object) {
		super(context, data, object);
		arrayList = (ArrayList<CommentBean>) data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.new_details_pinlun, null);
			holder.news_details_image = (RoundImageView) convertView
					.findViewById(R.id.news_details_image);
			holder.tv_new_details_name = (TextView) convertView
					.findViewById(R.id.tv_new_details_name);
			holder.tv_news_details_time = (TextView) convertView
					.findViewById(R.id.tv_news_details_time);
			holder.tv_news_details_huifu = (TextView) convertView
					.findViewById(R.id.tv_news_details_huifu);
			holder.tv_news_details_content = (TextView) convertView
					.findViewById(R.id.tv_news_details_content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();

		}
		CommentBean bean = arrayList.get(position);

		String imageString = bean.getFromPic();
		String displayImage = "";
		if (!TextUtils.isEmpty(imageString)) {
			displayImage = Constants.IMAGE_URL + bean.getFromPic();
		}
		ImageLoader.getInstance().displayImage(displayImage,
				holder.news_details_image);
		holder.tv_new_details_name.setText(bean.getFromNickName());
		holder.tv_news_details_time.setText(bean.getT_Talk_FromDate());
		holder.tv_news_details_huifu.setVisibility(View.GONE);
		holder.tv_news_details_content.setText(bean.getT_Talk_FromContent());
		return convertView;
	}

	private final class ViewHolder {
		private RoundImageView news_details_image;
		private TextView tv_new_details_name, tv_news_details_time,
				tv_news_details_huifu, tv_news_details_content;
	}

}
