package com.sibozn.gochat.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.sibozn.gochat.R;

/**
 * 仿汽车仪表盘(自定义view中用到了属性动画)
 * Created by Administrator on 2016/11/11.
 */

public class CarRecorderView extends View {

    private static final String TAG = "CarRecorderView";

    private int width, height;// view的宽度和高度
    private Paint blackCirclePaint;
    private int blackCirclePaintWidth = 8;//默认黑色圆环的宽度
    private Paint WhiteCirclePaint;
    private int whiteCirclePaintWidth = 48;
    private Paint linePaint;
    private int lineWidth = 15;
    private Paint unitTextPaint;
    private int unitTextSize = 48;//单位字体大小
    private Paint textValuePaint;
    private int textValueSize = 60;//
    private Paint arcAnimPaint;
    private Paint outerAnimPaint;

    private Paint outerPaint;
    private Paint interprogressPaint;
    private Paint outerSmallPaint;
    private RectF outerRectF;
    private RectF outerAnimRectF;

    private Bitmap carBitmap;

    private float value;
    private long duration = 1000;
    private long progressDelay = 350;
    private int startAngle = 140;//开始的角度
    private float plusAngle = 0;// 经过角度
    private float maxAngle = 260f;//最大的角度
    private int min = 0;
    private int max = 100;// 最大进度值
    private float lineInitValue = min;
    private float outerProgressValue = min;
    private ValueAnimator lineAnim;
    private ValueAnimator outerProgressAnim;

    public CarRecorderView(Context context) {
        this(context, null);
    }

    public CarRecorderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CarRecorderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        initAnim();

    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        arcAnimPaint = new Paint();
        arcAnimPaint.setStrokeWidth(12);
        arcAnimPaint.setColor(Color.parseColor("#1C86EE"));
        arcAnimPaint.setAntiAlias(true);
        arcAnimPaint.setStyle(Paint.Style.STROKE);

        interprogressPaint = new Paint();
        interprogressPaint.setStrokeWidth(12);
        interprogressPaint.setColor(Color.RED);
        interprogressPaint.setAntiAlias(true);
        interprogressPaint.setStyle(Paint.Style.STROKE);

        unitTextPaint = new Paint();
        unitTextPaint.setColor(Color.parseColor("#DBDBDB"));
        unitTextPaint.setTextSize(unitTextSize);
        unitTextPaint.setAntiAlias(true);

        textValuePaint = new Paint();
        textValuePaint.setColor(Color.parseColor("#DBDBDB"));
        textValuePaint.setTextSize(textValueSize);
        textValuePaint.setAntiAlias(true);

        blackCirclePaint = new Paint();
        blackCirclePaint.setColor(Color.parseColor("#1A1A1A"));
        blackCirclePaint.setStrokeWidth(blackCirclePaintWidth);
        blackCirclePaint.setStyle(Paint.Style.STROKE);
        blackCirclePaint.setAntiAlias(true);

        WhiteCirclePaint = new Paint();
        WhiteCirclePaint.setColor(Color.parseColor("#1A1A1A"));
        WhiteCirclePaint.setStrokeWidth(whiteCirclePaintWidth);
        WhiteCirclePaint.setStyle(Paint.Style.STROKE);
        WhiteCirclePaint.setAntiAlias(true);

        outerPaint = new Paint();
        outerPaint.setColor(Color.parseColor("#080808"));
        outerPaint.setStrokeWidth(whiteCirclePaintWidth - 8);
        outerPaint.setStyle(Paint.Style.STROKE);
        outerPaint.setAntiAlias(true);
        LinearGradient linearGradient = new LinearGradient(0, 0, 50, 50,
                new int[]{
                        Color.parseColor("#A4D3EE"), Color.parseColor("#9400D3"),
                        Color.parseColor("#43CD80"), Color.parseColor("#528B8B")
                },
                null, Shader.TileMode.REPEAT);
        outerPaint.setShader(linearGradient);

        float[] intervals = new float[]{8, 12};
        DashPathEffect dashPathEffect = new DashPathEffect(intervals, 0);
        outerPaint.setPathEffect(dashPathEffect);

        outerAnimPaint = new Paint();
        outerAnimPaint.setColor(Color.RED);
        outerAnimPaint.setPathEffect(dashPathEffect);
        outerAnimPaint.setStyle(Paint.Style.STROKE);
        outerAnimPaint.setStrokeWidth(whiteCirclePaintWidth);
        outerAnimPaint.setAntiAlias(true);

