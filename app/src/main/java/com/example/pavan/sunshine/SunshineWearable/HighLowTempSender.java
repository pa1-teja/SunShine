package com.example.pavan.sunshine.SunshineWearable;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.io.ByteArrayOutputStream;

/**
 * Created by KVR on 11/30/2016.
 */

public class HighLowTempSender implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG_SUNSHINE_HIGH_LOW_TEMP = "sunshine_high_low_temp";
    private static final String TAG_SUNSHINE_WEATHER_IMAGE = "sunshine_weather_image";
    private final String TAG = getClass().getSimpleName();
    private GoogleApiClient googleApiClient;

    public void pickHighLowTemp(double highTemp, double lowTemp, Context context, Bitmap bitmap) {

        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wearable.API)
                .build();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

        PutDataMapRequest mapRequest = PutDataMapRequest.create(WatchFaceSyncCommons.HIGH_LOW_TEMP_PATH);

        Log.e(TAG_SUNSHINE_HIGH_LOW_TEMP, String.valueOf(highTemp) + " " + String.valueOf(lowTemp));

        mapRequest.getDataMap().putDouble(WatchFaceSyncCommons.HIGH_TEMP_KEY, highTemp);
        mapRequest.getDataMap().putDouble(WatchFaceSyncCommons.LOW_TEMP_KEY, lowTemp);

        mapRequest.getDataMap().putAsset(WatchFaceSyncCommons.WEATHER_IMAGE_KEY,
                Asset.createFromBytes(byteArrayOutputStream.toByteArray()));


        PutDataRequest putDataRequest = mapRequest.asPutDataRequest();
        Wearable.DataApi.putDataItem(googleApiClient, putDataRequest);

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
}
