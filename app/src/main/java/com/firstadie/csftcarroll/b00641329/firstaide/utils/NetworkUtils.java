package com.firstadie.csftcarroll.b00641329.firstaide.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by tigh on 04/11/17.
 */

public class NetworkUtils {

    public static boolean isNetworkAvailable(Context c) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null;
    }
}
