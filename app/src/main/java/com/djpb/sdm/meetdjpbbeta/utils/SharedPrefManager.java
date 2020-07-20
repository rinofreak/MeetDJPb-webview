package com.djpb.sdm.meetdjpbbeta.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPrefManager {

    static final String USER_NAME_KEY = "username", ROOM_NAME_KEY = "roomname";

    private static SharedPreferences getSharedPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setUserName(Context context, String username) {
        SharedPreferences.Editor spEdit = getSharedPreference(context).edit();
        spEdit.putString(USER_NAME_KEY, username);
        spEdit.apply();
    }

    public static String getUserName(Context context) {
        return getSharedPreference(context).getString(USER_NAME_KEY,"");
    }

    public static void setRoomName(Context context, String roomname) {
        SharedPreferences.Editor spEdit = getSharedPreference(context).edit();
        spEdit.putString(ROOM_NAME_KEY, roomname);
        spEdit.apply();
    }

    public static String getRoomName(Context context) {
        return getSharedPreference(context).getString(ROOM_NAME_KEY,"");
    }

}
