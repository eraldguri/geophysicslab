package com.eraldguri.geophysicslab.util;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@RequiresApi(api = Build.VERSION_CODES.O)
public class DateTimeUtil {

    private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm'Z'";

    public static String parseDateTimeFromString(String dateTimeString) {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        DateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN, Locale.US);
        dateFormat.setTimeZone(timeZone);

        String formatted = dateFormat.format(new Date());
        String dateString = formatted.substring(0, 10);
        String timeString = formatted.replace('T', ' ').substring(11, 16);

        return dateString + "   " + timeString;
        //2021-02-08T20:33Z
    }

}
