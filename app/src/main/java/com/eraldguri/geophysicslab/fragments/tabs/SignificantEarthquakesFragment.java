package com.eraldguri.geophysicslab.fragments.tabs;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.eraldguri.geophysicslab.R;
import com.eraldguri.geophysicslab.adapter.EarthquakeListAdapter;
import com.eraldguri.geophysicslab.adapter.SignificantEarthquakesAdapter;
import com.eraldguri.geophysicslab.database.SignificantEarthquakes;
import com.eraldguri.geophysicslab.util.DividerItemDecorator;
import com.eraldguri.geophysicslab.util.TSVReader;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.M)
public class SignificantEarthquakesFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private SignificantEarthquakesAdapter mSignificantEarthquakesAdapter;
    private List<SignificantEarthquakes> significantEarthquakesList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_significant_earthquakes, container, false);

        initViews(root);

        //List<String[]> data = TSVReader.readTSV(requireContext());
        //TSVReader.setData(data);

        //significantEarthquakesList = TSVReader.getEarthquakesList();

        return root;
    }

    private void initViews(View root) {
        mRecyclerView = root.findViewById(R.id.significant_earthquakes_recyclerview);
    }

    private void setupRecyclerView(List<SignificantEarthquakes> mSignificantEarthquakes) {
        if (mSignificantEarthquakesAdapter == null) {
            mSignificantEarthquakesAdapter = new SignificantEarthquakesAdapter(getContext(), mSignificantEarthquakes);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            mRecyclerView.addItemDecoration(new DividerItemDecorator(requireContext()));
            mRecyclerView.setAdapter(mSignificantEarthquakesAdapter);
        }
    }



}