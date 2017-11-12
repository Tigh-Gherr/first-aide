package com.firstadie.csftcarroll.b00641329.firstaide.api;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tigh on 10/11/17.
 */

public class WeatherAPI extends API {

    public WeatherAPI() {
        super();

        mAllowedParams.add("appid");
        mAllowedParams.add("lat");
        mAllowedParams.add("lon");
        mAllowedParams.add("units");

        addParam("appid", getAPIKey());
        addParam("units", "metric");
    }

    @Override
    public String getBaseUrl() {
        return "https://api.openweathermap.org/data/2.5/weather";
    }

    @Override
    public String getAPIKey() {
        return "911240111a264d736f05e65f1b836f2a";
    }

    @Override
    public Weather parse(String results) throws JSONException {
        JSONObject resultJson = new JSONObject(results);

        JSONObject weatherJson = resultJson.getJSONArray("weather").getJSONObject(0);
        String description = weatherJson.getString("main");
        String iconCode = weatherJson.getString("icon");

        JSONObject tempJson = resultJson.getJSONObject("main");
        double temp = tempJson.getDouble("temp");
        double minTemp = tempJson.getDouble("temp_min");
        double maxTemp = tempJson.getDouble("temp_max");

        return new Weather(description, iconCode, temp, minTemp, maxTemp);
    }

}
