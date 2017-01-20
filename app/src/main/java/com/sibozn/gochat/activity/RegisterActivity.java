package com.sibozn.gochat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.sibozn.gochat.R;
import com.sibozn.gochat.nohttp.CallServer;
import com.sibozn.gochat.nohttp.HttpListener;
import com.sibozn.gochat.utils.Constants;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity
        implements Validator.ValidationListener, HttpListener<String> {

    private final int GOGIN_METHOD = 1;

    @NotEmpty
    @Email
    @BindView(R.id.regist_email)
    EditText regist_email;
    @Password(min = 6)
    @BindView(R.id.regist_password)
    EditText regist_password;
    @ConfirmPassword
    @BindView(R.id.regester_verify_password)
    EditText regester_verify_password;
    @BindView(R.id.bt_fregist)
    Button bt_fregist;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    private String email;
    private Validator validator;

    @Override
    public int getContentViewId() {
        return R.layout.activity_register_01;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @OnClick({R.id.iv_back, R.id.bt_fregist})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:// 返回
                finish();
                break;
            case R.id.bt_fregist:
                validator.validate(); // 格式验证
                break;
        }
    }

    @Override
    public void onValidationSucceeded() {
        //Toast.makeText(this, "Yay! we got it right!", Toast.LENGTH_SHORT).show();
        email = regist_email.getText().toString();
        String password = regist_password.getText().toString();
        Request<String> request = NoHttp.createStringRequest(Constants.REGISTER_URL, RequestMethod.POST);
        if (request != null) {
            request.add("email", email);
            request.add("passwd", password);
            request.add("pushtoken", FirebaseInstanceId.getInstance().getToken());
            request.add("sys", "android");
            // 添加到请求队列
            CallServer.getRequestInstance().add(this, GOGIN_METHOD, request, this, true, true);
        }
    }

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
    }

    @Override
    public void onSucceed(int what, Response<String> response) {
        int responseCode = response.getHeaders().getResponseCode();// 服务器响应码
        if (responseCode == 200) {
            if (RequestMethod.HEAD == response.request().getRequestMethod())// 请求方法为HEAD时没有响应内容
                // showMessageDialog(R.string.request_succeed, R.string.request_method_head);
                Toast.makeText(this, "request_method_head", Toast.LENGTH_SHORT).show();
            else {
                Log.e(TAG, response.get());
                try {
                    JSONObject jsonObject = new JSONObject(response.get());
                    if (TextUtils.equals("1", jsonObject.getString("ret"))) {
                        getSharedPreferences("user_info", MODE_PRIVATE).edit()
                                .putString("token", jsonObject.getString("token"))
                                .putString("email", email)
                                .commit();
                        startActivity(new Intent(this, LoginActivity.class));
                    }
                    Toast.makeText(this, jsonObject.getString("desc"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }// nohttp 请求成功回调

    @Override
    public void onFailed(int what, Response<String> response) {
        Toast.makeText(this, "登录失败！", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onFailed:" + response.getException().getMessage());
    }

}
