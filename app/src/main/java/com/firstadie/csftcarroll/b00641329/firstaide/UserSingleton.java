package com.firstadie.csftcarroll.b00641329.firstaide;

import android.content.Context;

/**
 * Created by tigh on 03/11/17.
 */

public class UserSingleton {
    private static UserSingleton sUserSingleton;
    private User mUser;

    private UserSingleton() {
    }

    public static UserSingleton get() {
        if(sUserSingleton == null) {
            sUserSingleton = new UserSingleton();
        }
        return sUserSingleton;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public User getUser() {
        return mUser;
    }
}
