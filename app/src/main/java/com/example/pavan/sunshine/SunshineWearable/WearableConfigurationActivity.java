package com.example.pavan.sunshine.SunshineWearable;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.pavan.sunshine.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

public class WearableConfigurationActivity extends AppCompatActivity implements
        ColorChooserDialog.Listener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "SimpleWatchface";
    private static final String TAG_BACKGROUND_COLOR_CHOOSER = "background_chooser";
    private static final String TAG_DATE_AND_TIME_COLOR_CHOOSER = "date_time_chooser";

    private GoogleApiClient googleApiClient;
    private View backgroundColorImagePreview, dateAndTimeColorImagePreview;
    private WatchConfigurationPreferences watchConfigurationPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wearable_configuration);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();

        findViewById(R.id.configuration_background_colour).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorChooserDialog.newInstance(getString(R.string.pick_background_color))
                        .show(getFragmentManager(), TAG_BACKGROUND_COLOR_CHOOSER);
            }
        });

        findViewById(R.id.configuration_time_colour).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorChooserDialog.newInstance(getString(R.string.pick_date_time_colour))
                        .show(getFragmentManager(), TAG_DATE_AND_TIME_COLOR_CHOOSER);
            }
        });

        backgroundColorImagePreview = findViewById(R.id.configuration_background_colour_preview);
        dateAndTimeColorImagePreview = findViewById(R.id.configuration_date_and_time_colour_preview);

        watchConfigurationPreferences = WatchConfigurationPreferences.newInstance(this);

        backgroundColorImagePreview.setBackgroundColor(watchConfigurationPreferences.getBackgroundColor());
        dateAndTimeColorImagePreview.setBackgroundColor(watchConfigurationPreferences.getDateAndTimeColor());
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return true;
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed");
    }

    @Override
    protected void onStop() {

        if (googleApiClient != null && googleApiClient.isConnected())
            googleApiClient.disconnect();

        super.onStop();
    }

    @Override
    public void onColorSelected(String color, String tag) {

        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(WatchFaceSyncCommons.PATH);

        if (TAG_BACKGROUND_COLOR_CHOOSER.equals(tag)) {
            backgroundColorImagePreview.setBackgroundColor(Color.parseColor(color));
            watchConfigurationPreferences.setBackgroundColor(Color.parseColor(color));
            putDataMapRequest.getDataMap().putString(WatchFaceSyncCommons.KEY_BACKGROUND_COLOUR, color);
        } else {

            dateAndTimeColorImagePreview.setBackgroundColor(Color.parseColor(color));
            watchConfigurationPreferences.setDateAndTimeColor(Color.parseColor(color));
            putDataMapRequest.getDataMap().putString(WatchFaceSyncCommons.KEY_DATE_TIME_COLOUR, color);
        }

        PutDataRequest dataRequest = putDataMapRequest.asPutDataRequest();
        Wearable.DataApi.putDataItem(googleApiClient, dataRequest);
    }
}
