package com.firstadie.csftcarroll.b00641329.firstaide.api;

import com.firstadie.csftcarroll.b00641329.firstaide.R;

/**
 * Created by tigh on 12/11/17.
 */

public class Weather {

    private String mDescription;
    private String mWeatherCode;

    private double mTemperature;

    public Weather(String description, String weatherCode, double temperature) {
        mDescription = description;
        mWeatherCode = weatherCode;
        mTemperature = temperature;
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

    public int getWeatherIcon() {
        switch (mWeatherCode) {
            case "01d":
                return R.drawable.weather_clear_day;
            case "01n":
                return R.drawable.weather_clear_night;
            case "02d":
                return R.drawable.weather_light_clouds_day;
            case "02n":
                return R.drawable.weather_light_clouds_night;
            case "03d":
                return R.drawable.weather_clouds_day;
            case "03n":
                return R.drawable.weather_clouds_night;
            case "04d":
                return R.drawable.weather_light_clouds_day;
            case "04n":
                return R.drawable.weather_light_clouds_night;
            case "09d":
                return R.drawable.weather_rain_day;
            case "09n":
                return R.drawable.weather_rain_night;
            case "10d":
                return R.drawable.weather_light_rain_day;
            case "10n":
                return R.drawable.weather_light_rain_night;
            case "11d":
                return R.drawable.weather_storm_day;
            case "11n":
                return R.drawable.weather_storm_night;
            case "13d":
                return R.drawable.weather_snow_day;
            case "13n":
                return R.drawable.weather_snow_night;
            case "50d":
                return R.drawable.weather_fog;
            case "50n":
                return R.drawable.weather_fog;
            default:
                return R.drawable.weather_none_available;

        }
    }
}
