package com.firstadie.csftcarroll.b00641329.firstaide.api;

/**
 * Created by tigh on 10/11/17.
 */

public class GooglePlacesAPI extends API {

    public GooglePlacesAPI() {
        super();

        mHashSet.add("key");
        mHashSet.add("origin");
        mHashSet.add("destination");
        mHashSet.add("metric");

        addParam("key", getAPIKey());
        addParam("metric", "imperial");
    }

    @Override
    public String getBaseUrl() {
        return "https://maps.google.co.uk/maps/api/directions/json";
    }

    @Override
    public String getAPIKey() {
        return "AIzaSyDuBMdzkh1GPGZflagG5J2oq89lwMt--JY";
    }
}
