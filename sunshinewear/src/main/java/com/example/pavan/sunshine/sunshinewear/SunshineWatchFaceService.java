/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.pavan.sunshine.sunshinewear;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.util.Log;
import android.view.SurfaceHolder;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataItemBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

import java.util.concurrent.TimeUnit;


/**
 * Digital watch face with seconds. In ambient mode, the seconds aren't displayed. On devices with
 * low-bit ambient mode, the text is drawn without anti-aliasing in ambient mode.
 */
public class SunshineWatchFaceService extends CanvasWatchFaceService {
    private static final Typeface NORMAL_TYPEFACE =
            Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
    private static final long TICK_PERIOD_MILLIS = TimeUnit.SECONDS.toMillis(1);

    /**
     * Update rate in milliseconds for interactive mode. We update once a second since seconds are
     * displayed in interactive mode.
     */
    private static final long INTERACTIVE_UPDATE_RATE_MS = TimeUnit.SECONDS.toMillis(1);
    /**
     * Handler message id for updating the time periodically in interactive mode.
     */
    private static final int MSG_UPDATE_TIME = 0;
    private Bitmap bitmap;
    private int imageResourceId;
    private boolean processImage = false;

    @Override
    public Engine onCreateEngine() {
        return new Engine();
    }
//
//    private static class EngineHandler extends Handler {
//        private final WeakReference<SunshineWatchFaceService.Engine> mWeakReference;
//
//        public EngineHandler(SunshineWatchFaceService.Engine reference) {
//            mWeakReference = new WeakReference<>(reference);
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            SunshineWatchFaceService.Engine engine = mWeakReference.get();
//            if (engine != null) {
//                switch (msg.what) {
//                    case MSG_UPDATE_TIME:
//                        engine.handleUpdateTimeMessage();
//                        break;
//                }
//            }
//        }
//    }

    private class Engine extends CanvasWatchFaceService.Engine implements
            GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

        private static final String ACTION_TIME_ZONE = "time-zone";
        private static final String TAG = "SunshineEngine";

