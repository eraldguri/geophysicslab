package com.eraldguri.geophysicslab;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.eraldguri.geophysicslab.api.model.websocket.WebSocketBuilder;
import com.eraldguri.geophysicslab.util.NetworkStateReceiver;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

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

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

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
        WebSocketBuilder.closeWebSocketConnection();
    }
}