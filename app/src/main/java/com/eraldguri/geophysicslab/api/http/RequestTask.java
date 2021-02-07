package com.eraldguri.geophysicslab.api.http;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

public class RequestTask  {

    public static void httpRequest(Context context) {
        URL url;
        HttpURLConnection urlConnection;
        try {
            url = new URL(HttpBuilder.JSON_URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            HttpBuilder.httpBuilder(urlConnection);
            int responseCode = urlConnection.getResponseCode();
            String responseMessage = urlConnection.getResponseMessage();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String response = readInputStream(urlConnection.getInputStream());

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String type = jsonObject.getString("type");
                    Log.d("type", type);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("response", response);
                Log.d("responseMessage", responseMessage);
            } else {
                Toast.makeText(context, "Error: cannot make request" + responseMessage, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseJson(String jsonString) {
        if (jsonString != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                String type = jsonObject.getString("type");
                Log.d("type", type);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private static String readInputStream(InputStream inputStream) {
        String line = "";
        BufferedReader bufferedReader = null;
        StringBuilder responseString = new StringBuilder();
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = bufferedReader.readLine()) != null) {
                responseString.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return responseString.toString();
    }

}
