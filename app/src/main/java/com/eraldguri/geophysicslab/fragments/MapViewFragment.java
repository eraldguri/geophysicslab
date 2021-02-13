package com.eraldguri.geophysicslab.fragments;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eraldguri.geophysicslab.R;
import com.eraldguri.geophysicslab.api.model.Features;
import com.eraldguri.geophysicslab.api.model.retrofit.RetrofitHelper;
import com.eraldguri.geophysicslab.mapview.MapViewInstance;
import com.eraldguri.geophysicslab.navigation.EarthquakesFragment;

import org.jetbrains.annotations.NotNull;
import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MapViewFragment extends EarthquakesFragment {

    private MapView mMapView = null;
    private MapViewInstance mapViewInstance;

    private final RetrofitHelper.ConnectionCallback mConnectionCallback = new RetrofitHelper.ConnectionCallback() {
        @Override
        public void onSuccess(List<Features> features) {
            System.out.print(features);
        }

        @Override
        public void onError(int code, String error) {
            Log.d("error", code + " " + error);
        }
    };

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_map_view, container, false);

        init(root);

        return root;
    }

    private void init(View view) {
        RetrofitHelper.callApi(mConnectionCallback);
        mapViewInstance = new MapViewInstance(getContext());

        mMapView = view.findViewById(R.id.map_view);

        MapViewInstance.mapViewConfigurations(mMapView);

        IMapController mapController = mMapView.getController();
        mapController.setZoom(9.5);
        GeoPoint startPoint = new GeoPoint(48.8583, 2.2944);
        mapController.setCenter(startPoint);

        Marker marker = new Marker(mMapView);
        marker.setPosition(startPoint);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        marker.setTitle("test");
        mMapView.getOverlays().add(marker);
    }

    private void addMarker() {

    }


}