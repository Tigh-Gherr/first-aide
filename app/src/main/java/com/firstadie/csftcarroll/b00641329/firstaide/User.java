package com.firstadie.csftcarroll.b00641329.firstaide;

import com.firstadie.csftcarroll.b00641329.firstaide.events.UserHobby;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tigh on 03/11/17.
 */

public class User {
    private int mId;
    private String mFirstName;
    private String mSurname;
    private String mEmail;
    private List<UserHobby> mUserHobbies;

    public static User buildFromJSON(JSONObject json) throws JSONException {
        int id = json.getInt("id");
        String firstName = json.getString("first_name");
        String surname = json.getString("surname");
        String email = json.getString("email");

        List<UserHobby> activities = new ArrayList<>();
        JSONArray activitesArray = json.getJSONArray("activities");
        for(int i = 0; i < activitesArray.length(); i++) {
            JSONObject jsonActivity = (JSONObject) activitesArray.get(i);
            String title = jsonActivity.getString("title");
            int duration = jsonActivity.getInt("duration");
            int priority = jsonActivity.getInt("priority");
            int activityId = jsonActivity.getInt("id");
            activities.add(new UserHobby(title, duration, priority, activityId));
        }

        return new User(id, firstName, surname, email, activities);
    }

    public User(int id, String firstName, String surname, String email, List<UserHobby> userHobbies) {
        mId = id;
        mFirstName = firstName;
        mSurname = surname;
        mEmail = email;
        mUserHobbies = userHobbies;
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

    public List<UserHobby> getUserHobbies() {
        return mUserHobbies;
    }

    public String toJSONString() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("first_name", mFirstName);
            jsonObject.put("surname", mSurname);
            jsonObject.put("id", mId);
            jsonObject.put("email", mEmail);

            JSONArray jsonActivitiesArray = new JSONArray();
            for(UserHobby activity : mUserHobbies) {
                JSONObject jsonActivity = new JSONObject();
                jsonActivity.put("title", activity.getTitle());
                jsonActivity.put("duration", activity.getDuration());
                jsonActivity.put("priority", activity.getPriority());
                jsonActivity.put("id", activity.getId());

                jsonActivitiesArray.put(jsonActivity);
            }

            jsonObject.put("activities", jsonActivitiesArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }
}