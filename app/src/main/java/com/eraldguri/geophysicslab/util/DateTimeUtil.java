package com.eraldguri.geophysicslab.util;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.O)
public class DateTimeUtil {

    public static String parseDateTimFromString(String dateTimeString) {
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(dateTimeString);
        Instant instant = offsetDateTime.toInstant();
        Date date = Date.from(instant);

        String dateTime = date.toString();
        String[] dateString = dateTime.split("\\+s");
        String outDate = dateString[0] + " " + dateString[1] + " " + dateString[2] + "      " + dateString[3];

        return outDate;
    }
}
