package com.eraldguri.geophysicslab.fragments.tabs;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.eraldguri.geophysicslab.R;
import com.eraldguri.geophysicslab.api.model.Features;
import com.eraldguri.geophysicslab.api.model.retrofit.ApiViewModel;
import com.eraldguri.geophysicslab.mapview.ClusterRenderManager;
import com.eraldguri.geophysicslab.mapview.EarthquakeClusterItem;
import com.eraldguri.geophysicslab.mapview.MapViewInstance;
import com.eraldguri.geophysicslab.navigation.EarthquakesFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.algo.NonHierarchicalDistanceBasedAlgorithm;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MapViewFragment extends EarthquakesFragment implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, GoogleMap.OnInfoWindowClickListener {

    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private static final String MAP_VIEW_KEY = "MapViewKey";
    private boolean cameraPositionUpdate;
    private final List<Marker> markers = new ArrayList<>();
    private ClusterManager<ClusterItem> mClusterManager;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_map_view, container, false);

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
        mMapView = view.findViewById(R.id.map_view_quakes);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if (!marker.isInfoWindowShown()) {
            marker.showInfoWindow();
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (cameraPositionUpdate) {
            cameraPositionUpdate = false;
            mGoogleMap.moveCamera(CameraUpdateFactory.zoomTo(18));
        } else {
            cameraPositionUpdate = true;
            mGoogleMap.moveCamera(CameraUpdateFactory.zoomTo(15));
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
        ApiViewModel viewModel = new ViewModelProvider(requireActivity()).get(ApiViewModel.class);
        viewModel.getFeatures().observe(getViewLifecycleOwner(), this::prepareMap);
    }

    private void prepareMap(List<Features> features) {
        if (mClusterManager == null) {
            mClusterManager = new ClusterManager<>(requireContext(), mGoogleMap);
        }
        mClusterManager.clearItems();
        addMarkers(features);
    }

    private void addMarkers(List<Features> earthquakes) {
        EarthquakeClusterItem earthquakeClusterItem;
        if (earthquakes != null) {
            List<ClusterItem> clusterItems = new ArrayList<>();
            for (int i = 0; i < earthquakes.size(); i++) {
                String title = earthquakes.get(i).getProperties().getTitle();
                double magnitude = earthquakes.get(i).getProperties().getMagnitude();
                double[] coordinates = earthquakes.get(i).getGeometry().getCoordinates();
                double latitude = coordinates[1];
                double longitude = coordinates[0];

                MarkerOptions markerOptions = new MarkerOptions();
                LatLng latLng = new LatLng(latitude, longitude);
                markerOptions.position(latLng);
                Marker marker = mGoogleMap.addMarker(markerOptions);
                MapViewInstance.cameraConfigurations(latLng);

                earthquakeClusterItem = new EarthquakeClusterItem(latitude, longitude, title, String.valueOf(magnitude));
                marker.remove();
                marker.showInfoWindow();
                clusterItems.add(earthquakeClusterItem);
                mClusterManager.addItem(earthquakeClusterItem);

                mGoogleMap.setOnMarkerClickListener(mClusterManager);
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 2.0f));
                markers.add(marker);

                setClusterManager(mClusterManager, clusterItems);
                mGoogleMap.setOnCameraIdleListener(mClusterManager);
            }
        }
    }

    private void setClusterManager(ClusterManager<ClusterItem> clusterManager, List<ClusterItem> clusterItems) {
        mClusterManager = clusterManager;
        ClusterRenderManager clusterRenderManager = new ClusterRenderManager(getContext(), mGoogleMap, mClusterManager);
        mClusterManager.addItems(clusterItems);
        mClusterManager.setAlgorithm(new NonHierarchicalDistanceBasedAlgorithm<>());
        mClusterManager.setRenderer(clusterRenderManager);
        mClusterManager.setOnClusterItemClickListener(item -> false);
        mClusterManager.setOnClusterClickListener(cluster -> false);
        mClusterManager.cluster();
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