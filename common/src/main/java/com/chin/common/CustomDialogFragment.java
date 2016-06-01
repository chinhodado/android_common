package com.chin.common;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

@SuppressLint("NewApi")
public class CustomDialogFragment extends DialogFragment {
    String message;

    public CustomDialogFragment(String message) {
        this.message = message;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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