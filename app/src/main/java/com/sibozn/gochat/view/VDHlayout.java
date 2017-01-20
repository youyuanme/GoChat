package com.sibozn.gochat.view;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * http://blog.csdn.net/lmj623565791/article/details/46858663
 * Created by Administrator on 2017/1/19.
 */

public class VDHlayout extends LinearLayout {

    private static final String TAG = "VDHlayout";
    private ViewDragHelper viewDragHelper;
    private View mDragView;// 第一个子view
    private View mAutoBackView; //第2个子view
    private View mEdgeTrackerView;//第3个子view
    private Point mAutoBackOriginPos = new Point();

    public VDHlayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        viewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                Log.e(TAG, "tryCaptureView--" + "pointerId: " + pointerId);
                //mEdgeTrackerView禁止直接移动
                return child == mDragView || child == mAutoBackView;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
//                final int leftBound = getPaddingLeft();
//                final int rightBound = getWidth() - child.getWidth() - leftBound;
//                final int newLeft = Math.min(Math.max(left, leftBound), rightBound);
//                return newLeft;
                //return super.clampViewPositionHorizontal(child, left, dx);
                Log.e(TAG, "clampViewPositionHorizontal: ");
                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                Log.e(TAG, "clampViewPositionVertical: ");
//                final int topBound = getPaddingTop();
//                final int bottomBound = getHeight() - child.getHeight() - topBound;
//                final int newTop = Math.min(Math.max(top, topBound), bottomBound);
//                return newTop;
                //return super.clampViewPositionVertical(child, top, dy);
                return top;
            }

            /*
            *到此，我们已经介绍了Callback中常用的回调方法了，当然还有一些方法没有介绍
            * ,接下来我们修改下我们的布局文件，我们把我们的TextView全部加上clickable=true
            * ，意思就是子View可以消耗事件。再次运行，你会发现本来可以拖动的View不动了
            * ，（如果有拿Button测试的兄弟应该已经发现这个问题了，我希望你看到这了，而不是已经提问了,哈~）。
            * 原因是什么呢？主要是因为，如果子View不消耗事件，那么整个手势（DOWN-MOVE*-UP）都是直接进入onTouchEvent
            * ，在onTouchEvent的DOWN的时候就确定了captureView。如果消耗事件，那么就会先走onInterceptTouchEvent方法
            * ，判断是否可以捕获，而在判断的过程中会去判断另外两个回调的方法：getViewHorizontalDragRange
            * 和getViewVerticalDragRange，只有这两个方法返回大于0的值才能正常的捕获。
            * 所以，如果你用Button测试，或者给TextView添加了clickable = true ，都记得重写下面这两个方法：
             */
            @Override
            public int getViewHorizontalDragRange(View child) {
                return getMeasuredWidth() - child.getMeasuredWidth();
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return getMeasuredHeight() - child.getMeasuredHeight();
            }

            //mAutoBackView手指释放时可以自动回去
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                Log.e(TAG, "onViewReleased--" + "xvel: " + xvel + "yvel:" + yvel);
                //mAutoBackView手指释放时可以自动回去
                if (releasedChild == mAutoBackView) {
                    viewDragHelper.settleCapturedViewAt(mAutoBackOriginPos.x, mAutoBackOriginPos.y);
                    invalidate();
                }
            }

            //在边界拖动时回调
            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                Log.e(TAG, "onEdgeDragStarted---" + "edgeFlags: " + edgeFlags
                        + "pointerId:" + pointerId);
                viewDragHelper.captureChildView(mEdgeTrackerView, pointerId);
            }
        });
        viewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e(TAG, "onInterceptTouchEvent: ");
        boolean b = viewDragHelper.shouldInterceptTouchEvent(ev);
        Log.e(TAG, "onInterceptTouchEvent: " + b);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        Log.e(TAG, "onTouchEvent: ");
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        Log.e(TAG, "computeScroll: ");
        if (viewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.e(TAG, "onLayout---" + "changed:" + changed
                + " l:" + l + " t:" + t + " r:" + r + " b:" + b);
        mAutoBackOriginPos.x = mAutoBackView.getLeft();
        mAutoBackOriginPos.y = mAutoBackView.getTop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.e(TAG, "onFinishInflate: ");
        mDragView = getChildAt(0);
        mAutoBackView = getChildAt(1);
        mEdgeTrackerView = getChildAt(2);
    }
}
