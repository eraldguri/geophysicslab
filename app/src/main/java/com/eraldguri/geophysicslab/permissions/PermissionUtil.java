package com.eraldguri.geophysicslab.permissions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@RequiresApi(api = Build.VERSION_CODES.M)
public class PermissionUtil {

    private static final String TAG = PermissionUtil.class.getSimpleName();
    private static final String KEY_PREV_PERMISSIONS = "previous_permissions";
    private static final String KEY_IGNORED_PERMISSIONS = "ignored_permissions";
    private Context context;
    private static SharedPreferences sharedPreferences;
    private static final ArrayList<PermissionRequest> permissionRequests = new ArrayList<>();

    public PermissionUtil(Context context) {
        sharedPreferences = context.getSharedPreferences("runtimepermissionhelper", Context.MODE_PRIVATE);
        this.context = context;
    }

    /**
     * Check that all given permissions have been granted by verifying that each entry in the
     * given array is of the value {@link PackageManager#PERMISSION_GRANTED}.
     */
    public boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if the Activity has access to given permissions.
     */
    public boolean hasPermission(Activity activity, String permission) {
        return activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Returns true if the Activity has access to a all given permission.
     */
    public boolean hasPermission(Activity activity, String[] permissions) {
        for (String permission : permissions) {
            if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /*
     * If we override other methods, lets do it as well, and keep name same as it is already weird enough.
     * Returns true if we should show explanation why we need this permission.
     */
    public boolean shouldShowRequestPermissionRationale(Activity activity, String permissions) {
        return activity.shouldShowRequestPermissionRationale(permissions);
    }

    public void askForPermission(Activity activity, String permission, PermissionCallback permissionCallback) {
        askForPermission(activity, new String[]{permission}, permissionCallback);
    }

    public void askForPermission(Activity activity, String[] permissions, PermissionCallback permissionCallback) {
        if (permissionCallback == null) {
            return;
        }
        if (hasPermission(activity, permissions)) {
            permissionCallback.permissionGranted();
            return;
        }
        PermissionRequest permissionRequest = new PermissionRequest(new ArrayList<>(Arrays.asList(permissions)), permissionCallback);
        permissionRequests.add(permissionRequest);

        activity.requestPermissions(permissions, permissionRequest.getRequestCode());
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        PermissionRequest requestResult = new PermissionRequest(requestCode);
        if (permissionRequests.contains(requestResult)) {
            PermissionRequest permissionRequest = permissionRequests.get(permissionRequests.indexOf(requestResult));
            if (verifyPermissions(grantResults)) {
                //Permission has been granted
                permissionRequest.getPermissionCallback().permissionGranted();
            } else {
                permissionRequest.getPermissionCallback().permissionRefused();

            }
            permissionRequests.remove(requestResult);
        }
        refreshMonitoredList();
    }


    //Permission monitoring part below

    /**
     * Get list of currently granted permissions, without saving it inside Nammu
     *
     * @return currently granted permissions
     */
    public ArrayList<String> getGrantedPermissions() {
        if (context == null) {
            throw new RuntimeException("Must call init() earlier");
        }
        ArrayList<String> permissions = new ArrayList<>();
        ArrayList<String> permissionsGranted = new ArrayList<>();
        //Group location
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        //Group Calendar
        permissions.add(Manifest.permission.WRITE_CALENDAR);
        permissions.add(Manifest.permission.READ_CALENDAR);
        //Group Camera
        permissions.add(Manifest.permission.CAMERA);
        //Group Contacts
        permissions.add(Manifest.permission.WRITE_CONTACTS);
        permissions.add(Manifest.permission.READ_CONTACTS);
        permissions.add(Manifest.permission.GET_ACCOUNTS);
        //Group Microphone
        permissions.add(Manifest.permission.RECORD_AUDIO);
        //Group Phone
        permissions.add(Manifest.permission.CALL_PHONE);
        permissions.add(Manifest.permission.READ_PHONE_STATE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            permissions.add(Manifest.permission.READ_CALL_LOG);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            permissions.add(Manifest.permission.WRITE_CALL_LOG);
        }
        permissions.add(Manifest.permission.ADD_VOICEMAIL);
        permissions.add(Manifest.permission.USE_SIP);
        permissions.add(Manifest.permission.PROCESS_OUTGOING_CALLS);
        //Group Body sensors
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            permissions.add(Manifest.permission.BODY_SENSORS);
        }
        //Group SMS
        permissions.add(Manifest.permission.SEND_SMS);
        permissions.add(Manifest.permission.READ_SMS);
        permissions.add(Manifest.permission.RECEIVE_SMS);
        permissions.add(Manifest.permission.RECEIVE_WAP_PUSH);
        permissions.add(Manifest.permission.RECEIVE_MMS);
        //Group Storage
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        for (String permission : permissions) {
            if (context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                permissionsGranted.add(permission);
            }
        }
        return permissionsGranted;
    }

    /**
     * Refresh currently granted permission list, and save it for later comparing using @permissionCompare()
     */
    public void refreshMonitoredList() {
        ArrayList<String> permissions = getGrantedPermissions();
        Set<String> set = new HashSet<>(permissions);
        sharedPreferences.edit().putStringSet(KEY_PREV_PERMISSIONS, set).apply();
    }

    /**
     * Get list of previous Permissions, from last refreshMonitoredList() call and they may be outdated,
     * use getGrantedPermissions() to get current
     */
    public ArrayList<String> getPreviousPermissions() {
        return new ArrayList<>(sharedPreferences.getStringSet(KEY_PREV_PERMISSIONS, new HashSet<>()));
    }

    public ArrayList<String> getIgnoredPermissions() {
        return new ArrayList<>(sharedPreferences.getStringSet(KEY_IGNORED_PERMISSIONS, new HashSet<>()));
    }

    /**
     * Lets see if we already ignore this permission
     */
    public boolean isIgnoredPermission(String permission) {
        if (permission == null) {
            return false;
        }
        return getIgnoredPermissions().contains(permission);
    }

    /**
     * Use to ignore to particular Permission - even if user will deny or add it we won't receive a callback.
     *
     * @param permission Permission to ignore
     */
    public void ignorePermission(String permission) {
        if (!isIgnoredPermission(permission)) {
            ArrayList<String> ignoredPermissions = getIgnoredPermissions();
            ignoredPermissions.add(permission);
            Set<String> set = new HashSet<>(ignoredPermissions);
            sharedPreferences.edit().putStringSet(KEY_IGNORED_PERMISSIONS, set).apply();
        }
    }

    /**
     * Used to trigger comparing process - @permissionListener will be called each time Permission was revoked, or added (but only once).
     *
     * @param permissionListener Callback that handles all permission changes
     */
    public void permissionCompare(PermissionListener permissionListener) {
        if (context == null) {
            throw new RuntimeException("Before comparing permissions you need to call Nammu.init(context)");

        }
        ArrayList<String> previouslyGranted = getPreviousPermissions();
        ArrayList<String> currentPermissions = getGrantedPermissions();
        ArrayList<String> ignoredPermissions = getIgnoredPermissions();
        for (String permission : ignoredPermissions) {
            if (!previouslyGranted.isEmpty()) {
                previouslyGranted.remove(permission);
            }

            if (!currentPermissions.isEmpty()) {
                currentPermissions.remove(permission);
            }
        }
        for (String permission : currentPermissions) {
            if (previouslyGranted.contains(permission)) {
                //All is fine, was granted and still is
                previouslyGranted.remove(permission);
            } else {
                //We didn't have it last time
                if (permissionListener != null) {
                    permissionListener.permissionsChanged(permission);
                    permissionListener.permissionsGranted(permission);
                }
            }
        }
        if (!previouslyGranted.isEmpty()) {
            //Something was granted and removed
            for (String permission : previouslyGranted) {
                if (permissionListener != null) {
                    permissionListener.permissionsChanged(permission);
                    permissionListener.permissionsRemoved(permission);
                }
            }
        }
        refreshMonitoredList();
    }

    /**
     * Not that needed method but if we override others it is good to keep same.
     */
    public boolean checkPermission(String permissionName) {
        if (context == null) {
            throw new RuntimeException("Before comparing permissions you need to call Nammu.init(context)");
        }
        return PackageManager.PERMISSION_GRANTED == context.checkSelfPermission(permissionName);
    }
}
