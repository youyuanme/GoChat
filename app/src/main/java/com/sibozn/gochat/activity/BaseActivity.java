package com.sibozn.gochat.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {
    protected String TAG = this.getClass().getSimpleName();
    protected Context mContext;

    //子类具体实现处理逻辑
    public abstract int getContentViewId();

    protected abstract void initAllMembersView(Bundle savedInstanceState);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(getContentViewId());
        ButterKnife.bind(this);
        initAllMembersView(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
