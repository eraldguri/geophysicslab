package com.eraldguri.geophysicslab.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.eraldguri.geophysicslab.EarthquakeListAdapter;
import com.eraldguri.geophysicslab.MainActivity;
import com.eraldguri.geophysicslab.R;
import com.eraldguri.geophysicslab.api.model.Features;
import com.eraldguri.geophysicslab.api.model.retrofit.ApiViewModel;
import com.eraldguri.geophysicslab.api.model.retrofit.RetrofitHelper;
import com.eraldguri.geophysicslab.navigation.EarthquakesFragment;
import com.eraldguri.geophysicslab.util.DividerItemDecorator;
import com.eraldguri.geophysicslab.util.StringUtils;

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
        setHasOptionsMenu(true);

        ApiViewModel viewModel = new ViewModelProvider(requireActivity()).get(ApiViewModel.class);
        viewModel.getFeatures().observe(getViewLifecycleOwner(), this::setupRecyclerView);

        return root;
    }

    private void initViews(View view) {
        mEarthquakeListView = view.findViewById(R.id.rv_earthquake_data_list);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        assert mEarthquakeListAdapter != null;
        String text;
        if (item.getItemId() == R.id.magnitude_all) {
            mEarthquakeListAdapter.getFilter().filter("");
        } else if (item.getItemId() == R.id.magnitude_3) {
            text = "3.";
            StringUtils.isDigit(text, mEarthquakeListAdapter);
        } else if (item.getItemId() == R.id.magnitude_4) {
            text = "4.";
            StringUtils.isDigit(text, mEarthquakeListAdapter);
        } else if (item.getItemId() == R.id.magnitude_5) {
            text = "5.";
            StringUtils.isDigit(text, mEarthquakeListAdapter);
        } else if (item.getItemId() == R.id.magnitude_strong) {
            text = "    ";
            String[] texts = new String[] {"6.", "7.", "8."};
            for (String s: texts) {
                if (text.contains(s)) {
                    if (Character.isDigit(texts[0].charAt(0))) {
                        mEarthquakeListAdapter.getFilter().filter(text);
                    }
                }
            }
        }

        return false;
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

}