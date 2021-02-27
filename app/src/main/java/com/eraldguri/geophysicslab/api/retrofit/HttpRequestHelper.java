package com.eraldguri.geophysicslab.api.retrofit;

import com.eraldguri.geophysicslab.api.model.Earthquake;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class HttpRequestHelper {
    private static APICallback callback;

    public static void init()  {
        Retrofit retrofit = ApiBuilder.getRetrofitClient();
        callback = retrofit.create(APICallback.class);
    }

    public static void getEarthquakes(Callback<Earthquake> earthquakeCallback) {
        Call<Earthquake> earthquakeCall = callback.getEarthquakes();
        earthquakeCall.enqueue(earthquakeCallback);
    }
}
