package com.eraldguri.geophysicslab.permissions;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class PermissionRequest {
    private static Random random;
    private ArrayList<String> permissions;
    private final int requestCode;
    private PermissionCallback mPermissionCallback;

    public PermissionRequest(ArrayList<String> permissions, PermissionCallback permissionCallback) {
        this.permissions = permissions;
        mPermissionCallback = permissionCallback;
        if (random == null) {
            random = new Random();
        }
        this.requestCode = random.nextInt(32768);
    }

    public ArrayList<String> getPermissions() {
        return permissions;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public PermissionCallback getPermissionCallback() {
        return mPermissionCallback;
    }

    public PermissionRequest(int requestCode) {
        this.requestCode = requestCode;
    }

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object instanceof PermissionRequest) {
            return ((PermissionRequest) object).requestCode == this.requestCode;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(requestCode);
    }
}
