package com.sibozn.gochat.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.sibozn.gochat.R;
import com.sibozn.gochat.application.MyApplication;
import com.sibozn.gochat.nohttp.CallServer;
import com.sibozn.gochat.nohttp.HttpListener;
import com.sibozn.gochat.utils.Constants;
import com.sibozn.gochat.utils.Tools;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener,
        Validator.ValidationListener, HttpListener<String> {

    private final int GOGIN_METHOD = 0;

    @NotEmpty
    @Email
    @BindView(R.id.user_email)
    EditText user_email;
    @Password(min = 6)
    @BindView(R.id.user_password)
    EditText user_password;
    @BindView(R.id.bt_login)
    Button bt_login;
    @BindView(R.id.bt_regest)
    Button bt_regest;
    @BindView(R.id.login_button)
    LoginButton login_button;
    private SharedPreferences sp;
    private Validator validator;
    private String email;
    private CallbackManager callbackManager;

    @Override
    public int getContentViewId() {
        return R.layout.activity_login_01;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        sp = getSharedPreferences("user_info", MODE_PRIVATE);
        validator = new Validator(this);
        validator.setValidationListener(this);
        //Facebook
        callbackManager = CallbackManager.Factory.create();
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //获取登录信息
                getLoginInfo(loginResult.getAccessToken());
                Profile profile = Profile.getCurrentProfile();
                if (profile != null) {
                    Log.e(TAG, "onSuccess: " + profile.getFirstName());
                }
                Log.e(TAG, "onSuccess: " + loginResult.getAccessToken().toString());
                Tools.showToast(mContext, "facebook登录成功了" + loginResult.getAccessToken().toString());
            }

            @Override
            public void onCancel() {
                Tools.showToast(mContext, "cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG, "onError: --" + error.toString());
            }
        });
        login_button.clearPermissions();
        login_button.setReadPermissions("email");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //facebook回调
        //callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 获取登录信息
     *
     * @param accessToken
     */
    public void getLoginInfo(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (object != null) {
                    String id = object.optString("id");   //比如:1565455221565
                    String name = object.optString("name");  //比如：Zhang San
                    String gender = object.optString("gender");  //性别：比如 male （男）  female （女）
                    String emali = object.optString("email");  //邮箱：比如：56236545@qq.com

                    //获取用户头像
                    JSONObject object_pic = object.optJSONObject("picture");
                    JSONObject object_data = object_pic.optJSONObject("data");
                    String photo = object_data.optString("url");

                    //获取地域信息
                    String locale = object.optString("locale");   //zh_CN 代表中文简体

                    Toast.makeText(mContext, "" + object.toString(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onCompleted: " + object.toString());
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,gender,birthday,email,picture,locale,updated_time,timezone," +
                "age_range,first_name,last_name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, "onNewIntent: ");
        if (!TextUtils.isEmpty(sp.getString("token", ""))) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        super.onNewIntent(intent);
    }

    @OnClick({R.id.bt_login, R.id.bt_regest})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                validator.validate(); // 邮箱格式验证
                break;
            case R.id.bt_regest:
                startActivity(new Intent(this, DemoActivity.class));
                break;
        }
    }

    @Override
    public void onValidationSucceeded() {
        // MobclickAgent.onProfileSignIn(email);// 友盟的账号统计
        email = user_email.getText().toString();
        String password = user_password.getText().toString();

        Request<String> request = NoHttp.createStringRequest(Constants.LOGIN_URL, RequestMethod.POST);
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
                    String ret = jsonObject.getString("ret");
                    if (TextUtils.equals("1", ret)) {
                        getSharedPreferences("user_info", MODE_PRIVATE).edit()
                                .putString("token", jsonObject.getString("token"))
                                .putString("email", email)
                                .commit();
                        MyApplication.mEmail = email;
                        MyApplication.mToken = jsonObject.getString("token");
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    }
                    Toast.makeText(this, jsonObject.getString("desc"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onFailed(int what, Response<String> response) {
        Toast.makeText(this, "登录失败！", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onFailed:" + response.getException().getMessage());
    }

}

