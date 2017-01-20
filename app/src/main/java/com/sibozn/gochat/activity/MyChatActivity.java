package com.sibozn.gochat.activity;

import android.os.Bundle;
import android.view.View;

import com.sibozn.gochat.R;


public class MyChatActivity extends BaseWebSockteActivity {


    private String userEmail, userUid;

    @Override
    public int getContentViewId() {
        return R.layout.activity_mychat;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        userEmail = getIntent().getStringExtra("userEmail");
        userEmail = getIntent().getStringExtra("user_uid");
    }

    //websocket登录成功回调
    @Override
    protected void loginWebSocketSuccessed(String message) {

    }

    @Override
    protected void loginOutWebSocket() {

    }


    public void onBack(View view) {
        finish();
    }
}
