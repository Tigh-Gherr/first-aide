package com.firstadie.csftcarroll.b00641329.firstaide.utils;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

/**
 * Created by tigh on 04/11/17.
 */

public class LoginUtils {
    public static boolean isValidEmail(String email) {
        boolean ise = TextUtils.isEmpty(email);
        boolean mat = Patterns.EMAIL_ADDRESS.matcher(email).matches();

        Log.i("LoginUtilsMate", "ise: " + ise + "      mat: " + mat);
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return true;
    }
}
