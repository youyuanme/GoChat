package com.sibozn.gochat.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 自定义计时器控件
 * 第一步：画一个简单的圆
 * 第二步：绘制刻度
 * 第三步：绘制时,分,表指针
 * 第四步：绘制当前时间文字
 * 第五步：实现时间动态显示
 * http://blog.csdn.net/coderinchina/article/details/53056262
 * Created by Administrator on 2016/11/9.
 */

public class ClockView extends ImageView {

    private static final int UP_DATA_CODE = 0x1000;
    private static final String TAG = "ClockView";
    private Paint mPaint;
    private int widhth = 600;//控件的默认宽度
    private int height = 600;//控件的默认高度
    private int padding = 5;
    private Calendar mCalendar;
    private SimpleDateFormat simpleDateFormat;
    private int mHour, mMinuate, mSecond;// 小时，分钟，秒
    private float mDegrees;// 因为圆是360度，我们有12个刻度，所以就是360/12
    private int mHourLine, mMinuateLine, mSecondLine;// 时针线，分针线，秒针线
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UP_DATA_CODE:
                    invalidate();
            }
        }
    };


    public ClockView(Context context) {
        this(context, null);
    }

    public ClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();// 初始化画笔
        simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(3);
        //mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        widhth = measureWidth(widthMeasureSpec);
        height = measureHeight(heightMeasureSpec);
        //设置当前view宽和高
        setMeasuredDimension(widhth, height);
        mHourLine = (int) (widhth / 2 * 0.6);// 时针线长
        mMinuateLine = (int) (widhth / 2 * 0.7);//分针线长
        mSecondLine = (int) (widhth / 2 * 0.9);// 秒针线长
    }

    /**
     * 测量view的高
     *
     * @param heightMeasureSpec
     * @return
     */
    private int measureHeight(int heightMeasureSpec) {
        int result = widhth;
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    /**
     * 测量view的宽
     *
     * @return
     */
    private int measureWidth(int widthMeasureSpec) {
        int result = height;
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCircle(canvas);// 画表盘
        drawScale(canvas);// 画刻度
        drawScaleText(canvas);//绘制刻度文字
        drawCenterCircle(canvas);//在圆中心画圆点
        drawPointer(canvas);// 画指针
        drawStr(canvas);//画时间字体
        mHandler.sendEmptyMessageDelayed(UP_DATA_CODE, 1000);
    }

    /**
     * 绘制刻度文字
     *
     * @param canvas
     */
    private void drawScaleText(Canvas canvas) {
        mPaint.setTextSize(48);
        mPaint.setColor(Color.RED);
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0:
                    int Str12W = (int) mPaint.measureText("12");
                    canvas.drawText("12", widhth / 2 - Str12W / 2, (float) (height * 0.15), mPaint);
                    break;
                case 1:
                    int Str3W = (int) mPaint.measureText("3");
                    canvas.drawText("3", (float) (widhth * 0.85), height / 2 + Str3W / 2, mPaint);
                    break;
                case 2:
                    int Str6W = (int) mPaint.measureText("6");
                    canvas.drawText("6", widhth / 2 - Str6W / 2, (float) (height * 0.85), mPaint);
                    break;
                case 3:
                    int Str9W = (int) mPaint.measureText("9");
                    canvas.drawText("9", (float) (widhth * 0.15), height / 2 + Str9W / 2, mPaint);
                    break;
            }
        }
    }

    /**
     * 画时间字体
     *
     * @param canvas
     */
    private void drawStr(Canvas canvas) {
        mPaint.setTextSize(48);
        mPaint.setColor(Color.BLACK);
        String timeStr = simpleDateFormat.format(mCalendar.getTime());
        //Log.e(TAG, "initCurrentTime: " + timeStr);
        int timeStrW = (int) mPaint.measureText(timeStr);
        canvas.drawText(timeStr, widhth / 2 - timeStrW / 2, widhth / 2 + widhth / 6, mPaint);
    }

    /**
     * 在圆中心画圆点
     *
     * @param canvas
     */
    private void drawCenterCircle(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLACK);
        canvas.drawCircle(widhth / 2, widhth / 2, 15, mPaint);
    }

    /**
     * 绘制时 分 秒指针
     *
     * @param canvas
     */
    private void drawPointer(Canvas canvas) {
        getCurrentTime();// 获取当前时间
        //小时的旋转度
        mDegrees = mHour * 30 + mMinuate / 2;
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(8);
        canvas.save();
        canvas.rotate(mDegrees, widhth / 2, height / 2);
        canvas.drawLine(widhth / 2, height / 2 + height / 10, widhth / 2, widhth / 2 - mHourLine, mPaint);
        canvas.restore();
        // 分钟
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(6);
        mDegrees = mMinuate * 6 + mSecond / 10;
        canvas.save();
        canvas.rotate(mDegrees, widhth / 2, widhth / 2);
        canvas.drawLine(widhth / 2, height / 2 + height / 8, widhth / 2, widhth / 2 - mMinuateLine, mPaint);
        canvas.restore();
        // 绘制秒表针
        mPaint.setStrokeWidth(4);
        mPaint.setColor(Color.GRAY);
        mDegrees = mSecond * 6;
        canvas.save();
        canvas.rotate(mDegrees, widhth / 2, widhth / 2);
        canvas.drawLine(widhth / 2, height / 2 + height / 6, widhth / 2, widhth / 2 - mSecondLine, mPaint);
        canvas.restore();
    }

    /**
     * 绘制刻度
     *
     * @param canvas
     */
    private void drawScale(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < 60; i++) {
            if (i % 15 == 0) {// 12 3 6 9 对应的刻度最粗最长
                mPaint.setStrokeWidth(5);
                canvas.drawLine(widhth / 2, padding, widhth / 2, widhth / 10, mPaint);
                canvas.rotate(6, widhth / 2, height / 2);
                continue;
            }
            if (i % 5 == 0) {// 对应的刻度较粗较长
                mPaint.setStrokeWidth(3);
                canvas.drawLine(widhth / 2, padding, widhth / 2, widhth / 12, mPaint);
            } else {// 对应的刻度最短最细
                mPaint.setStrokeWidth(1);
                canvas.drawLine(widhth / 2, padding, widhth / 2, widhth / 16, mPaint);
            }
            canvas.rotate(6, widhth / 2, height / 2);
        }
    }

    /**
     * 绘制圆
     *
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        canvas.drawCircle(widhth / 2, height / 2, widhth / 2 - padding, mPaint);
    }

    /**
     * 获取当前时间
     */
    private void getCurrentTime() {
        mCalendar = Calendar.getInstance();// 获取当前时间
        mHour = mCalendar.get(Calendar.HOUR);
        mMinuate = mCalendar.get(Calendar.MINUTE);
        mSecond = mCalendar.get(Calendar.SECOND);
    }
}
