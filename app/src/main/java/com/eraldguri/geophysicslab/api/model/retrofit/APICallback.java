package com.eraldguri.geophysicslab.api.model.retrofit;

import com.eraldguri.geophysicslab.api.model.Earthquake;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface APICallback {

    @Headers({"Accept: application/json"})
    @GET("?" + ApiBuilder.JSON_FORMAT + "&" + ApiBuilder.DELIMITER)
    Call<Earthquake> getEarthquakes();
}
