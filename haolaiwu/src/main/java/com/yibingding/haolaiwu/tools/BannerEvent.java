package com.yibingding.haolaiwu.tools;

import java.util.List;

import android.widget.ImageView;

public interface BannerEvent {

	<T> void clickPic(List<T> list, int position, ImageView iv);

	<T> void disPlayBannerImages(List<T> list, int position, ImageView iv);

}
