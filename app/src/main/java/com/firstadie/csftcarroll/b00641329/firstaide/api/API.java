package com.firstadie.csftcarroll.b00641329.firstaide.api;

import android.net.Uri;
import android.util.Log;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by tigh on 10/11/17.
 */

public abstract class API {

    private Map<String, String> mParameters;
    protected HashSet<String> mHashSet;


    public API() {
        mParameters = new HashMap<>();
        mHashSet = new HashSet<>();
    }

    public abstract String getBaseUrl();

    public abstract String getAPIKey();

    public void query() {
        Uri.Builder builder = Uri.parse(getBaseUrl()).buildUpon();

        for(Map.Entry<String, String> param : mParameters.entrySet()) {
            String key = param.getKey();
            String value = param.getValue();

            builder.appendQueryParameter(key, value);
        }

        String url = builder.build().toString();
        Log.d(getClass().getSimpleName(), url);
    }

    public void addParam(String key, String value) {
        if(mHashSet.contains(key)) {
            mParameters.put(key, value);
        }
    }

}