        outerSmallPaint = new Paint();
        outerSmallPaint.setColor(Color.RED);
        outerSmallPaint.setStrokeWidth(whiteCirclePaintWidth - 8);
        outerSmallPaint.setStyle(Paint.Style.STROKE);
        outerSmallPaint.setAntiAlias(true);

        linePaint = new Paint();
        linePaint.setColor(Color.parseColor("#20B2AA"));
        linePaint.setStrokeWidth(lineWidth);
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setAntiAlias(true);

        carBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        width = height = heightSize > widthSize ? widthSize : heightSize;
        blackCirclePaintWidth = width / 15;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.drawCircle(width / 2, height / 2, width / 2, blackCirclePaint);
        canvas.drawCircle(width / 2, height / 2, width / 2 - whiteCirclePaintWidth / 2, WhiteCirclePaint);
        canvas.drawBitmap(carBitmap, (width - carBitmap.getWidth()) / 2, (height - carBitmap.getHeight()) / 2,
                blackCirclePaint);
        float toX = width / 2 + (float) Math.cos(Math.toRadians(plusAngle + startAngle)) * (width / 2 -
                whiteCirclePaintWidth);
        float toY = height / 2 + (float) Math.sin(Math.toRadians(plusAngle + startAngle)) * (width / 2 -
                whiteCirclePaintWidth);
        canvas.drawLine(width / 2, height / 2, toX, toY, linePaint);
        canvas.drawText("km/h", width / 2 + textValuePaint.measureText("km/h")/2, (float) (height * 0.7),
                unitTextPaint);
        canvas.drawText(String.format("%.0f", lineInitValue), width / 2 - 55, (float) (height * 0.7), textValuePaint);
        outerRectF = new RectF();
        outerAnimRectF = new RectF();
        outerAnimRectF.set(blackCirclePaintWidth - 2, blackCirclePaintWidth - 1 - 1, width -
                blackCirclePaintWidth + 1 + 1, height - blackCirclePaintWidth + 1 + 1);
        outerRectF.set(blackCirclePaintWidth - 1 - 1, blackCirclePaintWidth - 1 - 1, width - blackCirclePaintWidth +
                1 + 1, height - blackCirclePaintWidth + 1 + 1);
        canvas.drawArc(outerRectF, startAngle, maxAngle, false, outerPaint);
        canvas.drawArc(outerAnimRectF, startAngle, plusAngle, false, outerAnimPaint);

        RectF rectF = new RectF(60, 60, height - 60, height - 60);
        canvas.drawArc(rectF, startAngle, maxAngle, false, interprogressPaint);

        canvas.drawArc(rectF, startAngle, plusAngle, false, arcAnimPaint);
    }

    //  更新表盘数值到指定数值
    public void updateValue(float value) {
        this.plusAngle = (maxAngle * value) / max;
        invalidate();
    }

    private void updateProgressText(float value) {
        updateValue(value);
    }

    // 开始表盘动画
    public void startAnim(float value) {
        this.value = value;
        if (value <= max && value >= min) {
            updateAnimValue();
        }
    }

    /**
     * 更新动画
     */
    private void updateAnimValue() {
        if (lineAnim != null && lineAnim != null) {
            outerProgressAnim.setFloatValues(outerProgressValue, value);
            outerProgressAnim.setDuration(duration + progressDelay);
            outerProgressAnim.start();

            lineAnim.setFloatValues(lineInitValue, value);
            lineAnim.setDuration(duration);
            lineAnim.start();
        }
    }

    /**
     * 初始化属性动画
     */
    private void initAnim() {
        outerProgressAnim = new ValueAnimator();
        outerProgressAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                Log.e(TAG, "===outerProgressAnim====>onAnimationUpdate: " + value);
                updateOuterProgressValue(value);
                outerProgressValue = value;
            }
        });
        lineAnim = new ValueAnimator();
        lineAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                Log.e(TAG, "-----lineAnim--->onAnimationUpdate: " + value);
                updateProgressText(value);
                lineInitValue = value;
            }
        });
    }

    /**
     * 更新外部进度条的值
     *
     * @param value
     */
    private void updateOuterProgressValue(float value) {
        setOuterAnimAngle(value);
    }

    public void setOuterAnimAngle(float value) {
        this.plusAngle = (maxAngle * value) / max;
        invalidate();
    }
}
