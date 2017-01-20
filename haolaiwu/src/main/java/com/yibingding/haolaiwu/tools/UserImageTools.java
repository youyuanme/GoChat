package com.yibingding.haolaiwu.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class UserImageTools {
	
/**
 *创建一个圆角矩形图片
 * @param x
 * @param y
 * @param image
 * @param outerRadiusRat
 * @return
 */
	public static Bitmap createFramedPhoto(int x, int y, Bitmap image, float outerRadiusRat) {
        //根据源文件新建一个darwable对象
        Drawable imageDrawable = new BitmapDrawable(image);

        // 新建一个新的输出图片
        Bitmap output = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
       canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
        // 新建一个矩形
        RectF outerRect = new RectF(0, 0, x, y);

        // 产生一个红色的圆角矩形
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        canvas.drawRoundRect(outerRect, outerRadiusRat, outerRadiusRat, paint);

        // 将源图片绘制到这个圆角矩形上
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        imageDrawable.setBounds(0, 0, x, y);
        canvas.saveLayer(outerRect, paint, Canvas.ALL_SAVE_FLAG);
        imageDrawable.draw(canvas);
        canvas.restore();

        return output;
    }
	/**
	 * 创建椭圆形图片
	 * @param source
	 * @param min
	 * @return
	 */
	public static Bitmap createCircleImage(Bitmap source, int min)  
    {  
        final Paint paint = new Paint();  
        paint.setAntiAlias(true);  
        Bitmap target = Bitmap.createBitmap(min, min, Config.ARGB_4444);  
        /** 
         * 产生一个同样大小的画布 
         */  
        Canvas canvas = new Canvas(target);  
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
        /** 
         * 首先绘制圆形 
         */  
        canvas.drawCircle(min / 2, min / 2, min / 2, paint);  
        /** 
         * 使用SRC_IN 
         */  
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));  
        /** 
         * 绘制图片 
         */  
        canvas.drawBitmap(source, 0, 0, paint);  
        return target;  
    }
	public static boolean saveBitmap(File f,Bitmap bitmap) {
		  if (f.exists()) {
		   f.delete();
		  }
		  try {
	            f.createNewFile();
	            FileOutputStream fos = new FileOutputStream(f);
	            bitmap.compress(CompressFormat.JPEG, 50, fos);
	            fos.flush();
	            fos.close();
	            return true;
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
		  return false;
		 }
}
