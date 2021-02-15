package com.eraldguri.geophysicslab.mapview;

import android.content.Context;
import android.preference.PreferenceManager;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.config.IConfigurationProvider;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.TileSystem;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;

import java.io.File;

public class MapViewInstance extends MapView {

    private static MapView mMapView;

    private static final double north = -41.033770;
    private static final double east = -71.591065;
    private static final double south = -41.207056;
    private static final double west = -71.111444;

    public MapViewInstance(Context context) {
        super(context);
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
        IConfigurationProvider mOsmConfig = Configuration.getInstance();
        mOsmConfig.load(context, PreferenceManager.getDefaultSharedPreferences(context));
        File basePath = new File(getContext().getCacheDir().getAbsolutePath(), "osmdroid");
        mOsmConfig.setOsmdroidBasePath(basePath);
        File tileCache = new File(mOsmConfig.getOsmdroidBasePath().getAbsolutePath(), "tile");
        mOsmConfig.setOsmdroidTileCache(tileCache);
    }

    public static void mapViewConfigurations(MapView mapView) {
        mMapView = mapView;
        BoundingBox mBoundingBox = new BoundingBox(north, east, south, west);
        mapView.setScrollableAreaLimitDouble(mBoundingBox);
        mMapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        mMapView.setTilesScaledToDpi(true);
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
