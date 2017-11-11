package com.firstadie.csftcarroll.b00641329.firstaide.api;

/**
 * Created by tigh on 10/11/17.
 */

public class WeatherAPI extends API {

    public WeatherAPI() {
        super();

        mHashSet.add("appid");
        mHashSet.add("lat");
        mHashSet.add("lon");

        addParam("appid", getAPIKey());
    }

    @Override
    public String getBaseUrl() {
        return "api.openweathermap.org/data/2.5/weather";
    }

    @Override
    public String getAPIKey() {
        return "911240111a264d736f05e65f1b836f2a";
    }

}
