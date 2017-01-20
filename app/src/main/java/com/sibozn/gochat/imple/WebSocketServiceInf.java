package com.sibozn.gochat.imple;

import org.java_websocket.handshake.ServerHandshake;

/**
 * Created by Administrator on 2016/8/17.
 */
public interface WebSocketServiceInf {

    public void onOpen(ServerHandshake serverHandshake);
    public void onMessage(String message);
    public void onClose(int code, String reason, boolean remote);
    public void onError(Exception e);
}
