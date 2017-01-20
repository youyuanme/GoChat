package com.yibingding.haolaiwu.view;

import com.yibingding.haolaiwu.tools.UserImageTools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CornerImageView extends ImageView {
	 private Bitmap currentBitmap; // 当前显示的bitmap
	 private int radius=14;
	public CornerImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	 private void initCurrentBitmap() {
	        Bitmap temp = null;
	        Drawable drawable = getDrawable();
	        if (drawable instanceof BitmapDrawable) {
	            temp = ((BitmapDrawable) drawable).getBitmap();
	        }
	        if (temp != null) {
	            currentBitmap = temp;
	        }
	    }
	 @Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
//		super.onDraw(canvas);
		 initCurrentBitmap();
		 canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
		 currentBitmap = UserImageTools.createFramedPhoto(currentBitmap.getWidth(), currentBitmap.getHeight(), currentBitmap,radius);
		 Matrix matrix = new Matrix();
		 matrix.setScale(canvas.getWidth()*1.0f/currentBitmap.getWidth(), canvas.getHeight()*1.0f/currentBitmap.getHeight());
		 canvas.drawBitmap(currentBitmap, matrix, new Paint());
	}
	public int getRadius() {
		return radius;
	}
	public void setRadius(int radius) {
		this.radius = radius;
	}
}
