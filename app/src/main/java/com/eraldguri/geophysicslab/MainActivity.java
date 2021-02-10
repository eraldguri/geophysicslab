package com.eraldguri.geophysicslab;

import android.Manifest;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.eraldguri.geophysicslab.api.model.websocket.WebSocketBuilder;
import com.eraldguri.geophysicslab.permissions.PermissionCallback;
import com.eraldguri.geophysicslab.permissions.PermissionUtil;
import com.eraldguri.geophysicslab.util.DeviceUtils;
import com.eraldguri.geophysicslab.util.NetworkStateReceiver;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MainActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener,
        WebSocketBuilder.JsonObjectFromWebSocket {

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;

    // Receiver that detects network state changes
    private NetworkStateReceiver mNetworkStateReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*if (DeviceUtils.isMarshmallow()) {
            checkForPermissions();
        } else {
            Toast.makeText(MainActivity.this, "LOAD", Toast.LENGTH_LONG).show();
        }*/

        startNetworkBroadcastReceiver(this);
        initViews();
        startNavigationMenu();

        checkForPermissions();
    }

    /**
     * @brief
     *      Initialize View IDs for each elements of the Views contained in the layout.
     */
    private void initViews() {
        mDrawer = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);

        WebSocketBuilder.startWebSocketConnection(this);
    }

    /**
     * @brief
     *      This method allows to start the navigation drawer and to
     *      navigate between the fragments contained in the navigation drawer.
     */
    private void startNavigationMenu() {
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_earthquakes)
                .setOpenableLayout(mDrawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(mNavigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onPause() {
        unregisterNetworkBroadcastReceiver(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        registerNetworkBroadcastReceiver(this);
        super.onResume();
    }

    public void startNetworkBroadcastReceiver(Context context) {
        mNetworkStateReceiver = new NetworkStateReceiver();
        mNetworkStateReceiver.addListener((NetworkStateReceiver.NetworkStateReceiverListener) context);
        registerNetworkBroadcastReceiver(context);
    }

    /**
    * Register the NetworkStateReceiver with your activity
    * */
    private void registerNetworkBroadcastReceiver(Context context) {
        context.registerReceiver(mNetworkStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    /**
    * Unregister the NetworkStateReceiver with your activity
    * */
    public void unregisterNetworkBroadcastReceiver(Context context) {
        context.unregisterReceiver(mNetworkStateReceiver);
    }

    @Override
    public void networkAvailable() {
        Log.i("TAG", "networkAvailable()");
    }

    @Override
    public void networkUnavailable() {
        Log.i("TAG", "networkUnavailable()");
        Snackbar snackbar = Snackbar.make(mDrawer, "No Internet Connection", Snackbar.LENGTH_LONG);
        snackbar.setTextColor(getResources().getColor(R.color.red));
        snackbar.show();
        //TODO:: https://gist.github.com/voghDev/71bb95a2525e7e9782b4
    }

    private final PermissionCallback mPermissionReadStorageCallback = new PermissionCallback() {
        @Override
        public void permissionGranted() {
            Toast.makeText(MainActivity.this, "LOAD", Toast.LENGTH_LONG).show();
        }

        @Override
        public void permissionRefused() {
            finish();
        }
    };

    private void checkForPermissions() {
        PermissionUtil.init(getApplicationContext());
        if (PermissionUtil.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                && PermissionUtil.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "LOAD", Toast.LENGTH_LONG).show();
        } else {
            if (PermissionUtil.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    && PermissionUtil.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Snackbar snackbar = Snackbar.make(mDrawer,
                        "Geophysics Lab need to read and write your storage to work properly",  Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", v -> PermissionUtil.askForPermission(MainActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        mPermissionReadStorageCallback));
                snackbar.setTextColor(getResources().getColor(R.color.red));
                snackbar.show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void passData(JSONObject jsonObject) {
        Log.d("data: ", jsonObject.toString());
        parseJsonObject(jsonObject);
        WebSocketBuilder.closeWebSocketConnection();
    }

    private void parseJsonObject(JSONObject jsonObject) {
        try {
            JSONArray dataArray = jsonObject.getJSONArray("data");
            Log.d("data:", dataArray.toString());
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject dataObject = dataArray.getJSONObject(i);
                JSONObject propertiesObject = dataObject.getJSONObject("properties");
                double latitude = propertiesObject.getDouble("lat");
                double longitude = propertiesObject.getDouble("lon");
                double magnitude = propertiesObject.getDouble("mag");
                String flynn_region = propertiesObject.getString("flynn_region");
                Log.d("tag",
                        "latitude: " + latitude + " " + "longitude: " + longitude
                        + " " + "magnitude: " + magnitude + " " + "flynn_region: " + flynn_region);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}