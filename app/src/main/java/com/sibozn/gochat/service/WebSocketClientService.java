package com.sibozn.gochat.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.sibozn.gochat.imple.WebSocketServiceInf;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketClientService extends Service {
    private static final String TAG = "WebSocketClientService";
    private WebSocketClient webSocketClient;
    private WebSocketServiceInf webSocketServiceInf;
    private String sendString;

    public WebSocketServiceInf getWebSocketServiceInf() {
        return webSocketServiceInf;
    }

    public void setWebSocketServiceInf(WebSocketServiceInf webSocketServiceInf) {
        this.webSocketServiceInf = webSocketServiceInf;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "-------------onCreate: ");
        if (webSocketClient == null) {
            initWebSocket();
        }
        connectWebSocket();
    }

    /**
     * onBind 是 Service 的虚方法，因此我们不得不实现它。
     * 返回 null，表示客服端不能建立到此服务的连接。
     */
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "-------------onBind: ");
        // TODO: Return the communication channel to the service.
        return new MyBinder();
        // throw new UnsupportedOperationException("Not yet implemented");
    }

    // 已取代onStart方法--onStart方法是在Android2.0之前的平台使用的.
    // 在2.0及其之后，则需重写onStartCommand方法，同时，旧的onStart方法则不会再被直接调用
    // （外部调用onStartCommand，而onStartCommand里会再调用 onStart。在2.0之后，
    // 推荐覆盖onStartCommand方法，而为了向前兼容，在onStartCommand依然会调用onStart方法。
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "----------onStartCommand: ");
        //sendString = intent.getStringExtra("message");
        //sendMessage(sendString);
        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "----------onUnbind: ");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "--------------onDestroy: ");
        Intent intent = new Intent(this, WebSocketClientService.class);
        intent.setAction("com.sibozn.gochat.WEB_SOCKTE_MESSAGE");
        startService(intent);
        //closeWebSocket();
        //Log.e(TAG, "WebSocketClientService onDestroy: ");
    }

    // IBinder是远程对象的基本接口，是为高性能而设计的轻量级远程调用机制的核心部分。但它不仅用于远程
    // 调用，也用于进程内调用。这个接口定义了与远程对象交互的协议。
    // 不要直接实现这个接口，而应该从Binder派生。
    // Binder类已实现了IBinder接口
    public class MyBinder extends Binder {
        /**
         * 获取Service的方法
         *
         * @return 返回WebSocketClientService
         */
        public WebSocketClientService getService() {
            return WebSocketClientService.this;
        }
    }

    public void sendMessage(String message) {
        if (webSocketClient != null && webSocketClient.getConnection().isOpen()) {
            webSocketClient.send(message);
        }
    }

    // 关闭websocket
    public void closeWebSocket() {
        WebSocket webSocket = webSocketClient.getConnection();
        Log.e(TAG, "closeWebSocket: -webSocket.isOpen()-->>" + webSocket.isOpen());
        Log.e(TAG, "closeWebSocket: -webSocket.isFlushAndClose()-->>" + webSocket.isFlushAndClose());
        Log.e(TAG, "closeWebSocket: -webSocket.isConnecting()-->>" + webSocket.isConnecting());
        Log.e(TAG, "closeWebSocket: -webSocket.isClosing()-->>" + webSocket.isClosing());
        Log.e(TAG, "closeWebSocket: -webSocket.isClosed()-->>" + webSocket.isClosed());
        if (webSocketClient.getConnection().isOpen()) {
            webSocketClient.close();
        }
    }

    // 连接webSocket
    public void connectWebSocket() {
        if (webSocketClient == null) {
            initWebSocket();
        }
        webSocketClient.connect();
    }

    //初始化websocket
    private void initWebSocket() {
        try {
            webSocketClient = new WebSocketClient(new URI("ws://test.sibozn.com:9601"), new Draft_17()) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    webSocketServiceInf.onOpen(serverHandshake);
                }

                @Override
                public void onMessage(String s) {
                    webSocketServiceInf.onMessage(s);
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    webSocketServiceInf.onClose(i, s, b);
                }

                @Override
                public void onError(Exception e) {
                    webSocketServiceInf.onError(e);
                }
            };
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

}
