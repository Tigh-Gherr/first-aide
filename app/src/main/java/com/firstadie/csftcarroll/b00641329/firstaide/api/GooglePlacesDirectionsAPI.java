package com.firstadie.csftcarroll.b00641329.firstaide.api;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tigh on 10/11/17.
 */

public class GooglePlacesDirectionsAPI extends API<GooglePlace> {

    public static final String PARAM_KEY = "key";
    public static final String PARAM_ORIGIN = "origin";
    public static final String PARAM_DESTINATION = "destination";
    public static final String PARAM_UNITS = "units";

    public GooglePlacesDirectionsAPI() {
        super();

        mAllowedParams.add(PARAM_KEY);
        mAllowedParams.add(PARAM_ORIGIN);
        mAllowedParams.add(PARAM_DESTINATION);
        mAllowedParams.add(PARAM_UNITS);

        addParam(PARAM_KEY, getAPIKey());
        addParam(PARAM_UNITS, "imperial");
    }

    @Override
    public String getBaseUrl() {
        return "https://maps.google.co.uk/maps/api/directions/json";
    }

    @Override
    public String getAPIKey() {
        return "AIzaSyDuBMdzkh1GPGZflagG5J2oq89lwMt--JY";
    }

    @Override
    public GooglePlace parse(String results) throws JSONException {
        JSONObject resultJSON = new JSONObject(results);
        JSONObject routeJSON = resultJSON.getJSONArray("routes").getJSONObject(0);
        JSONObject legJSON = routeJSON.getJSONArray("legs").getJSONObject(0);
        JSONObject endAddressJSON = legJSON.getJSONObject("end_location");

        int distance = legJSON.getJSONObject("distance").getInt("value");
        int duration = legJSON.getJSONObject("duration").getInt("value");
        String startAddress = legJSON.getString("start_address");
        String endAddress = legJSON.getString("end_address");

        double lat = endAddressJSON.getDouble("lat");
        double lng = endAddressJSON.getDouble("lng");

        return new GooglePlace(distance, duration, startAddress, endAddress, lat, lng);
    }
}
