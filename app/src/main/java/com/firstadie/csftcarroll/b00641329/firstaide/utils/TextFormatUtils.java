package com.firstadie.csftcarroll.b00641329.firstaide.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tigh on 12/11/17.
 */

public class TextFormatUtils {

    public static String formatTemperature(double temperature) {
        DecimalFormat df = new DecimalFormat("#.#");

        String temp = df.format(temperature);
        return temp.concat("\u00B0");
    }

    public static String toTitleCase(String... words) {
        StringBuilder builder = new StringBuilder();
        boolean cap = true;
        for(String word : words) {
            for(char letter : word.toCharArray()) {
                letter = cap ? Character.toUpperCase(letter) : Character.toLowerCase(letter);
                builder.append(letter);
                cap = false;
            }
            builder.append(' ');
            cap = true;
        }

        return builder.toString().trim();
    }

    public static String epochToTime(long epoch) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mma");
        Date date = new Date(epoch);

        return simpleDateFormat.format(date);
    }

    public static String kilometersToMiles(int km) {
        DecimalFormat df = new DecimalFormat("0.#");

        double distance = 0.000621 * km;
        return df.format(distance) + " miles";
    }

    public static String secondsToTime(int s) {
        return secondsToTime(s, true);
    }

    public static String secondsToTime(int s, boolean longform) {
        final int hour = 60 * 60;
        if(s >= hour) {
            int hours = (s / hour);
            int minutes = (s % 60);
            String hoursText = hours + (hours != 1 ? " hrs" : " hr");
            String minutesText = ", " + minutes + (minutes != 1 ? " mins" : " min");
            if(longform) {
                hoursText = hours + (hours != 1 ? " hours" : " hour");
                minutesText = ", " + minutes + (minutes != 1 ? " minutes" : " minute");
            }

            if(minutes != 0) {
                return hoursText + minutesText;
            } else {
                return hoursText;
            }
        } else {
            double minutes = s / 60.0;
            DecimalFormat df = new DecimalFormat("0");
            String ending = "";
            if(minutes != 1) {
                ending = "s";
            }
            if(longform) {
                return df.format(minutes) + " minute" + ending;
            } else {
                return df.format(minutes) + " min" + ending;
            }
        }
    }

    public static String databaseUrlFor(String endpoint) {
        return "https://uniprojects.000webhostapp.com/" + endpoint;
    }
}
