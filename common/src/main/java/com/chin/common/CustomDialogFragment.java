package com.chin.common;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class CustomDialogFragment extends DialogFragment {
    private static String MESSAGE = "message";

    public static CustomDialogFragment newInstance(String message) {
        CustomDialogFragment fragment = new CustomDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(MESSAGE, message);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String message = getArguments().getString(MESSAGE);
        builder.setMessage(message)
               .setPositiveButton("Quit", new DialogInterface.OnClickListener() {
                   @Override
                public void onClick(DialogInterface dialog, int id) {
                     System.exit(0);
                   }
               });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}