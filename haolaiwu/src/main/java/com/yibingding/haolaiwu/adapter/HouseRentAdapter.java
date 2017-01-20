package com.yibingding.haolaiwu.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ybd.app.MyBaseAdapter;
import com.yibingding.haolaiwu.HouseDetailsActivity;
import com.yibingding.haolaiwu.R;
import com.yibingding.haolaiwu.activity.BuildingInfoActivity;
import com.yibingding.haolaiwu.domian.HouseRentItem;
import com.yibingding.haolaiwu.tools.Constants;

public class HouseRentAdapter extends MyBaseAdapter {

	public HouseRentAdapter(Context context, List data, Object object) {
		super(context, data, object);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_house_rent, null);
			holder.imageViewPic = (ImageView) convertView
					.findViewById(R.id.imageViewPic);
			holder.textViewTitle = (TextView) convertView
					.findViewById(R.id.textViewTitle);
			holder.textViewSth = (TextView) convertView
					.findViewById(R.id.textViewSth);
			holder.textViewRooms = (TextView) convertView
					.findViewById(R.id.textViewRooms);
			holder.textViewMoney = (TextView) convertView
					.findViewById(R.id.textViewMoney);
			holder.textViewAddress = (TextView) convertView
					.findViewById(R.id.textViewAddress);
			holder.textViewTip1 = (TextView) convertView
					.findViewById(R.id.textViewTip1);
			holder.textViewTip2 = (TextView) convertView
					.findViewById(R.id.textViewTip2);
			holder.textViewTip3 = (TextView) convertView
					.findViewById(R.id.textViewTip3);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		List<HouseRentItem> houseRentItems = data;
		final HouseRentItem houseRentItem = houseRentItems.get(position);

		String imageString = houseRentItem.getHouseRentItemPic();
		String displayImage = "";
		if (!TextUtils.isEmpty(imageString)) {
			displayImage = Constants.IMAGE_URL + imageString;
		}
		ImageLoader.getInstance().displayImage(displayImage,
				holder.imageViewPic);

		holder.textViewTitle.setText(houseRentItem.getHouseRentItemTitle());
		holder.textViewSth.setText(houseRentItem.getHouseRentItemSth());
		holder.textViewRooms.setText(houseRentItem.getHouseRentItemRooms());
		holder.textViewMoney.setText(houseRentItem.getHouseRentItemMoney());
		holder.textViewAddress.setText(houseRentItem.getHouseRentItemAddress());
		String tip1 = houseRentItem.getHouseRentItemTip1();
		if (!"".equals(tip1)) {
			holder.textViewTip1.setText(tip1);
		} else {
			holder.textViewTip1.setVisibility(View.INVISIBLE);
		}
		String tip2 = houseRentItem.getHouseRentItemTip2();
		if (!"".equals(tip2)) {
			holder.textViewTip2.setText(tip2);
		} else {
			holder.textViewTip2.setVisibility(View.INVISIBLE);
		}
		String tip3 = houseRentItem.getHouseRentItemTip3();
		if (!"".equals(tip3)) {
			holder.textViewTip3.setText(tip3);
		} else {
			holder.textViewTip3.setVisibility(View.INVISIBLE);
		}

		final String whitch = (String) object;

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 楼盘
				if (Constants.BUILDING.equals(whitch)) {
					context.startActivity(new Intent(context,
							BuildingInfoActivity.class).putExtra("id",
							houseRentItem.getHouseRentItemId()));
				}

				// 二手房
				if (Constants.OLD_HOUSE.equals(whitch)) {
					Intent intent = new Intent(context,
							HouseDetailsActivity.class);
					intent.putExtra("whereFrom", Constants.OLD_HOUSE);
					intent.putExtra("id", houseRentItem.getHouseRentItemId());
					// intent.putExtra("item", houseRentItem);
					context.startActivity(intent);
				}

				// 房屋租赁
				if (Constants.HOUSE_RENT.equals(whitch)) {
					Intent intent = new Intent(context,
							HouseDetailsActivity.class);
					intent.putExtra("whereFrom", Constants.HOUSE_RENT);
					intent.putExtra("id", houseRentItem.getHouseRentItemId());
					// intent.putExtra("item", houseRentItem);
					context.startActivity(intent);
				}

			}
		});

		return convertView;
	}

	final class ViewHolder {
		ImageView imageViewPic;
		TextView textViewTitle, textViewSth, textViewRooms, textViewMoney,
				textViewAddress, textViewTip1, textViewTip2, textViewTip3;
	}

}
