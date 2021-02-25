package com.eraldguri.geophysicslab.fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eraldguri.geophysicslab.R;
import com.eraldguri.geophysicslab.api.model.retrofit.ApiViewModel;
import com.eraldguri.geophysicslab.fragments.tabs.EarthquakeListFragment;
import com.eraldguri.geophysicslab.mapview.MapViewInstance;
import com.eraldguri.geophysicslab.util.DateTimeUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class EarthquakeFragment extends Fragment implements OnMapReadyCallback {

    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private static final String MAP_VIEW_KEY = "MapViewKey";
    private boolean cameraPositionUpdate;
    private static final String BACK_STACK_ROOT_TAG = "earthquake_fragment";

    private TextView rowMagnitude, rowRegion, rowDepth, rowDate;

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
        rowMagnitude = view.findViewById(R.id.row_magnitude);
        rowRegion = view.findViewById(R.id.row_region);
        rowMagnitude = view.findViewById(R.id.row_magnitude);
        rowDepth = view.findViewById(R.id.row_depth);
        rowDate = view.findViewById(R.id.row_date);
    }

    private void setupMap() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String region = bundle.getString("title");
            double magnitude = bundle.getDouble("magnitude");
            double latitude = bundle.getDouble("latitude");
            double longitude = bundle.getDouble("longitude");
            String dateTime = bundle.getString("date_time");
            double depth = bundle.getDouble("depth");

            LatLng latLng = new LatLng(latitude, longitude);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            mGoogleMap.addMarker(markerOptions);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 8.0f));

            // set TextViews
            rowMagnitude.setText(String.valueOf(magnitude));
            rowRegion.setText(region);
            rowDepth.setText(depth + " km");
            rowDate.setText(dateTime);
        }
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