package com.eraldguri.geophysicslab.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.eraldguri.geophysicslab.EarthquakeListAdapter;
import com.eraldguri.geophysicslab.MainActivity;
import com.eraldguri.geophysicslab.R;
import com.eraldguri.geophysicslab.api.model.Features;
import com.eraldguri.geophysicslab.api.model.retrofit.RetrofitHelper;
import com.eraldguri.geophysicslab.navigation.EarthquakesFragment;
import com.eraldguri.geophysicslab.util.DividerItemDecorator;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.M)
public class EarthquakeListFragment extends EarthquakesFragment implements
        EarthquakeListAdapter.OnItemClickListener {

    private RecyclerView mEarthquakeListView;
    private EarthquakeListAdapter mEarthquakeListAdapter;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_earthquake_list, container, false);

        initViews(root);

        return root;
    }

    private RetrofitHelper.ConnectionCallback mConnectionCallback = new RetrofitHelper.ConnectionCallback() {
        @Override
        public void onSuccess(List<Features> features) {
            setupRecyclerView(features);
        }

        @Override
        public void onError(int code, String error) {
            Log.d("error", code + " " + error);
        }
    };

    private void initViews(View view) {
        mEarthquakeListView = view.findViewById(R.id.rv_earthquake_data_list);

        RetrofitHelper.callApi(mConnectionCallback);
    }

    private void setupRecyclerView(List<Features> earthquakes) {
        if (mEarthquakeListAdapter == null) {
            mEarthquakeListAdapter = new EarthquakeListAdapter(getContext(), earthquakes, this);
            mEarthquakeListView.setLayoutManager(new LinearLayoutManager(requireContext()));
            mEarthquakeListView.addItemDecoration(new DividerItemDecorator(requireContext()));
            mEarthquakeListView.setAdapter(mEarthquakeListAdapter);
        }
    }

    @Override
    public void onItemClick(int position, List<Features> features) {
        Log.d("tag", "clicked: " + position);
    }

  /*  @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mConnectionCallback = (RetrofitHelper.ConnectionCallback) context;
    }*/
}