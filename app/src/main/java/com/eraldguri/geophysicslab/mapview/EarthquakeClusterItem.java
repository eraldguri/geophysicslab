package com.eraldguri.geophysicslab.mapview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class EarthquakeClusterItem implements ClusterItem {

    private final LatLng mPosition;
    private String mTitle;
    private String mSnippet;

    public EarthquakeClusterItem(double lat, double lon, String title, String snippet) {
        mPosition = new LatLng(lat, lon);
        mTitle = title;
        mSnippet = snippet;
    }

    public EarthquakeClusterItem(double lat, double lon) {
        mPosition = new LatLng(lat, lon);
    }

    @NonNull
    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Nullable
    @Override
    public String getTitle() {
        return mTitle;
    }

    @Nullable
    @Override
    public String getSnippet() {
        return mSnippet;
    }
}
