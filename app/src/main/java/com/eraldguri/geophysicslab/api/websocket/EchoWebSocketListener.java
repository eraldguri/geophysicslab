package com.eraldguri.geophysicslab.api.websocket;

import android.util.Log;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class EchoWebSocketListener extends WebSocketListener {
    private final SocketClientListener socketClientListener;
    private WebSocket webSocket = null;

    public EchoWebSocketListener(SocketClientListener listener) {
        socketClientListener = listener;
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        super.onOpen(webSocket, response);
        this.webSocket = webSocket;
        socketClientListener.onConnected(webSocket, response);
    }

    public void sendMessage(String message) {
        if (webSocket != null) {
            webSocket.send(message);
        }
    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
        socketClientListener.onFailure(webSocket, t.getLocalizedMessage());
    }

    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        socketClientListener.closingOrClosed(true, webSocket, reason, code);
    }

    @Override
    public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        socketClientListener.closingOrClosed(true, webSocket, reason, code);
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        socketClientListener.getMessage(text);
        Log.d("tag", text);
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
        super.onMessage(webSocket, bytes);
        Log.d("tag", bytes.toString());
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    public interface SocketClientListener {
        void onConnected(WebSocket webSocket, Response response);
        void getMessage(String message);
        void onFailure(WebSocket webSocket, String reason);
        void closingOrClosed(boolean isClosed, WebSocket webSocket, String reason, int code);
    }
}
