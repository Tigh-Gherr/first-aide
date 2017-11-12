package com.firstadie.csftcarroll.b00641329.firstaide.api;

import com.firstadie.csftcarroll.b00641329.firstaide.R;

/**
 * Created by tigh on 12/11/17.
 */

public class Weather {

    private String mDescription;
    private String mWeatherCode;

    private double mTemperature;
    private double mMinimumTemperature;
    private double mMaximumTemperature;

    public Weather(String description, String weatherCode, double temperature, double minimumTemperature, double maximumTemperature) {
        mDescription = description;
        mWeatherCode = weatherCode;
        mTemperature = temperature;
        mMinimumTemperature = minimumTemperature;
        mMaximumTemperature = maximumTemperature;
    }

    public double getTemperature() {
        return mTemperature;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getWeatherCode() {
        return mWeatherCode;
    }

    public double getMinimumTemperature() {
        return mMinimumTemperature;
    }

    public double getMaximumTemperature() {
        return mMaximumTemperature;
    }

    public int getWeatherIcon() {
        switch (mWeatherCode) {
            case "01d":
                return R.drawable.art_clear;
            case "01n":
                return R.drawable.art_clear;
            case "02d":
                return R.drawable.art_light_clouds;
            case "02n":
                return R.drawable.art_light_clouds;
            case "03d":
                return R.drawable.art_clouds;
            case "03n":
                return R.drawable.art_clouds;
            case "04d":
                return R.drawable.art_light_clouds;
            case "04n":
                return R.drawable.art_light_clouds;
            case "09d":
                return R.drawable.art_rain;
            case "09n":
                return R.drawable.art_rain;
            case "10d":
                return R.drawable.art_light_rain;
            case "10n":
                return R.drawable.art_light_rain;
            case "11d":
                return R.drawable.art_storm;
            case "11n":
                return R.drawable.art_storm;
            case "13d":
                return R.drawable.art_snow;
            case "13n":
                return R.drawable.art_snow;
            case "50d":
                return R.drawable.art_fog;
            case "50n":
                return R.drawable.art_fog;
            default: return R.drawable.art_clear;

        }
    }
}
