package com.firstadie.csftcarroll.b00641329.firstaide.api;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tigh on 08/12/17.
 */

public class GooglePlacesAPI extends API<GooglePlace> {

    public static final String PARAM_KEY = "key";
    public static final String PARAM_ADDRESS = "address";

    public GooglePlacesAPI() {
        super();

        mAllowedParams.add(PARAM_KEY);
        mAllowedParams.add(PARAM_ADDRESS);

        addParam(PARAM_KEY, getAPIKey());
    }

    @Override
    public String getBaseUrl() {
        return "https://maps.google.co.uk/maps/api/geocode/json";
    }

    @Override
    public String getAPIKey() {
        return "AIzaSyDuBMdzkh1GPGZflagG5J2oq89lwMt--JY";
    }

    @Override
    public GooglePlace parse(String results) throws JSONException {
        JSONObject json = new JSONObject(results);
        JSONObject resultJSON = json.getJSONArray("results").getJSONObject(0);
        JSONObject locationJSON = resultJSON.getJSONObject("geometry").getJSONObject("location");

        double lat = locationJSON.getDouble("lat");
        double lng = locationJSON.getDouble("lng");
        String address = resultJSON.getString("formatted_address");

        return new GooglePlace(address, lat, lng);
    }
}
