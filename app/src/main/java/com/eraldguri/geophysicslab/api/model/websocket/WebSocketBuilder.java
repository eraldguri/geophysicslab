package com.eraldguri.geophysicslab.api.model.websocket;

import android.os.Build;
import android.util.Log;

import com.eraldguri.geophysicslab.BuildConfig;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WebSocketBuilder extends WebSocketListener {

    private static final String SOCKET_URL = "wss://www.seismicportal.eu/standing_order/websocket";
    private static OkHttpClient client;
    private static EchoWebSocketListener webSocketListener;
    private static final int NORMAL_CLOSURE_STATUS = 1000;

    private static JsonObjectFromWebSocket objectFromWebSocket;

    public static void startWebSocketConnection(JsonObjectFromWebSocket jsonListener) {
        client = new OkHttpClient();
        OkHttpClient.Builder builder = new OkHttpClient.Builder(client);
        builder.readTimeout(3000, TimeUnit.MILLISECONDS);

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N) {
            builder.connectionSpecs(Objects.requireNonNull(getSpecsBelowLollipopMR1(builder)));
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
            List<ConnectionSpec> specList = getSpecsBelowLollipopMR1(builder);
            if (specList != null) {
                builder.connectionSpecs(specList);
            }
        }
        builder.build();

        objectFromWebSocket = jsonListener;

        webSocketListener = new EchoWebSocketListener(new EchoWebSocketListener.SocketClientListener() {

            @Override
            public void onConnected(WebSocket webSocket, Response response) {
                Log.d("Connected :", response.toString());
            }

            @Override
            public void getMessage(String message) {
                Log.d("Received", message);
                try {
                    JSONObject jsonObject = (JSONObject) new JSONTokener(message).nextValue();
                    objectFromWebSocket.passData(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(WebSocket webSocket, String reason) {
                Log.d("Failure :", reason);
            }

            @Override
            public void closingOrClosed(boolean isClosed, WebSocket webSocket, String reason, int code) {
                Log.d("ClosingOrClosed :", isClosed ? "Closed" : "closing");
            }
        });


        Request request = new Request.Builder().url(SOCKET_URL).build();
        client.newWebSocket(request, webSocketListener);
    }

    public interface JsonObjectFromWebSocket {
        void passData(JSONObject jsonObject);
    }

    public static void closeWebSocketConnection() {
        try {
            webSocketListener.getWebSocket().cancel();
            webSocketListener.getWebSocket().close(NORMAL_CLOSURE_STATUS, "onClose");
            client.dispatcher().executorService().shutdown();
            client.connectionPool().evictAll();
        } catch (Exception e) {
            e.printStackTrace();;
        }
    }

    private static List<ConnectionSpec> getSpecsBelowLollipopMR1(OkHttpClient.Builder builder) {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLSv1.1");
            sslContext.init(null, null, null);
            builder.sslSocketFactory(new Tls12SocketFactory(sslContext.getSocketFactory()));
            ConnectionSpec connectionSpec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                    .tlsVersions(TlsVersion.TLS_1_2)
                    .build();
            List<ConnectionSpec> connectionSpecs = new ArrayList<>();
            connectionSpecs.add(connectionSpec);
            connectionSpecs.add(ConnectionSpec.COMPATIBLE_TLS);

            return connectionSpecs;
        } catch (Exception e) {
           Log.e("tag", "OkHttpTLSCompat Error while setting TLS 1.2"+ e);
           return null;
        }
    }
}
