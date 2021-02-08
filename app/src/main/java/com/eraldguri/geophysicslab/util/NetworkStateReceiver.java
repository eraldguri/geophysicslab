package com.eraldguri.geophysicslab.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * This class defines a BroadcastReceiver which allows us to register for system or application
 * events. All registered receivers for an event are notified by the Android runtime once this
 * event happens.
 * */
public class NetworkStateReceiver extends BroadcastReceiver {

    protected List<NetworkStateReceiverListener> mNetworkListeners;
    protected Boolean connected;
    private final String TAG = "NetworkStateReceiver";

    public NetworkStateReceiver() {
        mNetworkListeners = new ArrayList<>();
        connected = null;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Intent broadcast received");
        if (intent == null || intent.getExtras() == null) {
            return;
        }

        // Retrieve a ConnectivityManager for handling management of network connections
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        /*
         * Details about the currently active default data network. When connected, this network is
         * the default route for outgoing connections
         * */
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        /*
        * getActiveNetworkInfo() may return null when there is no default network e.g. Airplane mode
        * */
        if (networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
            //Boolean that indicates whether there is a complete lack of connectivity
        } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)) {
            connected = false;
        }
        
        notifyStateToAll();
    }

    /*
    * Notify the state to all needed methods
    * */
    private void notifyStateToAll() {
        Log.i(TAG, "Notifying state to " + mNetworkListeners.size() + " listener(s)");
        for (NetworkStateReceiverListener eachNetworkStateReceiverListener: mNetworkListeners) {
            notifyState(eachNetworkStateReceiverListener);
        }
    }

    /**
     * Notify the network state, triggering interface functions based on the current state
     *
     * @param networkStateReceiverListener
     *          Object which implements the NetworkStateReceiverListener interface
     * */
    private void notifyState(NetworkStateReceiverListener networkStateReceiverListener) {
        if (connected == null || networkStateReceiverListener == null)
            return;

        if (connected) {
            // Triggering function on the interface towards network availability
            networkStateReceiverListener.networkAvailable();
        } else {
            // Triggering function on the interface towards network being unavailable
            networkStateReceiverListener.networkUnavailable();
        }
    }

    /*
    * Adds a listener to the list so that it will receive connection state change updates.
    *
    * @param networkStateReceiverListener
    *           Object which implements the NetworkStateReceiverListener interface
    * */
    public void addListener(NetworkStateReceiverListener networkStateReceiverListener) {
        Log.i(TAG, "addListener() - listeners.add(networkStateReceiverListener) + notifyState(networkStateReceiverListener);");
        mNetworkListeners.add(networkStateReceiverListener);
        notifyState(networkStateReceiverListener);
    }

    /*
    * Removes listener (which is no longer necessary) from the list so that it will no longer
    * received connection state change updates
    *
    * @param networkStateReceiverListener
    *           Object which implements the NetworkStateReceiverListener interface
    * */
    public void removeListener(NetworkStateReceiverListener networkStateReceiverListener) {
        mNetworkListeners.remove(networkStateReceiverListener);
    }

    /**
     * Inner interface which handles connection state changes for classes which registered this
     * receiver. This interface implements the "Strategy Pattern", where an execution strategy is
     * evaluated and applied internally at runtime.
     * */
    public interface NetworkStateReceiverListener {
        /**
         * When the connection state is changed and there is a connection, this method is called.
         * */
        void networkAvailable();

        /**
         * Connection state is changed and there is not a connection, this method is called.
         * */
        void networkUnavailable();
    }
}
