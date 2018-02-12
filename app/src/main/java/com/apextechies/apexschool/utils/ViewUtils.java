package com.apextechies.apexschool.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * Created by Asi on 19/01/2016.
 */
public class ViewUtils {
    private static String TAG = ViewUtils.class.getSimpleName();

    public static void setTextType(int textStyle, TextView... textViews) {
        for (TextView tv : textViews) {
            tv.setTypeface(tv.getTypeface(), textStyle);
        }
    }

    public static void setTextSize(int textSize, TextView... textViews) {
        for (TextView tv : textViews) {
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        }
    }

    /**
     * Check device have internet connection or not
     *
     * @param activity
     * @return
     */
    public static boolean isOnline(Activity activity) {
        {
            boolean haveConnectedWifi = false;
            boolean haveConnectedMobile = false;

            ConnectivityManager cm = null;
            if (activity != null)
                cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                NetworkInfo[] netInfo = cm.getAllNetworkInfo();
                for (NetworkInfo ni : netInfo) {
                    if (ni.getTypeName().equalsIgnoreCase("WIFI")) {
                        if (ni.isConnected()) {
                            haveConnectedWifi = true;
                            Log.i("", "WIFI CONNECTION : AVAILABLE");
                        } else {
                            Log.i(TAG, "WIFI CONNECTION : NOT AVAILABLE");
                        }
                    }
                    if (ni.getTypeName().equalsIgnoreCase("MOBILE")) {
                        if (ni.isConnected()) {
                            haveConnectedMobile = true;
                            Log.i(TAG, "MOBILE INTERNET CONNECTION : AVAILABLE");
                        } else {
                            Log.i(TAG, "MOBILE INTERNET CONNECTION : NOT AVAILABLE");
                        }
                    }
                }
            }
            return haveConnectedWifi || haveConnectedMobile;
        }

    }

    public static void showNoInternetConnectionDialog(final Activity mActivity) {
        try {
            AlertDialog.Builder builder =new AlertDialog.Builder(mActivity);
            builder.setTitle("No Internet Connection");
            builder.setMessage("No Internet connection. Please check your network settings.");
            builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } catch (Exception e) {
            Log.d(TAG, "Show Dialog: " + e.getMessage());
        }
    }
    public static void showMessageOnDialog(final Activity mActivity, final String title, final String message) {
        try {
            AlertDialog.Builder builder =new AlertDialog.Builder(mActivity);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } catch (Exception e) {
            Log.d(TAG, "Show Dialog: " + e.getMessage());
        }
    }
}
