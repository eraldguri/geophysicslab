package com.eraldguri.geophysicslab.util;

import android.content.Context;
import android.util.Log;

import com.eraldguri.geophysicslab.database.SignificantEarthquakes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class TSVReader {

    private static List<SignificantEarthquakes> earthquakesList;

    public static void setData(List<String[]> earthquakes) {
        earthquakesList = new ArrayList<>();
        SignificantEarthquakes significantEarthquakes = new SignificantEarthquakes();
        for (int index = 2; index < earthquakes.size(); index++) {
            String[] row = earthquakes.get(index);
            try {
                if (row != null) {
                    significantEarthquakes.setYear(Integer.parseInt(row[1]));
                    significantEarthquakes.setMonth(Integer.parseInt(row[2]));
                    significantEarthquakes.setDay(Integer.parseInt(row[3]));
                    significantEarthquakes.setHour(Integer.parseInt(row[4]));
                    significantEarthquakes.setMinute(Integer.parseInt(row[5]));
                    significantEarthquakes.setSecond(Integer.parseInt(row[6]));
                    significantEarthquakes.setTsunami(Integer.parseInt(row[7]));
                    significantEarthquakes.setVolcano(Integer.parseInt(row[8]));
                    significantEarthquakes.setLocation(row[9]);
                    significantEarthquakes.setLatitude(Double.parseDouble(row[10]));
                    significantEarthquakes.setLongitude(Double.parseDouble(row[11]));
                    significantEarthquakes.setLatitude(Integer.parseInt(row[12]));
                    significantEarthquakes.setMagnitude(Double.parseDouble(row[13]));
                    significantEarthquakes.setIntensity(Integer.parseInt(row[14]));
                    significantEarthquakes.setDeaths(Long.parseLong(row[15]));
                    significantEarthquakes.setDeathDescription(Integer.parseInt(row[16]));
                    significantEarthquakes.setMissing(Long.parseLong(row[17]));
                    significantEarthquakes.setMissingDescription(Integer.parseInt(row[18]));
                    significantEarthquakes.setInjuries(Long.parseLong(row[19]));
                    significantEarthquakes.setInjuriesDescription(Integer.parseInt(row[20]));
                    significantEarthquakes.setDamage_$Mil(Double.parseDouble(row[21]));
                    significantEarthquakes.setDamageDescription(Integer.parseInt(row[22]));
                    significantEarthquakes.setHouseDestroyed(Long.parseLong(row[23]));
                    significantEarthquakes.setHouseDestroyedDescription(Integer.parseInt(row[24]));
                    significantEarthquakes.setHousesDamaged(Long.parseLong(row[25]));
                    significantEarthquakes.setHousesDamagedDescription(Integer.parseInt(row[26]));
                    significantEarthquakes.setTotalDeaths(Long.parseLong(row[27]));
                    significantEarthquakes.setTotalDeathDescription(Integer.parseInt(row[28]));
                    significantEarthquakes.setTotalMissing(Long.parseLong(row[29]));
                    significantEarthquakes.setTotalMissingDescription(Integer.parseInt(row[30]));
                    significantEarthquakes.setTotalInjuries(Long.parseLong(row[31]));
                    significantEarthquakes.setTotalInjuriesDescription(Integer.parseInt(row[32]));
                    significantEarthquakes.setTotalDamage_$Mil(Double.parseDouble(row[33]));
                    significantEarthquakes.setTotalDamageDescription(Integer.parseInt(row[34]));
                    significantEarthquakes.setTotalHouseDestroyed(Long.parseLong(row[35]));
                    significantEarthquakes.setTotalHouseDestroyedDescription(Integer.parseInt(row[36]));
                    significantEarthquakes.setTotalHousesDamaged(Long.parseLong(row[37]));
                    significantEarthquakes.setTotalHousesDamagedDescription(Integer.parseInt(row[38]));

                    earthquakesList.add(significantEarthquakes);
                } else {
                    Log.d("field: ", "field is null");
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<SignificantEarthquakes> getEarthquakesList() {
        return earthquakesList;
    }

    public static List<String[]> readTSV(Context context) {
        List<String[]> data = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(context.getAssets().open("earthquakes.tsv")));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] rows = line.split("\t");
                StringTokenizer stringTokenizer = new StringTokenizer(line, "\t", true);
                while (stringTokenizer.hasMoreTokens()) {
                    System.out.print(stringTokenizer.nextToken());
                }
                data.add(rows);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return data;
    }
}
