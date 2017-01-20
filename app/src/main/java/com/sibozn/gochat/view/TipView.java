package com.sibozn.gochat.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sibozn.gochat.R;

import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016/11/14.
 */

public class TipView extends FrameLayout {

    private static final String TAG = "TipView";
    /**
     * 动画间隔
     */
    private static final int ANIM_DELAYED_MILLIONS = 3 * 1000;
    /**
     * 动画持续时长
     */
    private static final int ANIM_DURATION = 1 * 1000;
    /**
     * 默认字体颜色
     */
    private static final String DEFAULT_TEXT_COLOR = "#2F4F4F";
    /**
     * 默认字体大小  dp
     */
    private static final int DEFAULT_TEXT_SIZE = 16;
    private Animation anim_out, anim_in;
    private TextView tv_tip_out, tv_tip_in;
    /**
     * 循环播放的消息
     */
    private List<String> tipList;
    /**
     * 当前轮播到的消息索引
     */
    private int curTipIndex = 0;
    private long lastTimeMillis;
    private Drawable head_boy, head_girl;
    /**
     * 点击监听
     */
    private OnMyLooperClickListener listener;

    public TipView(Context context) {
        this(context, null);
    }

    public TipView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTipFrame();
        initAnimation();
    }

    private void initTipFrame() {
        head_boy = loadDrawable(R.mipmap.ic_launcher);
        head_girl = loadDrawable(R.mipmap.ic_launcher);
        tv_tip_out = newTextView();
        tv_tip_in = newTextView();
        addView(tv_tip_in);
        addView(tv_tip_out);
    }

    /**
     * 设置要循环播放的信息
     *
     * @param tipList
     */
    public void setTipList(List<String> tipList) {
        this.tipList = tipList;
        curTipIndex = 0;
        updateTip(tv_tip_out);
        updateTipAndPlayAnimation();
    }

    /**
     * 设置循环播放的消息点击事件
     *
     * @param listener
     */
    public void setOnMyLooperClickListener(OnMyLooperClickListener listener) {
        this.listener = listener;
    }

    private void updateTip(TextView tipView) {
        if (new Random().nextBoolean()) {
            tipView.setCompoundDrawables(head_boy, null, null, null);
        } else {
            tipView.setCompoundDrawables(head_girl, null, null, null);
        }
        String tip = getNextTip();
        if (!TextUtils.isEmpty(tip)) {
            tipView.setText(tip);
        }
    }

    /**
     * 获取下一条消息
     *
     * @return
     */
    private String getNextTip() {
        if (isListEmpty(tipList)) return null;
        if (curTipIndex > tipList.size()) {
            curTipIndex = 0;
        }
        return tipList.get(curTipIndex++ % tipList.size());
    }

    private void initAnimation() {
        anim_out = newAnimation(0, -1);
        anim_in = newAnimation(1, 0);
        anim_in.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                updateTipAndPlayAnimationWithCheck();
            }
        });
    }

    private void updateTipAndPlayAnimation() {
        if (curTipIndex % 2 == 0) {
            updateTip(tv_tip_out);
            tv_tip_in.startAnimation(anim_out);
            tv_tip_out.startAnimation(anim_in);
            this.bringChildToFront(tv_tip_in);
        } else {
            updateTip(tv_tip_in);
            tv_tip_out.startAnimation(anim_out);
            tv_tip_in.startAnimation(anim_in);
            this.bringChildToFront(tv_tip_out);
        }
    }

    private void updateTipAndPlayAnimationWithCheck() {
        if (System.currentTimeMillis() - lastTimeMillis < 1000) {
            return;
        }
        lastTimeMillis = System.currentTimeMillis();
        updateTipAndPlayAnimation();
    }


    private TextView newTextView() {
        TextView textView = new TextView(getContext());
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        textView.setLayoutParams(lp);
        textView.setGravity(Gravity.CENTER);
        textView.setCompoundDrawablePadding(10);
        //textView.setCompoundDrawables(head_boy, null, null, null);
        textView.setLines(2);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setTextColor(Color.parseColor(DEFAULT_TEXT_COLOR));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TEXT_SIZE);
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onMyLooperClickListener(curTipIndex - 2);
                }
            }
        });
        return textView;
    }

    private Animation newAnimation(float fromYValue, float toYValue) {
        Animation anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, fromYValue, Animation.RELATIVE_TO_SELF, toYValue);
        anim.setDuration(ANIM_DURATION);
        anim.setStartOffset(ANIM_DELAYED_MILLIONS);
        anim.setInterpolator(new DecelerateInterpolator());
        return anim;
    }

    /**
     * 将资源图片转换为Drawable对象
     *
     * @param ResId
     * @return
     */
    private Drawable loadDrawable(int ResId) {
        Drawable drawable = getResources().getDrawable(ResId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth() - 10, drawable.getMinimumHeight() - 10);
        return drawable;
    }

    /**
     * list是否为空
     *
     * @param list
     * @return true 如果是空
     */
    public static boolean isListEmpty(List list) {
        return list == null || list.isEmpty();
    }

    public interface OnMyLooperClickListener {
        void onMyLooperClickListener(int index);
    }
}
