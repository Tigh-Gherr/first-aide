package com.firstadie.csftcarroll.b00641329.firstaide;

import android.os.AsyncTask;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by tigh on 03/11/17.
 */

public class PostAsyncTask extends AsyncTask<String, Void, String> {

    private OnEndpointQueryCompleteListener mListener;

    public void setEndpointQueryCompleteListener(OnEndpointQueryCompleteListener listener) {
        mListener = listener;
    }

    @Override
    protected String doInBackground(String... strings) {
        String jsonResult;
        HttpsURLConnection connection = null;
        try {
            URL url = new URL(strings[0]);
            String data = strings[1];

            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.connect();

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            writer.write(data);
            writer.close();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream inputStream = connection.getInputStream();

            int bytesRead = 0;
            byte[] buffer = new byte[1024];

            while ((bytesRead = inputStream.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();

            jsonResult = new String(out.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return jsonResult;
    }

    @Override
    protected void onPostExecute(String s) {
        if (mListener != null) {
            try {
                mListener.onQueryComplete(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
