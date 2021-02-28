package com.eraldguri.geophysicslab.util;

import android.util.Log;

import com.eraldguri.geophysicslab.adapter.EarthquakeListAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static final String csvName = "earthquakes";
    public static final String csvExtension = ".csv";
    public static final String csvDirectory = "/GeophysicsLab";

    public static String[] header = {
            "Place", "Felt", "Date/Time", "Magnitude", "Latitude", "Longitude", "Depth",
            "cdi", "mmi", "sig", "nst", "dmin", "rms", "gap", "Magnitude Type", "Tsunami"
    };

    final static Pattern PATTERN = Pattern.compile("(.*?)(?:\\((\\d+)\\))?(\\.[^.]*)?");

    public static void isDigit(String text, EarthquakeListAdapter earthquakeListAdapter) {
        if (Character.isDigit(text.charAt(0))) {
            earthquakeListAdapter.getFilter().filter(text);
        }
    }

    public static void replaceWithNewFileName(String filename, File directory, String extension) {
        String[] filenames = new File(directory.toString()).list();
        ArrayList<String> files = new ArrayList<>();
        assert filenames != null;
        for (String file: filenames) {
            if (file.matches(filename + "[0-9]+\\." + extension + "|" + filename + "\\." + extension)) {
                files.add(file);
            }
        }
        files.trimToSize();

        int highestNumber = 0;
        int digit;
        for (String file: files) {
            try {
                digit = Integer.parseInt(file.replace(filename, "").replace("." + extension, ""));
            } catch (NumberFormatException e) {
                digit = 1;
            }

            if (digit > highestNumber) {
                highestNumber = digit;
            }
        }

        filename = filename + highestNumber++ + "." + extension;
        File mFile = new File(directory.toString(), filename);

        System.out.println(mFile);
    }
}
