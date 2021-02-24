package com.eraldguri.geophysicslab;

import android.Manifest;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.eraldguri.geophysicslab.api.model.Earthquake;
import com.eraldguri.geophysicslab.api.model.Features;
import com.eraldguri.geophysicslab.api.model.retrofit.ApiViewModel;
import com.eraldguri.geophysicslab.api.model.retrofit.HttpRequestHelper;
import com.eraldguri.geophysicslab.api.model.websocket.WebSocketBuilder;
import com.eraldguri.geophysicslab.permissions.PermissionsUtil;
import com.eraldguri.geophysicslab.util.NetworkStateReceiver;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        startNetworkBroadcastReceiver(this);
        initViews();
        startNavigationMenu();

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