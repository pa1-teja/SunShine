package com.example.pavan.sunshine.sunshinewear;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.Wearable;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * Created by KVR on 12/4/2016.
 */

public class imageProcessingTask extends AsyncTask {
    private static final String TAG = "SunshineEngine";

    Bitmap bitmap;
    private GoogleApiClient googleApiClient;
    private Asset asset;
    private SunshineWatchFace watchFace;
    private Paint paint = new Paint();

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        watchFace = new SunshineWatchFace();
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        googleApiClient = (GoogleApiClient) objects[1];
        asset = (Asset) objects[0];
        bitmap = loadBitmapFromAsset(asset, googleApiClient, paint);
        return bitmap;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        bitmap = (Bitmap) o;

    }

    public Bitmap loadBitmapFromAsset(Asset asset, GoogleApiClient googleApiClient, Paint paint) {
        if (asset == null) {
            throw new IllegalArgumentException("Asset must be non-null");
        }
        long TIME = 1000;
        ConnectionResult result =
                googleApiClient.blockingConnect(TIME, TimeUnit.MILLISECONDS);
        if (!result.isSuccess()) {
            return null;
        }
//             convert asset into a file descriptor and block until it's ready
        InputStream assetInputStream = Wearable.DataApi.getFdForAsset(
                googleApiClient, asset).await().getInputStream();
        googleApiClient.disconnect();

        if (assetInputStream == null) {
            Log.w(TAG, "Requested an unknown Asset.");
            return null;
        }
        Log.d(TAG, "processed image");

        // decode the stream into a bitmap
        return BitmapFactory.decodeStream(assetInputStream);
    }

}
