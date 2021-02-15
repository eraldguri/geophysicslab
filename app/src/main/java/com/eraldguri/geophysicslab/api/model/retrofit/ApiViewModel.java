package com.eraldguri.geophysicslab.api.model.retrofit;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eraldguri.geophysicslab.R;
import com.eraldguri.geophysicslab.api.model.Features;

import java.util.List;

public class ApiViewModel extends ViewModel {
    private MutableLiveData<List<Features>> quakes;

    public LiveData<List<Features>> getFeatures() {
        if (quakes == null) {
            quakes = new MutableLiveData<>();
            loadQuakes();
        }
        return quakes;
    }

    private void loadQuakes() {
        RetrofitHelper.callApi(mConnectionCallback);
    }

    private final RetrofitHelper.ConnectionCallback mConnectionCallback = new RetrofitHelper.ConnectionCallback() {
        @Override
        public void onSuccess(List<Features> features) {
            quakes.setValue(features);
        }

        @Override
        public void onError(int code, String error) {
            Log.d("error", code + " " + error);
        }
    };

}
