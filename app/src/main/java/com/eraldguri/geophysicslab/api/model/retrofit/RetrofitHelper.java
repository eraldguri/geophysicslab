package com.eraldguri.geophysicslab.api.model.retrofit;

import android.accounts.NetworkErrorException;
import android.util.Log;

import com.eraldguri.geophysicslab.api.model.Earthquake;
import com.eraldguri.geophysicslab.api.model.Features;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import okhttp3.ResponseBody;
import okio.Timeout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitHelper {

    private static List<Features> features;
    private static ConnectionCallback mConnectionCallback;

    public static void callApi(ConnectionCallback connectionCallback) {
        mConnectionCallback = connectionCallback;
        HttpRequestHelper.init();
        Callback<Earthquake> earthquakeCallback = new Callback<Earthquake>() {
            @Override
            public void onResponse(@NotNull Call<Earthquake> call, @NotNull Response<Earthquake> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (mConnectionCallback != null) {
                        features = response.body().getFeatures();
                        mConnectionCallback.onSuccess(features);
                    }
                } else {
                    try {
                        String res = response.body().toString();
                        if (mConnectionCallback != null)
                            mConnectionCallback.onError(response.code(), res);
                    } catch (NullPointerException e) {
                        Log.i("TAG", "onResponse: " + call.request().url());
                        e.printStackTrace();
                        if (mConnectionCallback != null) {
                            mConnectionCallback.onError(response.code(), e.getMessage());
                        }
                    }
                    System.out.println(features);
                }
            }

            @Override
            public void onFailure(@NotNull Call<Earthquake> call, @NotNull Throwable error) {
                String message = null;
                if (error instanceof NetworkErrorException) {
                    message = "Please check your internet connection";
                } else if (error instanceof ParseException) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof TimeoutException) {
                    message = "Connection TimeOut! Please check your internet connection.";
                } else if (error instanceof UnknownHostException) {
                    message = "Please check your internet connection and try later";
                } else if (error instanceof Exception) {
                    message = error.getMessage();
                }
                if (mConnectionCallback != null) {
                    mConnectionCallback.onError(-1, message);
                }
            }
        };
        HttpRequestHelper.getEarthquakes(earthquakeCallback);
    }

    public interface ConnectionCallback {
        void onSuccess(List<Features> features);

        void onError(int code, String error);
    }
}

