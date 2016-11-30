package com.example.pavan.sunshine.SunshineWearable;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

/**
 * Created by KVR on 11/29/2016.
 */

public class WatchConfigurationPreferences {

    private static final String NAME = "WatchConfigurationPreferences";
    private static final String KEY_BACKGROUND_COLOR = NAME + ".KEY_BACKGROUND_COLOR";
    private static final String KEY_DATE_TIME_COLOR = NAME + ".KEY_DATE_TIME_COLOR";

    private static final int DEFAULT_BACKGROUND_COLOR = Color.parseColor("black");
    private static final int DEFAULT_DATE_TIME_COLOR = Color.parseColor("white");

    private final SharedPreferences preferences;

    WatchConfigurationPreferences(SharedPreferences sharedPreferences) {
        this.preferences = sharedPreferences;
    }

    public static WatchConfigurationPreferences newInstance(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return new WatchConfigurationPreferences(preferences);
    }

    public int getBackgroundColor() {
        return preferences.getInt(KEY_BACKGROUND_COLOR, DEFAULT_BACKGROUND_COLOR);
    }

    public void setBackgroundColor(int color) {
        preferences.edit().putInt(KEY_BACKGROUND_COLOR, color).apply();
    }

    public int getDateAndTimeColor() {
        return preferences.getInt(KEY_DATE_TIME_COLOR, DEFAULT_DATE_TIME_COLOR);
    }

    public void setDateAndTimeColor(int color) {
        preferences.edit().putInt(KEY_DATE_TIME_COLOR, color).apply();
    }
}
