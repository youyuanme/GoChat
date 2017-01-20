package com.sibozn.gochat.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

/**
 * Created by Administrator on 2016/12/26.
 * ClearEditTextView通过扩展EditText，添加了右边清除按钮、输入数据不合法时可左右抖动的功能。
 */

public class ClearEditTextView extends EditText {
    public ClearEditTextView(Context context) {
        this(context, null);
    }

    public ClearEditTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearEditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    /**
     * EditText抖动
     *
     * @param counts
     * @return
     */
    public void startShake(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(500);
        startAnimation(translateAnimation);
    }
}
