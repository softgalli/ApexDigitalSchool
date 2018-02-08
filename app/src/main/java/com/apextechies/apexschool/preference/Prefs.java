package com.apextechies.apexschool.preference;

import android.content.SharedPreferences;

import com.apextechies.apexschool.AppController;

/**
 * Created by Welcome on 1/12/2018.
 */

public class Prefs {
    public static void setPreLoad(boolean isLogin) {
        SharedPreferences pref = AppController.getInstance().getSharedPreferences("PreLoad1", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("PreLoad", isLogin);
        editor.commit();
    }

    public static boolean isPreLoaded() {
        SharedPreferences pref = AppController.getInstance().getSharedPreferences("PreLoad1", 0);
        return pref.getBoolean("PreLoad", false);
    }
}
