package com.firstadie.csftcarroll.b00641329.firstaide.utils;

import java.text.DecimalFormat;

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
}
