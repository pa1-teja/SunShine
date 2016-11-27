package com.example.pavan.sunshine.SunshineWearable;


import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

/**
 * Created by KVR on 11/27/2016.
 */

public class MobileToWearSender implements DataApi.DataListener,GoogleApiClient.ConnectionCallbacks,
GoogleApiClient.OnConnectionFailedListener{

    private String HIGH_TEMP_KEY = "com.example.pavan.sunshine.wear.highTemp";
    private String LOW_TEMP_KEY = "com.example.pavan.sunshine.wear.lowTemp";

    private GoogleApiClient mGoogleApiClient;
    private int count = 0;


    // TODO: Set a Weather Image,High- Low Temperatures in the created DataMap to send it to the wearable.
    private void sendWeatherImageAndHighLowTempToWearable(String highTemp,String lowTemp){

        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create("/temps");
        putDataMapRequest.getDataMap().putString(HIGH_TEMP_KEY,highTemp);
        putDataMapRequest.getDataMap().putString(LOW_TEMP_KEY,lowTemp);

        PutDataRequest putDataRequest = putDataMapRequest.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(mGoogleApiClient, putDataRequest);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {

    }
}
