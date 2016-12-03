package com.example.pavan.sunshine.SunshineWearable.WearableCompanion;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.pavan.sunshine.R;


/**
 * Created by KVR on 11/29/2016.
 */

public class ColorChooserDialog extends DialogFragment {
    private static final String ARG_TITLE = "ARG_TITLE";
    private Listener colorSelectedListener;

    public static ColorChooserDialog newInstance(String dialogTitle) {
        Bundle arguments = new Bundle();
        arguments.putString(ARG_TITLE, dialogTitle);
        ColorChooserDialog dialog = new ColorChooserDialog();
        dialog.setArguments(arguments);
        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        colorSelectedListener = (Listener) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString(ARG_TITLE);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setItems(R.array.colors_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String[] colors = getResources().getStringArray(R.array.colors_array);
                        colorSelectedListener.onColorSelected(colors[which], getTag());
                    }
                });

        return builder.create();
    }

    public interface Listener {
        void onColorSelected(String color, String tag);
    }
}
