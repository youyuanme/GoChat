package com.sibozn.gochat.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.View;

/**
 * 圆角图片控件
 * Created by Administrator on 2016/11/17.
 */

public class CustomImageView extends View {

    private Paint mPaint;
    private ShapeDrawable mShapeDrawable;
    private Bitmap shaderBitmap;
    private int bitmapWidth, bitmapHeight;
    private BitmapShader mBitmapShader;


    public CustomImageView(Context context) {
        this(context, null);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mShapeDrawable = new ShapeDrawable(new OvalShape());
        mShapeDrawable.getPaint().setShader(mBitmapShader);
        //这是设置一个矩形区域  表示这个drawable所绘制的区域
        mShapeDrawable.setBounds(20, 20, bitmapWidth, bitmapHeight);
        mShapeDrawable.draw(canvas);
    }

    /**
     * 设置图片资源
     *
     * @param resId
     */
    public void setImageRes(int resId) {
        shaderBitmap = BitmapFactory.decodeResource(getContext().getResources(), resId);
        bitmapWidth = shaderBitmap.getWidth();
        bitmapHeight = shaderBitmap.getHeight();
        mBitmapShader = new BitmapShader(shaderBitmap, Shader.TileMode.MIRROR, Shader.TileMode.REPEAT);
    }
}
