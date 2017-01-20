package com.yibingding.haolaiwu.domian;

import android.os.Parcel;
import android.os.Parcelable;

public class AdInfo implements Parcelable {
	public String guid;
	public String imgUrl;
	public String url;
	public String title;
	public String contents;

	/**
	 * 序列化实体类
	 */
	public static final Parcelable.Creator<AdInfo> CREATOR = new Creator<AdInfo>() {
		public AdInfo createFromParcel(Parcel source) {
			AdInfo adInfo = new AdInfo();
			adInfo.guid = source.readString();
			adInfo.imgUrl = source.readString();
			adInfo.url = source.readString();
			adInfo.title = source.readString();
			adInfo.contents = source.readString();
			return adInfo;
		}

		public AdInfo[] newArray(int size) {
			return new AdInfo[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(guid);
		dest.writeString(imgUrl);
		dest.writeString(url);
		dest.writeString(title);
		dest.writeString(contents);
	}
}
