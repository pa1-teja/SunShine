package com.example.pavan.sunshine.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.pavan.sunshine.sync.SunshineSyncAdapter;

/**
 * Created by pavan on 7/17/2016.
 */

/**
 * Provider for a horizontally expandable widget showing today's weather.
 * <p/>
 * Delegates widget updating to {@link TodayWidgetIntentService} to ensure that
 * data retrieval is done on a background thread
 */

public class TodayWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        context.startService(new Intent(context, TodayWidgetIntentService.class));
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        context.startService(new Intent(context, TodayWidgetIntentService.class));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (SunshineSyncAdapter.ACTION_DATA_UPDATED.equals(intent.getAction()))
            context.startService(new Intent(context, TodayWidgetIntentService.class));
    }
}
