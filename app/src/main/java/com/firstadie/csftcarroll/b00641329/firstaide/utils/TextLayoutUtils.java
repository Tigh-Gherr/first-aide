package com.firstadie.csftcarroll.b00641329.firstaide.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tigh on 12/11/17.
 */

public class TextLayoutUtils {

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
        if(s >= 60 * 60) {
            return "";
        } else {
            return (s % 60) + " minutes";
        }
    }
}
