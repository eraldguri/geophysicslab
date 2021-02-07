package com.eraldguri.geophysicslab.api.http;

import java.net.HttpURLConnection;
import java.net.ProtocolException;

public class HttpBuilder {

    public static final String JSON_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson";
    private static final String charset = "UTF-8";
    private static final String GET_REQUEST_METHOD = "GET";
    private static final int CONNECTION_TIMEOUT = 40000;
    private static final String APPLICATION_JSON = "application/json";

    public static void httpBuilder(HttpURLConnection urlConnection) {
        try {
            urlConnection.setRequestMethod(GET_REQUEST_METHOD);
            urlConnection.setReadTimeout(CONNECTION_TIMEOUT);
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setRequestProperty("Accept-Charset", charset);
            urlConnection.setRequestProperty("Accept", APPLICATION_JSON);
            urlConnection.setRequestProperty("Content-Type", APPLICATION_JSON);
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
    }

}
