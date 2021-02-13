package com.eraldguri.geophysicslab.mapview;

import android.content.Context;
import android.preference.PreferenceManager;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;

public class MapViewInstance extends MapView {

    private static MapView mMapView;

    public MapViewInstance(Context context) {
        super(context);
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
    }

    public static void mapViewConfigurations(MapView mapView) {
        mMapView = mapView;
        mMapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        mMapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT);
        mMapView.setMultiTouchControls(true);

        mMapView.setHorizontalMapRepetitionEnabled(false);
        mMapView.setVerticalMapRepetitionEnabled(false);
        mMapView.setScrollableAreaLimitLatitude(MapView.getTileSystem().getMaxLatitude(),
                MapView.getTileSystem().getMinLatitude(), 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }
}
