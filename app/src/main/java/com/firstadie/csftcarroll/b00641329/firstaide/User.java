package com.firstadie.csftcarroll.b00641329.firstaide;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tigh on 03/11/17.
 */

public class User {
    private int mId;
    private String mFirstName;
    private String mSurname;
    private String mEmail;

    public User(JSONObject json) throws JSONException {
        this(json.getInt("id"),
                json.getString("first_name"),
                json.getString("surname"),
                json.getString("email"));
    }
    public User(int id, String firstName, String surname, String email) {
        mId = id;
        mFirstName = firstName;
        mSurname = surname;
        mEmail = email;
    }

    public int getId() {
        return mId;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getSurname() {
        return mSurname;
    }

    public String getFullName() {
        return mFirstName + " " + mSurname;
    }

    public String getEmail() {
        return mEmail;
    }

    public String toJSONString() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("first_name", mFirstName);
            jsonObject.put("surname", mSurname);
            jsonObject.put("id", mId);
            jsonObject.put("email", mEmail);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }
}