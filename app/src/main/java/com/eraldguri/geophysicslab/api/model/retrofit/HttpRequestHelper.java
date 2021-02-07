package com.eraldguri.geophysicslab.api.model.retrofit;

import com.eraldguri.geophysicslab.api.model.Earthquake;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class HttpRequestHelper {
    private static APICallback callback;

    public HttpRequestHelper() {
        Retrofit retrofit = ApiBuilder.getRetrofitClient();
        callback = retrofit.create(APICallback.class);
    }

    public void getEarthquakes(Callback<Earthquake> earthquakeCallback) {
        Call<Earthquake> earthquakeCall = callback.getEarthquakes();
        earthquakeCall.enqueue(earthquakeCallback);
    }
}
