package com.example.photolens.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

public class SharedPrefsHelper {
    private static final String PREF_NAME = "photolens_prefs";
    private static final String KEY_SELECTED_BRAND = "selected_brand";

    public static void saveBrand(Context context, String brand) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_SELECTED_BRAND, brand).apply();
    }

    public static String getSelectedBrand(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_SELECTED_BRAND, "canon");
    }

    public static int getBrandColor(Context context) {
        String brand = getSelectedBrand(context);
        switch (brand) {
            case "sony":
                return Color.parseColor("#FF6B00"); // Orange
            case "nikon":
                return Color.parseColor("#FFC600"); // Yellow
            default:
                return Color.parseColor("#E31837"); // Red (Canon)
        }
    }
}