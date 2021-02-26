package com.eraldguri.geophysicslab.util;

import com.eraldguri.geophysicslab.adapter.EarthquakeListAdapter;

public class StringUtils {

    public static void isDigit(String text, EarthquakeListAdapter earthquakeListAdapter) {
        if (Character.isDigit(text.charAt(0))) {
            earthquakeListAdapter.getFilter().filter(text);
        }
    }
}
