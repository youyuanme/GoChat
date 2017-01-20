package com.yibingding.haolaiwu;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Parcelable;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ybd.app.BaseActivity;
import com.yibingding.haolaiwu.domian.AdInfo;
import com.yibingding.haolaiwu.tools.ImageControl;

public class ImagePagerActivity extends BaseActivity {

	private ImageControl imgControl;
	private List<AdInfo> adinfos;

	@Override
	public void onCreateThisActivity() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_image_page);
	}

	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		imgControl = (ImageControl) findViewById(R.id.common_imageview_imageControl1);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		adinfos = getIntent().getParcelableArrayListExtra("adinfos");
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
//		init();
	}

//	private void init() {
//		// tvTitle.setText("图片测试");
//		// 这里可以为imgcontrol的图片路径动态赋值
//		// ............
//
//		ImageLoader.getInstance().displayImage(uri, imageView);
//		imgControl.setImageBitmap();
//
//		Bitmap bmp;
//		if (imgControl.getDrawingCache() != null) {
//			bmp = Bitmap.createBitmap(imgControl.getDrawingCache());
//		} else {
//			bmp = ((BitmapDrawable) imgControl.getDrawable()).getBitmap();
//		}
//		Rect frame = new Rect();
//		getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
//		int statusBarHeight = frame.top;
//		int screenW = this.getWindowManager().getDefaultDisplay().getWidth();
//		int screenH = this.getWindowManager().getDefaultDisplay().getHeight()
//				- statusBarHeight;
//		if (bmp != null) {
//			imgControl.imageInit(bmp, screenW, screenH, statusBarHeight,
//					new ICustomMethod() {
//
//						@Override
//						public void customMethod(Boolean currentStatus) {
//							// 当图片处于放大或缩小状态时，控制标题是否显示
//							// if (currentStatus) {
//							// llTitle.setVisibility(View.GONE);
//							// } else {
//							// llTitle.setVisibility(View.VISIBLE);
//							// }
//						}
//					});
//		} else {
//			Toast.makeText(this, "图片加载失败，请稍候再试！", Toast.LENGTH_SHORT).show();
//		}
//
//	}
}
