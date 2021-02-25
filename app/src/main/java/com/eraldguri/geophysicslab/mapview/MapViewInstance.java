package com.eraldguri.geophysicslab.mapview;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class MapViewInstance {

    private static final String TAG = "Tag";
    private final Context mContext;
    private static GoogleMap mGoogleMap;

    public MapViewInstance(Context context, GoogleMap googleMap) {
        mContext = context;
        mGoogleMap = googleMap;
    }

    public static void cameraConfigurations(LatLng latLng) {
        CameraPosition.Builder cameraBuilder = CameraPosition.builder();
        cameraBuilder.bearing(45);
        cameraBuilder.tilt(30);
        cameraBuilder.target(latLng);
        cameraBuilder.zoom(20.0f);
    }

    public static void mapViewConfigurations(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMinZoomPreference(4.0f);
        mGoogleMap.setIndoorEnabled(true);

        UiSettings uiSettings = mGoogleMap.getUiSettings();
        uiSettings.setIndoorLevelPickerEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setScrollGesturesEnabled(true);
    }
}
