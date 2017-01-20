package com.sibozn.gochat.websocket;

import android.util.Log;

import com.sibozn.gochat.application.MyApplication;
import com.sibozn.gochat.utils.Constants;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * WebSocketClient 单例类
 */
public class MyWebSocketClient extends WebSocketClient {

    private static final String TAG = "MyWebSocketClient";
    public static MyWebSocketClient myWebSocketClient;
    private static WebSocketClientListenerInterface mWebSocketClientListener;
    private MyWebSocketOpenedListener myWebSocketOpenedListener;
    private MyWebSocketClosedListener myWebSocketClosedListener;
    private MyWebSocketMessageListener myWebSocketMessageListener;
    private MyWebSocketErrorListener myWebSocketErrorListener;

    // 构造器
    private MyWebSocketClient(URI serverURI) {
        super(serverURI);
    }

    // 构造器
    private MyWebSocketClient(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }

    // 构造器
    private MyWebSocketClient(URI serverUri, Draft draft, Map<String, String> headers, int connecttimeout) {
        super(serverUri, draft, headers, connecttimeout);
    }

    public static MyWebSocketClient getInstance() {
        if (myWebSocketClient == null) {
            synchronized (MyWebSocketClient.class) {
                if (myWebSocketClient == null) {
                    try {
                        myWebSocketClient = new MyWebSocketClient(new URI(
                                Constants.WEB_SOCKET_URL), new Draft_17());
                        Log.e(TAG, "重新new MyWebSocketClient对象");
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                        Log.d(TAG, "MyWebSocketClient-创建异常--->" + e.toString());
                    }
                }
            }
        }
        return myWebSocketClient;
    }// 单例模式获取对象

    /**
     * 获取WebSocket 对象
     *
     * @return
     */
    public WebSocket getWebSocket() {
        return getConnection();
    }// 获取WebSocket 对象

    /**
     * 连接websocket
     */
    public void connectMyWebSocket() {
        if (myWebSocketClient != null) {
            myWebSocketClient.connect();
        }
    }//连接websocket

    /**
     * webSockey发送文本内容
     *
     * @param text
     */
    public void sendMyWebSocket(String text) {
        if (myWebSocketClient != null && myWebSocketClient.getConnection().isOpen()) {
            Log.e(TAG, "sendMyWebSocket: " + text);
            myWebSocketClient.send(text);
        }
    }//webSockey发送文本内容

    /**
     * webSockey发送二进制数据
     *
     * @param data
     */
    public void sendMyWebSocket(byte[] data) {
        if (myWebSocketClient != null && myWebSocketClient.getConnection().isOpen()) {
            myWebSocketClient.send(data);
        }
    }//webSockey发送二进制数据

    /**
     * 关websocket连接
     */
    public void closeMyWebSocketClient() {
        if (myWebSocketClient != null && myWebSocketClient.getConnection().isOpen()) {
            myWebSocketClient.close();
        }
    }// 关websocket连接

    @Override
    public void onOpen(final ServerHandshake serverHandshakeData) {
        myWebSocketOpenedListener.onOpened(serverHandshakeData);
    }

    @Override
    public void onMessage(final String message) {
        myWebSocketMessageListener.onMessage(message);
    }

    @Override
    public void onClose(final int code, final String reason,
                        final boolean remote) {
        Log.e(TAG, "onClose:断开服务器连接【" + getURI() + "，状态码： "
                + code + "，断开原因：" + reason + ",remote：" + remote + "】");
        // myWebSocketClosedListener.onClose(code, reason, remote);
        MyApplication.isLoginWebSocket = false;
        if (myWebSocketClient != null) {
            myWebSocketClient = null;
        }
        if (myWebSocketClient == null) {
            Log.e(TAG, "--myWebSocketClient == null--");
        }
//        Log.e(TAG, "myWebSocketClient.toString()" + myWebSocketClient.toString());
//        Log.e(TAG, "myWebSocketClient.isClosing()" + myWebSocketClient.isClosing());
//        Log.e(TAG, "myWebSocketClient.isConnecting()" + myWebSocketClient.isConnecting());
//        Log.e(TAG, "myWebSocketClient.isClosed()" + myWebSocketClient.isClosed());
//        Log.e(TAG, "myWebSocketClient.isOpen()" + myWebSocketClient.isOpen());
//        Log.e(TAG, "myWebSocketClient.isFlushAndClose()" + myWebSocketClient.isFlushAndClose());

    }

    @Override
    public void onError(final Exception e) {
        Log.e(TAG, "连接发生了异常【异常原因】onError: " + e);
        if (myWebSocketClient != null) {
            myWebSocketClient = null;
        }
    }

    /**
     * webSocket连接成功监听接口
     */
    public interface MyWebSocketOpenedListener {// webSocket连接成功监听接口

        public void onOpened(final ServerHandshake serverHandshakeData);
    }

    /**
     * webSocket关闭监听接口
     */
    public interface MyWebSocketClosedListener {
        public void onClose(final int code, final String reason,
                            final boolean remote);
    }

    /**
     * webSocket返回数据监听接口
     */
    public interface MyWebSocketMessageListener {
        public void onMessage(final String message);
    }

    /**
     * webSocket异常监听接口
     */
    public interface MyWebSocketErrorListener {
        public void onError(final Exception e);
    }

    public void setMyWebSocketOpenedListener(MyWebSocketOpenedListener myWebSocketOpendListener) {
        this.myWebSocketOpenedListener = myWebSocketOpendListener;
    }

    public void setMyWebSocketClosedListener(MyWebSocketClosedListener myWebSocketClosedListener) {
        this.myWebSocketClosedListener = myWebSocketClosedListener;
    }

    public void setMyWebSocketMessageListener(MyWebSocketMessageListener myWebSocketMessageListener) {
        this.myWebSocketMessageListener = myWebSocketMessageListener;
    }

    public void setMyWebSocketErrorListener(MyWebSocketErrorListener myWebSocketErrorListener) {
        this.myWebSocketErrorListener = myWebSocketErrorListener;
    }

    public void setmWebSocketClientListener(
            WebSocketClientListenerInterface mWebSocketClientListener) {
        this.mWebSocketClientListener = mWebSocketClientListener;
    }
}
