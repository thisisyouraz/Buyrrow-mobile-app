package com.project.mycompany.firebaseapp;

import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDialogFragment;

public class LocationNotFoundDialog extends AppCompatDialogFragment {
    public Dialog onCreateDialog( Bundle savedInstanceState) {

//        super.onSaveInstanceState(savedInstanceState);

        LocationNotFoundDialog exampleDialog = new LocationNotFoundDialog();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Location not found!")
                .setCancelable(false)
                .setMessage("your ad has not been rented yet.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }


}
