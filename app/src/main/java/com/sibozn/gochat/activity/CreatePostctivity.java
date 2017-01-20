package com.sibozn.gochat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.sibozn.gochat.R;
import com.sibozn.gochat.utils.PreferenceHelper;
import com.sibozn.gochat.utils.Tools;
import com.sibozn.gochat.websocket.MyWebSocketClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;

public class CreatePostctivity extends BaseWebSockteActivity implements View.OnClickListener, TextWatcher
        , MyWebSocketClient.MyWebSocketMessageListener, Validator.ValidationListener {

    @BindView(R.id.create_post_toobar)
    Toolbar createPostToobar;
    @NotEmpty
    @BindView(R.id.ed_post_content)
    EditText edPostContent;
    @BindView(R.id.tv_display_edittext)
    TextView tv_display_edittext;
    @BindView(R.id.bt_post)
    Button btPost;
    private Validator validator;
    private double lat, lng;
    private String email, token;

    @Override
    public int getContentViewId() {
        return R.layout.activity_create_postctivity;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        email = PreferenceHelper.readString(this, "user_info", "email", "");
        token = PreferenceHelper.readString(this, "user_info", "token", "");
        initView();
        validator = new Validator(this);
        validator.setValidationListener(this);
        // myWebSocketClient.setMyWebSocketOpenedListener(this);
        MyWebSocketClient.getInstance().setMyWebSocketMessageListener(this);
        Intent intent = getIntent();
        lat = intent.getDoubleExtra("lat", -33.867);
        lng = intent.getDoubleExtra("lng", 151.206);
    }

    // websocket登录成功回调
    @Override
    protected void loginWebSocketSuccessed(String message) {
        Log.e(TAG, "=================loginWebSocketSuccessed: ");
        MyWebSocketClient.getInstance().setMyWebSocketMessageListener(this);
    }

    @Override
    protected void loginOutWebSocket() {

    }

    private void initView() {
        setSupportActionBar(createPostToobar);
        createPostToobar.setNavigationIcon(R.drawable.selector_back);
        createPostToobar.setTitle(R.string.create_post);
        createPostToobar.setTitleTextColor(getResources().getColor(R.color.white));
        createPostToobar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        edPostContent.addTextChangedListener(this);
        btPost.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_post:
                validator.validate();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }// EditText变化监听

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        int lengthString = s.length();
        if (lengthString <= 140) {
            String post_value_format = getResources().getString(R.string.post_display_value);
            tv_display_edittext.setText(String.format(post_value_format, lengthString));
            btPost.setVisibility(View.VISIBLE);
        } else {
            btPost.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onValidationSucceeded() {
        String content = edPostContent.getText().toString();
        Tools.showDialog(this, false);
        MyWebSocketClient.getInstance()
                .sendMyWebSocket(Tools.sendPostWebSocketString(email, token, lat, lng, content));
    }// 使用webSocket发布post

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }// EditText输入内容验证失败回调

    @Override
    public void onMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(message);
                    switch (jsonObject.getString("cmd")) {
                        case "post":// post发送成功
                            Log.d(TAG, "onMessage: " + message);
                            Tools.closeDialog();
                            if (TextUtils.equals("1", jsonObject.getString("success"))) {
                                Toast.makeText(CreatePostctivity.this, R.string.post_success, Toast.LENGTH_SHORT)
                                        .show();
                                finish();
                            }
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }// webSocket返回数据回调方法
}
