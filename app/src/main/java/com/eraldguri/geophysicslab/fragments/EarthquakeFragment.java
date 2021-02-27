package com.eraldguri.geophysicslab.fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.eraldguri.geophysicslab.R;
import com.eraldguri.geophysicslab.adapter.ExpandableListViewAdapter;
import com.eraldguri.geophysicslab.fragments.tabs.EarthquakeListFragment;
import com.eraldguri.geophysicslab.mapview.MapViewInstance;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EarthquakeFragment extends Fragment implements OnMapReadyCallback {

    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private static final String MAP_VIEW_KEY = "MapViewKey";
    private static final String BACK_STACK_ROOT_TAG = "earthquake_fragment";

    private ExpandableListView expandableListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_earthquake, container, false);

        requireActivity().getOnBackPressedDispatcher()
                .addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void handleOnBackPressed() {
                        backStack();
                    }
                });

        initGoogleMap(savedInstanceState, root);

        return root;
    }

    private void initGoogleMap(Bundle savedInstanceState, View view) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_KEY);
        }
        initViews(view);
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);
    }

    private void initViews(View view){
        mMapView = view.findViewById(R.id.mapView_for_quake);
        expandableListView = view.findViewById(R.id.expandableListView);
    }

    private void setupMap() {
        Bundle bundle = this.getArguments();
        HashMap<String, List<String>> expandableListDetail = getData(bundle);
        List<String> expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        ExpandableListViewAdapter expandableListAdapter = new ExpandableListViewAdapter(requireContext(),
                expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(groupPosition -> {

        });

        expandableListView.setOnGroupCollapseListener(groupPosition -> {

        });
    }

    private HashMap<String, List<String>> getData(Bundle bundle) {
        HashMap<String, List<String>> _expandableListDetail = null;
        if (bundle != null) {
            String region = bundle.getString("title");
            double magnitude = bundle.getDouble("magnitude");
            double latitude = bundle.getDouble("latitude");
            double longitude = bundle.getDouble("longitude");
            String dateTime = bundle.getString("date_time");
            double depth = bundle.getDouble("depth");
            int tsunami = bundle.getInt("tsunami");
            String felt = bundle.getString("felt");

            int tz = bundle.getInt("tz");
            String cdi = bundle.getString("cdi");
            String mmi = bundle.getString("mmi");
            int sig = bundle.getInt("sig");
            int nst = bundle.getInt("nst");
            double dmin = bundle.getDouble("dmin");
            double rms = bundle.getDouble("rms");
            double gap = bundle.getDouble("gap");
            String magType = bundle.getString("magType");

            LatLng latLng = new LatLng(latitude, longitude);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            mGoogleMap.addMarker(markerOptions);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 8.0f));

            _expandableListDetail = new HashMap<>();
            List<String> geometry = new ArrayList<>();
            geometry.add("Latitude " + latitude);
            geometry.add("Longitude " + longitude);
            geometry.add("Depth " + depth);

            List<String> properties = new ArrayList<>();
            properties.add("Region    " + region);
            properties.add("Magnitude " + magnitude);
            properties.add("Date/Time " + dateTime);

            if (tsunami == 1) {
                properties.add("Tsunami " + "Yes");
            } else {
                properties.add("Tsunami " + "No");
            }

            if (felt == null) {
                properties.add("Intensity " + "No");
            } else {
                properties.add("Intensity " + felt);
            }

            properties.add("tz " + tz);
            properties.add("cdi " + cdi);
            properties.add("mmi " + mmi);
            properties.add("sig " + sig);
            properties.add("nst " + nst);
            properties.add("dmin " + dmin);
            properties.add("rms " + rms);
            properties.add("gap " + gap);
            properties.add("magType " + magType);

            _expandableListDetail.put("geometry", geometry);
            _expandableListDetail.put("properties", properties);
        }

        return _expandableListDetail;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        MapViewInstance.mapViewConfigurations(googleMap);
        if (mGoogleMap != null) {
            mGoogleMap.clear();
        } else {
            Log.d("TAG", "google map is null");
        }
        setupMap();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void backStack() {
        FragmentManager fragmentManager = getChildFragmentManager();
        Fragment quakeFragment = new EarthquakeListFragment();
        fragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_earthquake_map_container, quakeFragment)
                .addToBackStack(BACK_STACK_ROOT_TAG)
                .commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}