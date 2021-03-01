package com.eraldguri.geophysicslab.util;

import android.content.Context;
import android.view.View;

import com.eraldguri.geophysicslab.R;
import com.google.android.material.snackbar.Snackbar;

public class GeoSnackBar {

    public static void successSnackBar(Context context, View view, int text, int duration) {
        Snackbar snackbar = Snackbar.make(view, text, duration);
        snackbar.setTextColor(context.getResources().getColor(R.color.green));
        snackbar.show();
    }

    public static void errorSnackBar(Context context, View view, int text, int duration) {
        Snackbar snackbar = Snackbar.make(view, text, duration);
        snackbar.setTextColor(context.getResources().getColor(R.color.red));
        snackbar.show();
    }

    public static void errorSnackBar(Context context, View view, String text, int duration) {
        Snackbar snackbar = Snackbar.make(view, text, duration);
        snackbar.setTextColor(context.getResources().getColor(R.color.red));
        snackbar.show();
    }

}
