package com.sibozn.gochat.websocket;

import org.java_websocket.handshake.ServerHandshake;

/**
 * Created by Administrator on 2016/8/17.
 */
public interface MyWebSocketClientConnetListener {
    public void onOpened(ServerHandshake serverHandshakeData);
}
