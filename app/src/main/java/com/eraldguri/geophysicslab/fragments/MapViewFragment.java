package com.eraldguri.geophysicslab.fragments;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eraldguri.geophysicslab.R;
import com.eraldguri.geophysicslab.api.model.Features;
import com.eraldguri.geophysicslab.api.model.retrofit.ApiViewModel;
import com.eraldguri.geophysicslab.api.model.retrofit.RetrofitHelper;
import com.eraldguri.geophysicslab.mapview.MapViewInstance;
import com.eraldguri.geophysicslab.navigation.EarthquakesFragment;

import org.jetbrains.annotations.NotNull;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.simplefastpoint.LabelledGeoPoint;
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlay;
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlayOptions;
import org.osmdroid.views.overlay.simplefastpoint.SimplePointTheme;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MapViewFragment extends EarthquakesFragment {

    private MapView mMapView = null;
    private MapViewInstance mapViewInstance;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_map_view, container, false);

        init(root);


        return root;
    }

    private void init(View view) {
        mapViewInstance = new MapViewInstance(getContext());

        mMapView = view.findViewById(R.id.map_view);

        MapViewInstance.mapViewConfigurations(mMapView);

        IMapController mapController = mMapView.getController();
        mapController.setZoom(9.5);
        GeoPoint startPoint = new GeoPoint(48.8583, 2.2944);
        mapController.setCenter(startPoint);

        ApiViewModel viewModel = new ViewModelProvider(requireActivity()).get(ApiViewModel.class);
        viewModel.getFeatures().observe(getViewLifecycleOwner(), this::addMarker);
    }

    private void addMarker(List<Features> features) {
        List<GeoPoint> geoPoints = new ArrayList<>();
        if (features != null) {
            for (int i = 0; i < features.size(); ++i) {
                String title = features.get(i).getProperties().getTitle();
                double[] coordinates = features.get(i).getGeometry().getCoordinates();
                double latitude = coordinates[0];
                double longitude = coordinates[1];
                GeoPoint point = new GeoPoint(latitude, longitude);
                geoPoints.add(i, point);
                addGeoPoints(title, latitude, longitude, geoPoints);

                if (geoPoints != null) {
                    double north = -90;
                    double south = 90;
                    double west = 180;
                    double east = -180;

                }
            }
        }
    }

    private void addGeoPoints(String title, double latitude, double longitude, List<GeoPoint> geoPoints) {
        GeoPoint position = new GeoPoint(latitude, longitude);
        Marker marker = new Marker(mMapView);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setPosition(position);
        marker.setTitle(title);
        mMapView.getOverlays().add(marker);
        geoPoints.add(position);
    }

}