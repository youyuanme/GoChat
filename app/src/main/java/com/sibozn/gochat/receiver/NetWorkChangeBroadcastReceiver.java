package com.sibozn.gochat.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import com.sibozn.gochat.R;
import com.sibozn.gochat.application.MyApplication;
import com.sibozn.gochat.utils.PreferenceHelper;
import com.sibozn.gochat.utils.Tools;
import com.sibozn.gochat.websocket.MyWebSocketClient;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

public class NetWorkChangeBroadcastReceiver extends BroadcastReceiver implements MyWebSocketClient
        .MyWebSocketOpenedListener, MyWebSocketClient.MyWebSocketMessageListener {
    private static final String TAG = "NetWorkChangeBroadcastReceiver";
    private Context mContext;
    private onLoginWebSocketListener onLoginWebSocketListener;
    private onLoginOutWebSocketListener onLoginOutWebSocketListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        // MyWebSocketClient myWebSocketClient = MyWebSocketClient.getInstance();
        String action = intent.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            if (Tools.isNetworkConnected(context)) {
                Log.e(TAG, "---------------网络连接正常");
                if (!MyApplication.isLoginWebSocket) {
                    Tools.showWebSocketDialog(context, false, context.getString(R.string.web_socket_loading));
                    MyWebSocketClient.getInstance().setMyWebSocketOpenedListener(NetWorkChangeBroadcastReceiver
                            .this);
                    MyWebSocketClient.getInstance().setMyWebSocketMessageListener(NetWorkChangeBroadcastReceiver
                            .this);
                    MyWebSocketClient.getInstance().connectMyWebSocket();
                }
            } else {
                Log.e(TAG, "------------网络连接断开--");
                Tools.showToast(context, "请检查网络");
                onLoginOutWebSocketListener.onLoginOut();
            }
        }
    }

    @Override
    public void onOpened(ServerHandshake serverHandshakeData) {
        Log.e(TAG, "--------serverHandshakeData.getHttpStatus()" + serverHandshakeData.getHttpStatus());
        Log.e(TAG, "--------serverHandshakeData.getHttpStatusMessage()" + serverHandshakeData.getHttpStatusMessage());
        if (Tools.isLoginNohttp(mContext)) {
            final String email = PreferenceHelper.readString(mContext, "user_info", "email", "");
            final String token = PreferenceHelper.readString(mContext, "user_info", "token", "");
            Log.e(TAG, "--------------" + MyWebSocketClient.getInstance().toString());
            MyWebSocketClient.getInstance().sendMyWebSocket(Tools.loginWebSocketString(email, token));
        }
    }

    @Override
    public void onMessage(String message) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            switch (jsonObject.getString("cmd")) {
                case "login":
                    Tools.closeDialog();
                    Log.e(TAG, "webSocket登录成功了。。。。");
                    MyApplication.fd = jsonObject.getString("fd");
                    MyApplication.isLoginWebSocket = true;
                    onLoginWebSocketListener.onFirstLoginSucceed(message);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public interface onLoginWebSocketListener {
        void onFirstLoginSucceed(String message);
    }

    public interface onLoginOutWebSocketListener {
        void onLoginOut();
    }

    public void setOnLoginWebSocketListener(NetWorkChangeBroadcastReceiver.onLoginWebSocketListener
                                                    onLoginWebSocketListener) {
        this.onLoginWebSocketListener = onLoginWebSocketListener;
    }

    public void setOnLoginOutWebSocketListener(NetWorkChangeBroadcastReceiver.onLoginOutWebSocketListener
                                                       onLoginOutWebSocketListener) {
        this.onLoginOutWebSocketListener = onLoginOutWebSocketListener;
    }
}
