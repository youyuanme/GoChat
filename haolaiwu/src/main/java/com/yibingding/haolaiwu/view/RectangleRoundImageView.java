package com.yibingding.haolaiwu.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RectangleRoundImageView extends ImageView {

	private Bitmap currentBitmap; // 当前显示的bitmap
	private int outerRadiusRat = 50;

	public RectangleRoundImageView(Context context) {
		super(context);
	}

	public RectangleRoundImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public RectangleRoundImageView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
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
		initCurrentBitmap();
		if (currentBitmap == null || getWidth() == 0 || getHeight() == 0) {
			return;
		}
		// this.measure(0, 0);
		int width = getWidth();
		int height = getHeight();
		// 根据源文件新建一个darwable对象
		Drawable imageDrawable = new BitmapDrawable(currentBitmap);
		// 新建一个新的输出图片
		Bitmap output = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		// Canvas NewCanvas = new Canvas(output);
		canvas.setBitmap(output);
		// 新建一个矩形
		RectF outerRect = new RectF(0, 0, width, height);
		// 产生一个红色的圆角矩形
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.RED);
		canvas.drawRoundRect(outerRect, outerRadiusRat, outerRadiusRat, paint);
		// 将源图片绘制到这个圆角矩形上
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		imageDrawable.setBounds(0, 0, width, height);
		canvas.saveLayer(outerRect, paint, Canvas.ALL_SAVE_FLAG);
		imageDrawable.draw(canvas);
		canvas.restore();
		
		super.onDraw(canvas);
	}
}
