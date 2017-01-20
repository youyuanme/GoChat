package com.sibozn.gochat.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.sibozn.gochat.nohttp.CallServer;
import com.sibozn.gochat.nohttp.HttpListener;
import com.sibozn.gochat.utils.Constants;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService implements HttpListener<String> {
    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     * <p/>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        Log.e(TAG, "---推送token更新--" + token);
        Request<String> request = NoHttp.createStringRequest(Constants.SET_PUSH_URL, RequestMethod.POST);
        if (request != null) {// email  token   pushtoken  sys(android/ios)
            SharedPreferences sp = getSharedPreferences("user_info", Context.MODE_PRIVATE);
            String email = sp.getString("email", "");
            if (TextUtils.isEmpty(email)) {// 判读用户是否登录，没有登录不发送pushtoken
                return;
            }
            request.add("email", email);
            request.add("token", sp.getString("token", ""));
            request.add("pushtoken", token);
            request.add("sys", "android");
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.d(TAG, "onValidationSucceeded: --refreshedToken-->>" + refreshedToken);
            // 添加到请求队列
            CallServer.getRequestInstance().add(this, 0, request, this, false, false);
        }
    }

    @Override
    public void onSucceed(int what, Response<String> response) {
        Log.d(TAG, "onSucceed: -更新token成功！-->>" + response.get());
    }

    @Override
    public void onFailed(int what, Response<String> response) {
        Log.d(TAG, "onSucceed: -更新token失败！-->>" + response.getException().getMessage());
    }

}
