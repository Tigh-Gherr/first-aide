package com.firstadie.csftcarroll.b00641329.firstaide.api;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.firstadie.csftcarroll.b00641329.firstaide.OnEndpointQueryCompleteListener;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by tigh on 10/11/17.
 */

public abstract class API<T> {

    private Map<String, String> mParameters;
    protected HashSet<String> mAllowedParams;

    private OnEndpointQueryCompleteListener mOnEndpointQueryCompleteListener;

    public API() {
        mParameters = new HashMap<>();
        mAllowedParams = new HashSet<>();
    }

    public void setOnEndpointQueryCompleteListener(OnEndpointQueryCompleteListener listener) {
        mOnEndpointQueryCompleteListener = listener;
    }

    public abstract String getBaseUrl();

    public abstract String getAPIKey();

    public void query() {
        String url = buildEndpoint();

        Log.d(getClass().getSimpleName(), url);

        APIAsyncTask asyncTask = new APIAsyncTask();
        asyncTask.execute(url);
    }

    public void addParam(String key, String value) {
        if(mAllowedParams.contains(key)) {
            mParameters.put(key, value);
        }
    }

    private String buildEndpoint() {
        Uri.Builder builder = Uri.parse(getBaseUrl()).buildUpon();

        for(Map.Entry<String, String> param : mParameters.entrySet()) {
            String key = param.getKey();
            String value = param.getValue();

            builder.appendQueryParameter(key, value);
        }

        return builder.build().toString();
    }

    public abstract T parse(String results) throws JSONException;

    private class APIAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpsURLConnection connection = null;

            try {
                URL url = new URL(params[0]);

                connection = (HttpsURLConnection) url.openConnection();

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                InputStream inputStream = connection.getInputStream();

                connection.connect();

                if(connection.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                    Log.d(API.this.getClass().getSimpleName(), "NOT OK: " + connection.getResponseCode());
                    return null;
                }

                int bytesRead = 0;
                byte[] buffer = new byte[1024];
                while ((bytesRead = inputStream.read(buffer)) > 0) {
                    out.write(buffer, 0, bytesRead);
                }

                out.close();

                return new String(out.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(connection != null) {
                    connection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if(mOnEndpointQueryCompleteListener == null) {
                return;
            }

            try {
                mOnEndpointQueryCompleteListener.onQueryComplete(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
