package com.eraldguri.geophysicslab.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eraldguri.geophysicslab.EarthquakeListAdapter;
import com.eraldguri.geophysicslab.R;
import com.eraldguri.geophysicslab.api.model.Earthquake;
import com.eraldguri.geophysicslab.api.model.Features;
import com.eraldguri.geophysicslab.api.model.retrofit.HttpRequestHelper;
import com.eraldguri.geophysicslab.util.DividerItemDecorator;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EarthquakeListFragment extends Fragment {

    private RecyclerView mEarthquakeListView;
    private EarthquakeListAdapter mEarthquakeListAdapter;
    private List<Features> features;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_earthquake_list, container, false);

        initViews(root);
        getDataFromServer();

        return root;
    }

    private void initViews(View view) {
        mEarthquakeListView = view.findViewById(R.id.rv_earthquake_data_list);
    }

    private void setupRecyclerView(List<Features> _features) {
        if (mEarthquakeListAdapter == null) {
            mEarthquakeListAdapter = new EarthquakeListAdapter(getContext(), _features);
            mEarthquakeListView.setLayoutManager(new LinearLayoutManager(requireContext()));
            mEarthquakeListView.addItemDecoration(new DividerItemDecorator(requireContext()));
            mEarthquakeListView.setAdapter(mEarthquakeListAdapter);
        }
    }

    private void getDataFromServer() {
        HttpRequestHelper requestHelper = new HttpRequestHelper();
        Callback<Earthquake> earthquakeCallback = new Callback<Earthquake>() {
            @Override
            public void onResponse(@NotNull Call<Earthquake> call, @NotNull Response<Earthquake> response) {
                if (response.isSuccessful() && response.body() != null) {
                    features = response.body().getFeatures();
                    setupRecyclerView(features);
                } else {
                    System.out.println(features);
                }
            }

            @Override
            public void onFailure(@NotNull Call<Earthquake> call, @NotNull Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        requestHelper.getEarthquakes(earthquakeCallback);
    }
}