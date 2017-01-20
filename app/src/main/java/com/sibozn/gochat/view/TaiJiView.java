package com.sibozn.gochat.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

/**
 * 自定义view实现太极图效果
 * http://blog.csdn.net/coderinchina/article/details/53083030
 * Created by Administrator on 2016/11/8.
 */

public class TaiJiView extends View implements View.OnClickListener {
    private RotateAnimation rotateAnimation;
    private int width = 360;
    private int height = 360;
    private boolean isRotate = true;
    private int padding = 5;
    private Paint mPaint;
    private RectF mRectf;
    private RectF blackHalfRect;
    private RectF whiteHalfRect;


    public TaiJiView(Context context) {
        this(context, null);
    }

    public TaiJiView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TaiJiView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        initAnim();
        initValueAnim();
        setOnClickListener(this);
    }

    /**
     * 属性动画实现太极转动
     */
    private void initValueAnim() {
    }

//    public TaiJiView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, RotateAnimation
//            rotateAnimation) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        this.rotateAnimation = rotateAnimation;
//    }

    /**
     * 动画
     */
    private void initAnim() {
        rotateAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setRepeatCount(-1);
        rotateAnimation.setFillAfter(false);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setInterpolator(new LinearInterpolator());// 不停顿
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(5);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width, height);
        mRectf = new RectF(0, 0, width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCirCle(canvas);
        drawHalfCirCle(canvas);
        drawSmallCirCle(canvas);

    }

    /**
     * 绘制两个小圆点
     *
     * @param canvas
     */
    private void drawSmallCirCle(Canvas canvas) {
        //画白色的小圆
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width / 2, width / 4, 20, mPaint);
        //画黑色的小圆
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width / 2, width / 4 * 3, 20, mPaint);
    }

    /**
     * 绘制两个半圆，一个黑色一个白色
     *
     * @param canvas
     */
    private void drawHalfCirCle(Canvas canvas) {
        // 画上面黑色半圆
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLACK);
        blackHalfRect = new RectF(width / 4, 0, width / 2 + width / 4, width / 2);
        canvas.drawArc(blackHalfRect, 270, 180, true, mPaint);

        //画下面的白色半圆
        mPaint.setColor(Color.WHITE);
        whiteHalfRect = new RectF(width / 4, width / 2, width / 2 + width / 4, width);
        canvas.drawArc(whiteHalfRect, 270, -180, true, mPaint);
    }

    /**
     * 画两个简单的黑白半圆
     */
    private void drawCirCle(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);
        canvas.drawArc(mRectf, 270, 180, true, mPaint);
        mPaint.setColor(Color.BLACK);
        canvas.drawArc(mRectf, 270, -180, true, mPaint);
    }

    @Override
    public void onClick(View v) {
        if (isRotate) {
            startAnimation(rotateAnimation);
            isRotate = false;
        } else {
            rotateAnimation.cancel();
            isRotate = true;
        }

    }
}
