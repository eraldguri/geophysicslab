package com.eraldguri.geophysicslab.fragments.tabs;

import android.Manifest;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eraldguri.geophysicslab.R;
import com.eraldguri.geophysicslab.api.model.Earthquake;
import com.eraldguri.geophysicslab.api.model.Features;
import com.eraldguri.geophysicslab.api.model.retrofit.ApiViewModel;
import com.eraldguri.geophysicslab.mapview.MapViewInstance;
import com.eraldguri.geophysicslab.permissions.PermissionUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.M)
public class NearYouFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, GoogleMap.OnInfoWindowClickListener {

    private MapView mMapView;
    private static final String MAP_VIEW_KEY = "MapViewKey";
    private boolean cameraPositionUpdate;
    private GoogleMap mGoogleMap;

    PermissionUtil permissionUtil;

    protected Location mLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private LatLng earthquakePoint;
    private LatLng locationPoint;
    private Map<String, List<Features>> nearEarthquakes = new HashMap<>();;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_near_you, container, false);

        initGoogleMap(savedInstanceState, root);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        permissionUtil = new PermissionUtil(requireContext());

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

    private void initViews(View view) {
        mMapView = view.findViewById(R.id.map_view_quakes_near_you);
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
    }

    private void prepareData(List<Features> features) {
        for (int i = 0; i < features.size(); i++) {
            double[] coordinates = features.get(i).getGeometry().getCoordinates();
            double latitude = coordinates[1];
            double longitude = coordinates[0];
            earthquakePoint = new LatLng(latitude, longitude);
            LatLng latLng = new LatLng(latitude, longitude);

            double distance = calculateDistanceBetweenUserLocationAndEarthquake(earthquakePoint, locationPoint);
            List<Features> nearFeatures = new ArrayList<>();
            if (distance >= 0.0 && distance <= 15000) {
                nearFeatures.add(features.get(i));
                drawMarker(latLng);
            }

        }
    }

    private double calculateDistanceBetweenUserLocationAndEarthquake(LatLng startPoint, LatLng endPoint) {
        int earthRadius            = 6371;
        double startLatitude       = startPoint.latitude;
        double endLatitude         = endPoint.latitude;
        double startLongitude      = startPoint.longitude;
        double endLongitude        = endPoint.longitude;
        double latitudeDifference  = Math.toRadians(startLatitude - endLatitude);
        double longitudeDifference = Math.toRadians(startLongitude - endLongitude);

        double arc                 = Math.sin(latitudeDifference / 2) * Math.sin(latitudeDifference)
                                    + Math.cos(Math.toRadians(startLatitude))
                                    * Math.cos(Math.toRadians(endLatitude)) * Math.sin(longitudeDifference / 2)
                                    * Math.sin(longitudeDifference / 2);

        double c                   = 2 * Math.asin(Math.sqrt(arc));
        double result              = earthRadius * c;

        return formatDistanceInKM(result);
    }

    private double formatDistanceInKM(double distance) {
        double resultInKM          = distance / 1;
        double kmInDec = 0;
        try {
            DecimalFormat format = new DecimalFormat("####");
            kmInDec = Integer.parseInt(format.format(resultInKM));

            Log.i("km", "result: " + distance + " " + kmInDec);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return kmInDec;
    }

    private void drawMarker(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("My Location");
        mGoogleMap.addMarker(markerOptions);
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 2.0f));
    }

    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation().addOnCompleteListener(requireActivity(), task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                mLocation = task.getResult();
                locationPoint = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
               // drawMarker(locationPoint);

                ApiViewModel viewModel = new ViewModelProvider(requireActivity()).get(ApiViewModel.class);
                viewModel.getFeatures().observe(getViewLifecycleOwner(), this::prepareData);
            } else {
                Log.w("tag", "getLastLocation:exception", task.getException());
                Toast.makeText(requireContext(), "Location not detected", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();

        if (!permissionUtil.hasPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
            permissionUtil.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION);
        } else {
            getLastLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
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