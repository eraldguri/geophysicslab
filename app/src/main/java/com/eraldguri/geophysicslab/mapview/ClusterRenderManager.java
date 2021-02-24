package com.eraldguri.geophysicslab.mapview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.eraldguri.geophysicslab.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.ClusterRenderer;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class ClusterRenderManager extends DefaultClusterRenderer<ClusterItem> {

    private static final int MIN_CLUSTER_SIZE = 2;
    private final Context mContext;
    private GoogleMap googleMap;

    private static double magnitude;

    public ClusterRenderManager(Context context, GoogleMap map, ClusterManager<ClusterItem> clusterManager) {
        super(context, map, clusterManager);

        mContext = context;
        googleMap = map;
    }

    @Override
    protected void onBeforeClusterItemRendered(@NonNull ClusterItem item, @NonNull MarkerOptions markerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions);

        if (item.getSnippet() != null) {
            magnitude = Double.parseDouble(item.getSnippet());
            markerOptions.title(item.getTitle());
            markerOptions.snippet(item.getSnippet());
            markerOptions.icon(drawMarkerPoints(mContext));
        }
    }

    @Override
    protected void onBeforeClusterRendered(@NonNull Cluster<ClusterItem> cluster, @NonNull MarkerOptions markerOptions) {
        super.onBeforeClusterRendered(cluster, markerOptions);
    }

    @Override
    protected void onClusterItemRendered(@NonNull ClusterItem clusterItem, @NonNull Marker marker) {
        super.onClusterItemRendered(clusterItem, marker);
        //mSelectedMarker = marker;
    }

    @Override
    protected void onClusterRendered(@NonNull Cluster<ClusterItem> cluster, @NonNull Marker marker) {
        super.onClusterRendered(cluster, marker);
    }

    @Override
    protected boolean shouldRenderAsCluster(@NonNull Cluster<ClusterItem> cluster) {
        return cluster.getSize() > MIN_CLUSTER_SIZE;
    }

    private BitmapDescriptor drawMarkerPoints(Context context) {
        Drawable markerIcon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_fiber_manual_record_24);
        Bitmap bitmap = null;
        if (markerIcon != null) {
            if (magnitude >= 0.0 && magnitude < 2) {
                markerIcon.setBounds(0, 0 , 30, 30);
            } else if (magnitude >= 2.0 && magnitude < 3) {
                markerIcon.setBounds(0, 0 , 40, 40);
            } else if (magnitude >= 3.0 && magnitude < 4) {
                markerIcon.setBounds(0, 0 , 50, 50);
            } else if (magnitude >= 4.0 && magnitude <= 5.9) {
                markerIcon.setBounds(0, 0 , 60, 60);
            } else if (magnitude >= 6) {
                markerIcon.setBounds(0, 0 , 70, 70);
            }

            bitmap = Bitmap.createBitmap(markerIcon.getIntrinsicWidth(),
                    markerIcon.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            markerIcon.draw(canvas);
        }
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
