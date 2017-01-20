package com.sibozn.gochat.websocket;


import org.java_websocket.handshake.ServerHandshake;

public interface WebSocketClientListenerInterface {
	// public void onOpen(final ServerHandshake serverHandshakeData);

	public void onMessage(final String message);

//	public void onClose(final int code, final String reason,
//						final boolean remote);

	//public void onError(final Exception e);
}
