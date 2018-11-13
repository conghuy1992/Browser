package com.conghuy.MyBrowser.common;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    // shared pref mode
    private int PRIVATE_MODE = 0;
    private String PREF_NAME = "Browser";
    private String homePage="homePage";

    public PrefManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void ClearData() {
        editor.clear();
        editor.commit();
    }
    public void setHomePage(String s) {
        editor.putString(homePage, s);
        editor.commit();
    }

    public String getHomePage() {
        return pref.getString(homePage, Statics.PAGE_DEFAULT);
    }
}
