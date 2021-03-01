package com.eraldguri.geophysicslab.util;

import android.os.Environment;
import android.util.Log;

import com.eraldguri.geophysicslab.adapter.EarthquakeListAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    //public static final String csvName = "earthquakes";
    public static final String csvExtension = ".csv";
    public static final String csvDirectory = "GeophysicsLab";

    private static int count = 0;

    public static String[] header = {
            "Place", "Felt", "Date/Time", "Magnitude", "Latitude", "Longitude", "Depth",
            "cdi", "mmi", "sig", "nst", "dmin", "rms", "gap", "Magnitude Type", "Tsunami"
    };

    public static void isDigit(String text, EarthquakeListAdapter earthquakeListAdapter) {
        if (Character.isDigit(text.charAt(0))) {
            earthquakeListAdapter.getFilter().filter(text);
        }
    }

}
