package com.example.bbusra05d1app.functions;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class Sessions {
    //data member
    private SharedPreferences sp;

    //constructor
    public Sessions(Context context) {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    //setter Methods
    public void SetUserID(int userID) {
        sp.edit().putInt("USER_ID", userID).commit();
    }

    public void SetUserName(String userName) {
        sp.edit().putString("USER_NAME", userName).commit();
    }

    public void SetUserPassword(String userPassword) {
        sp.edit().putString("USER_PASSWORD", userPassword).commit();
    }

    public void SetUserFullName(String fullName) {
        sp.edit().putString("FULL_NAME", fullName).commit();
    }

    public void SetUserType(String userType) {
        sp.edit().putString("USER_TYPE", userType).commit();
    }

    public void SetUserEmail(String email) {
        sp.edit().putString("USER_EMAIL", email).commit();
    }

    public void SetUserImage(String image) {
        sp.edit().putString("USER_IMAGE", image).commit();
    }

    //getter methods
    public int getUserID() {
        return sp.getInt("USER_ID", 0);
    }

    public String getUserName() {
        return sp.getString("USER_NAME", "");
    }

    public String getUserType() {
        return sp.getString("USER_TYPE", "");
    }

    public String getUserEmail() {
        return sp.getString("USER_EMAIL", "");
    }

    public String getUserImage() {
        return sp.getString("USER_IMAGE", "");
    }

    public String getUserPassword() {
        return sp.getString("USER_PASSWORD", "");
    }

    public void ClearSessions() {
        sp.edit().clear().commit();
    }
}