        private Resources resources = getResources();
        private SunshineWatchFace watchFace;
        private final DataApi.DataListener onDataChangedListener = new DataApi.DataListener() {
            @Override
            public void onDataChanged(DataEventBuffer dataEventBuffer) {
                for (DataEvent dataEvent : dataEventBuffer) {
                    if (dataEvent.getType() == DataEvent.TYPE_CHANGED) {
                        DataItem dataItem = dataEvent.getDataItem();
                        processConfigurationFor(dataItem);
                    }
                }
                dataEventBuffer.release();
                invalidateIfNecessary();
            }
        };
        private final ResultCallback<DataItemBuffer> onConnectedResultCallback = new ResultCallback<DataItemBuffer>() {
            @Override
            public void onResult(@NonNull DataItemBuffer dataItems) {
                for (DataItem dataItem : dataItems) {
                    processConfigurationFor(dataItem);
                }
                dataItems.release();
                invalidateIfNecessary();
            }
        };
        private Handler timeTick;
        private final Runnable timeRunnable = new Runnable() {
            @Override
            public void run() {
                onSecondTick();

                if (isVisible() && !isInAmbientMode())
                    timeTick.postDelayed(this, TICK_PERIOD_MILLIS);

            }
        };
        private GoogleApiClient googleApiClient;
        private BroadcastReceiver timeZoneChangedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Intent.ACTION_TIMEZONE_CHANGED.equals(intent.getAction()))
                    watchFace.updateTimeZoneWith(intent.getStringExtra(ACTION_TIME_ZONE));
            }
        };

        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);

            setWatchFaceStyle(new WatchFaceStyle.Builder(SunshineWatchFaceService.this)
                    .setCardPeekMode(WatchFaceStyle.PEEK_MODE_SHORT)
                    .setAmbientPeekMode(WatchFaceStyle.AMBIENT_PEEK_MODE_HIDDEN)
                    .setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE)
                    .setShowSystemUiTime(false)
                    .build());


            timeTick = new Handler(Looper.myLooper());
            startTimerIfNecessary();

            watchFace = SunshineWatchFace.newInstance(SunshineWatchFaceService.this);
            googleApiClient = new GoogleApiClient.Builder(SunshineWatchFaceService.this)
                    .addApi(Wearable.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
//            watchFace.setContext(getBaseContext());''''
        }


        private void startTimerIfNecessary() {
            timeTick.removeCallbacks(timeRunnable);
            if (isVisible() && !isInAmbientMode())
                timeTick.post(timeRunnable);
        }

        private void onSecondTick() {
            invalidateIfNecessary();
        }

        private void invalidateIfNecessary() {
            if (isVisible() && !isInAmbientMode())
                invalidate();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);

            if (visible) {
                registerTimeZoneReceiver();
                googleApiClient.connect();
            } else {
                unregisterTimeZoneReceiver();
                releaseGoogleApiClient();
            }
            startTimerIfNecessary();
        }

        private void releaseGoogleApiClient() {
            if (googleApiClient != null && googleApiClient.isConnected()) {
                Wearable.DataApi.removeListener(googleApiClient, onDataChangedListener);
                googleApiClient.disconnect();
            }
        }

        private void unregisterTimeZoneReceiver() {
            unregisterReceiver(timeZoneChangedReceiver);
        }

        private void registerTimeZoneReceiver() {
            IntentFilter timeZoneFilter = new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED);
            registerReceiver(timeZoneChangedReceiver, timeZoneFilter);
        }

        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            super.onDraw(canvas, bounds);

            watchFace.draw(canvas, bounds);

        }

        @Override
        public void onTimeTick() {
            super.onTimeTick();
            invalidate();
        }

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);

            watchFace.setAntiAlias(!inAmbientMode);
            watchFace.setShowSeconds(!isInAmbientMode());

            if (inAmbientMode) {
                watchFace.updateBackgroundColorToDefault();
                watchFace.updateDateAndTimeColorToDefault();
            } else {
                watchFace.restoreBackgroundColor();
                watchFace.restoreDateAndTimeDefaultColor();
            }

            invalidate();
            startTimerIfNecessary();
        }

        @Override
        public void onConnected(@Nullable Bundle bundle) {
            Log.d(TAG, "connected GoogleAPI");
            watchFace.setContext(getBaseContext());
            Log.d(TAG, "Bundle : " + bundle);

            Wearable.DataApi.addListener(googleApiClient, onDataChangedListener);
            Wearable.DataApi.getDataItems(googleApiClient).setResultCallback(onConnectedResultCallback);
        }

        private void processConfigurationFor(DataItem dataItem) {
            if (WatchFaceSyncCommons.PATH.equals(dataItem.getUri().getPath())) {
                DataMap dataMap = DataMapItem.fromDataItem(dataItem).getDataMap();

                if (dataMap.containsKey(WatchFaceSyncCommons.KEY_BACKGROUND_COLOUR)) {
                    String backgrounfColor = dataMap.getString(WatchFaceSyncCommons.KEY_BACKGROUND_COLOUR);
                    watchFace.updateBackgroundColorTo(Color.parseColor(backgrounfColor));
                }

                if (dataMap.containsKey(WatchFaceSyncCommons.KEY_DATE_TIME_COLOUR)) {
                    String timeColor = dataMap.getString(WatchFaceSyncCommons.KEY_DATE_TIME_COLOUR);
                    watchFace.updateDateAndTimeColorTo(Color.parseColor(timeColor));
                }
            }
            if (WatchFaceSyncCommons.HIGH_LOW_TEMP_PATH.equals(dataItem.getUri().getPath())) {
                DataMap dataMap = DataMapItem.fromDataItem(dataItem).getDataMap();

                if (dataMap.containsKey(WatchFaceSyncCommons.HIGH_TEMP_KEY)) {
                    double highTemp = dataMap.getDouble(WatchFaceSyncCommons.HIGH_TEMP_KEY);
                    Log.d(getClass().getSimpleName() + "===D", "highTemp : " + highTemp);
                    watchFace.setHighTemp(highTemp);
                }

                if (dataMap.containsKey(WatchFaceSyncCommons.LOW_TEMP_KEY)) {
                    double lowTemp = dataMap.getDouble(WatchFaceSyncCommons.LOW_TEMP_KEY);
                    Log.d(getClass().getSimpleName() + "===D", "lowTemp : " + lowTemp);
                    watchFace.setLowTemp(lowTemp);
                }

                if (dataMap.containsKey(WatchFaceSyncCommons.WEATHER_IMAGE_KEY)) {
                    DataMapItem dataMapItem = DataMapItem.fromDataItem(dataItem);
                    imageResourceId = dataMapItem.getDataMap().getInt(WatchFaceSyncCommons.WEATHER_IMAGE_KEY);
                    Log.d(getClass().getSimpleName() + "===D", "imageResourceId : " + imageResourceId);
                    watchFace.createBitmapFromDrawable(resources, imageResourceId);
                }
            }
        }


        @Override
        public void onConnectionSuspended(int i) {
            Log.e(TAG, "suspended GoogleAPI");
        }

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            Log.e(TAG, "connectionFailed GoogleAPI : the reason : " + connectionResult);
            Log.e(TAG, "error message : " + connectionResult.getErrorMessage());
            Log.e(TAG, "error code : " + connectionResult.getErrorCode());
            Log.e(TAG, "connection status : " + connectionResult.isSuccess());
        }

        @Override
        public void onDestroy() {
            timeTick.removeCallbacks(timeRunnable);
            releaseGoogleApiClient();
            super.onDestroy();
        }
    }
}
