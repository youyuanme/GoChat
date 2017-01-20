package com.yibingding.haolaiwu.adapter;

import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yibingding.haolaiwu.R;
import com.yibingding.haolaiwu.User_MyAppraisalActivity;
import com.yibingding.haolaiwu.User_MyAppraisalDetailsActivity;
import com.yibingding.haolaiwu.domian.Appraisal;
import com.yibingding.haolaiwu.houseparopertynew.HousePropertyNewDetailsActivity;
import com.yibingding.haolaiwu.tools.Constants;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class User_MyAppraisalAdapter extends BaseAdapter {

	private List<Appraisal> list;
	private LayoutInflater inflater;
	private Context cxt;

	public User_MyAppraisalAdapter(Context cxt, List<Appraisal> list) {
		this.list = list;
		inflater = LayoutInflater.from(cxt);
		this.cxt = cxt;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Appraisal getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder holder;
		if (arg1 == null) {
			holder = new ViewHolder();
			arg1 = inflater.inflate(R.layout.user_appraisal_talk, null);
			holder.newsTitle = (TextView) arg1.findViewById(R.id.newstitle);
			holder.newsTime = (TextView) arg1.findViewById(R.id.newstime);
			holder.nickname = (TextView) arg1.findViewById(R.id.nickname);
			holder.time = (TextView) arg1.findViewById(R.id.time);
			holder.content = (TextView) arg1.findViewById(R.id.content);
			holder.num = (TextView) arg1.findViewById(R.id.num);
			holder.newimg = (ImageView) arg1.findViewById(R.id.newsimg);
			holder.avatar = (ImageView) arg1.findViewById(R.id.avatar);
			arg1.setTag(holder);
		}
		holder = (ViewHolder) arg1.getTag();
		final Appraisal a = getItem(arg0);
		holder.newsTitle.setText(a.getNewsTitle());
		holder.newsTime.setText(a.getNewsDate());
		if (a.getFromNickName().equals("")) {
			holder.nickname.setText(a.getFromLoginId());
		} else {
			holder.nickname.setText(a.getFromNickName());
		}
		holder.time.setText(a.getT_Talk_FromDate());
		holder.num.setText(a.getStrReply().size() + "");
		holder.num.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(cxt,
						User_MyAppraisalDetailsActivity.class);
				intent.putExtra("guid", a.getGuid());
				intent.putExtra("data", a.getStrReply().toJSONString());
				cxt.startActivity(intent);
			}
		});
		holder.content.setText(a.getT_Talk_FromContent());

		String imageString = a.getNewsPic();
		String displayImage = "";
		if (!TextUtils.isEmpty(imageString)) {
			displayImage = Constants.IMAGE_URL + imageString;
		}
		ImageLoader.getInstance().displayImage(displayImage, holder.newimg);

		String imageString1 = a.getFromPic();
		String displayImage1 = "";
		if (!TextUtils.isEmpty(imageString1)) {
			displayImage1 = Constants.IMAGE_URL + imageString1;
		}
		ImageLoader.getInstance().displayImage(displayImage1, holder.avatar);
		return arg1;
	}

	private final class ViewHolder {
		private TextView newsTitle, newsTime, nickname, num, time, content;
		private ImageView newimg, avatar;
	}

}
