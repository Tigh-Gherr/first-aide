package com.firstadie.csftcarroll.b00641329.firstaide.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by tigh on 03/11/17.
 */

public class EncryptionUtils {

    public static String sha256(String input) {
        String output = "";

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(input.getBytes());

            byte[] digest = md.digest();
            output = String.format("%64x", new BigInteger(1, digest));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return output;
    }

    public static String hashAndSalt(String password, String email) {
        String[] email_parts = email.split("@");
        String salt = email_parts[0].substring(0, 2) + email_parts[1].substring(0, 2) + "!?%djwfjc";
        return sha256(password + salt);
    }
}
