package com.example.pavan.sunshine;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;

/**
 * Created by pavan on 7/4/2016.
 */
public class LocationEditTextPreference extends EditTextPreference {
    private static final int DEFAULT_MINIMUM_LOCATION_LENGTH = 2;
    private int mMinLength;

    public LocationEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.LocationEditTextPreference, 0, 0);

        try {
            mMinLength = a.getInteger(R.styleable.LocationEditTextPreference_minLength, DEFAULT_MINIMUM_LOCATION_LENGTH);
        } finally {
            a.recycle();
        }

        // Check to see if Google Play services is available. The Place Picker API is available
        // through Google Play Services, so if this is false, we'll just carry on as though this
        // feature does not exist. If it is true, however, we can add a widget to our preference.
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(getContext());

        if (resultCode == ConnectionResult.SUCCESS)
            // Add the get current location widget to our location preference
            setWidgetLayoutResource(R.layout.pref_current_location);

    }


    @Override
    protected View onCreateView(ViewGroup parent) {
        View view = super.onCreateView(parent);
        View currentLocation = view.findViewById(R.id.current_location);
        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = getContext();

                // Launch the Place Picker so that the user can specify their location, and then
                // return the result to SettingsActivity.
                // TODO(student): Create a PlacePicker.IntentBuilder object here.

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                // We are in a view right now, not an activity. So we need to get ourselves
                // an activity that we can use to start our Place Picker intent. By using
                // SettingsActivity in this way, we can ensure the result of the Place Picker
                // intent comes to the right place for us to process it.
                Activity settingsActivity = (SettingsActivity) context;
                try {
                    // TODO(student): Launch the intent using your settingsActivity object to access
                    // startActivityForResult(). You'll need to build your builder object and use
                    // the request code we declared in SettingsActivity.
                    settingsActivity.startActivityForResult(builder.build(context), SettingsActivity.PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {
                    // What did you do?? This is why we check Google Play services in onResume!!!
                    // The difference in these exception types is the difference between pausing
                    // for a moment to prompt the user to update/install/enable Play services vs
                    // complete and utter failure.
                    // If you prefer to manage Google Play services dynamically, then you can do so
                    // by responding to these exceptions in the right moment. But I prefer a cleaner
                    // user experience, which is why you check all of this when the app resumes,
                    // and then disable/enable features based on that availability.
                }

            }
        });

        return view;
    }





    @Override
    protected void showDialog(Bundle state) {
        super.showDialog(state);

        EditText et = getEditText();
        et.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                Dialog d = getDialog();

                if (d instanceof AlertDialog) {
                    AlertDialog dialog = (AlertDialog) d;
                    Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

                    // Check if the EditText is empty
                    if (s.length() < mMinLength)
                        // Disable OK button
                        positiveButton.setEnabled(false);
                    else
                        // Re-enable the button
                        positiveButton.setEnabled(true);
                }
            }
        });
    }
}
