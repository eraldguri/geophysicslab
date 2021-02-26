package com.eraldguri.geophysicslab.fragments.tabs;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.eraldguri.geophysicslab.adapter.EarthquakeListAdapter;
import com.eraldguri.geophysicslab.R;
import com.eraldguri.geophysicslab.api.model.Features;
import com.eraldguri.geophysicslab.api.model.retrofit.ApiViewModel;
import com.eraldguri.geophysicslab.fragments.EarthquakeFragment;
import com.eraldguri.geophysicslab.navigation.EarthquakesFragment;
import com.eraldguri.geophysicslab.util.DateTimeUtil;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(int position, List<Features> features) {
        Features quakes = features.get(position);

        FragmentManager fragmentManager = getChildFragmentManager();
        Fragment earthquakeFragment = new EarthquakeFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.earthquake_list_container, earthquakeFragment)
                .commit();

        double[] coordinates = quakes.getGeometry().getCoordinates();
        String dateTime = quakes.getProperties().getTime();
        String formattedTime = DateTimeUtil.parseDateTimeFromString(dateTime);

        Bundle bundle = new Bundle();
        bundle.putString("title", quakes.getProperties().getPlace());
        bundle.putDouble("magnitude", quakes.getProperties().getMagnitude());
        bundle.putDouble("latitude", coordinates[1]);
        bundle.putDouble("longitude", coordinates[0]);
        bundle.putDouble("depth", coordinates[2]);
        bundle.putString("date_time", formattedTime);
        bundle.putInt("tsunami", quakes.getProperties().getTsunami());
        bundle.putString("felt", quakes.getProperties().getFelt());
        bundle.putInt("tz", quakes.getProperties().getTz());
        bundle.putString("cdi", quakes.getProperties().getCdi());
        bundle.putString("mmi", quakes.getProperties().getMmi());
        bundle.putInt("sig", quakes.getProperties().getSig());
        bundle.putInt("nst", quakes.getProperties().getNst());
        bundle.putDouble("dmin", quakes.getProperties().getDmin());
        bundle.putDouble("rms",  quakes.getProperties().getRms());
        bundle.putDouble("gap", quakes.getProperties().getGap());
        bundle.putString("magType", quakes.getProperties().getMagType());

        earthquakeFragment.setArguments(bundle);
    }

}