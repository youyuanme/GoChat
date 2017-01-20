package com.sibozn.gochat.websocket;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.sibozn.gochat.application.MyApplication;
import com.sibozn.gochat.utils.Tools;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Administrator on 2016/8/26.
 */
public class WebSocketHelper {

    WebSocketClient mConnection;
    private static MyWebSockteMessageInf mface;
    private String memail,mToken;

    public void setMemail(String memail) {
        this.memail = memail;
    }

    public void setmThken(String mToken) {
        this.mToken = mToken;
    }

    public interface MyWebSockteMessageInf {
        void onMessage(String message);
    }

    private static class SingletonHolder {
        /**
         * 单例对象实例
         */
        static final WebSocketHelper INSTANCE = new WebSocketHelper();

    }

    /**
     * private的构造函数用于避免外界直接使用new来实例化对象
     */
    private WebSocketHelper() {
    }

    public static WebSocketHelper getInstance(MyWebSockteMessageInf face) {
        mface = face;
        return SingletonHolder.INSTANCE;
    }

    // 发送websocke
    public void send(String data) {
//        mConnection.send(data);
        if (MyApplication.isLoginWebSocket) {
            mConnection.send(data);
        }
    }

    // 连接websocket
    public void connect() {
        initwebSocket();
//        new Thread() {
//            @Override
//            public void run() {
//                initwebSocket();
//                Log.d("TAG", "onMessage: 2");
//            }
//        }.start();
    }
    // 关闭websocket
    public void close() {
        mConnection.close();
    }

    private void initwebSocket() {
        try {
            mConnection = new WebSocketClient(new URI(
                    "ws://test.sibozn.com:9601"), new Draft_17()) {

                @Override
                public void onOpen(ServerHandshake arg0) {
                    // TODO Auto-generated method stub
                    if (TextUtils.isEmpty(memail)) {
                        Log.e("---------------", "登录websock时邮箱为空！");
                        return;
                    }
                    mConnection.send(Tools.loginWebSocketString(memail, mToken));
                }

                @Override
                public void onMessage(String arg0) {
                    // TODO Auto-generated method stub
                    try {
                        JSONObject jsonObject = new JSONObject(arg0);
                        if (TextUtils.equals(jsonObject.getString("cmd"),"login")) {
                            MyApplication.isLoginWebSocket = true;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mface.onMessage(arg0);
                    //mConnection.close();
                }

                @Override
                public void onError(Exception arg0) {
                    // TODO Auto-generated method stub
                    mface.onMessage("error");
                }

                @Override
                public void onClose(int arg0, String arg1, boolean arg2) {
                    // TODO Auto-generated method stub
                    mface.onMessage("close");
                }
            };
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            mface.onMessage("e");
        }
        Log.d("TAG", "onMessage: 4");
        mConnection.connect();
    }

}
