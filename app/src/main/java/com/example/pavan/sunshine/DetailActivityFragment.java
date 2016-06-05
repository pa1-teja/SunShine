package com.example.pavan.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private final String LOG_TAG = getClass().getSimpleName();
    private String weatherStr;
    private ShareActionProvider shareActionProvider;
    private MenuItem shareItem;

    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        shareItem = menu.findItem(R.id.share_menu_button);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);

        if (shareActionProvider != null)
            shareActionProvider.setShareIntent(shareTemperatureOfTheDay());
        else
            Log.d(LOG_TAG, "Share Action Provider is null?");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Intent intent = getActivity().getIntent();
        TextView weatherText = (TextView) rootView.findViewById(R.id.weather_textView);

        weatherStr = intent.getStringExtra("weather");

        weatherText.setText(weatherStr);

        return rootView;
    }

    Intent shareTemperatureOfTheDay() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, weatherStr + "#SunshineApp");
        return shareIntent;
    }

}
