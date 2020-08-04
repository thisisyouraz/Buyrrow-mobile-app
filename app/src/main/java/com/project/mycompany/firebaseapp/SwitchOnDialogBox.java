package com.project.mycompany.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class SwitchOnDialogBox extends AppCompatDialogFragment {

    Switch aSwitch;
    String adId;
    Context mContext;
    Boolean isOnRent;
    AdCredentials credentials;

    public SwitchOnDialogBox(Switch aSwitch, AdCredentials credentials,Context mContext,Boolean isOnRent) {
        this.aSwitch = aSwitch;
        this.adId = credentials.getParentID();
        this.mContext=mContext;
        this.isOnRent=isOnRent;

        this.credentials=credentials;
    }

    public Boolean getOnRent() {
        return isOnRent;
    }

    public void setOnRent(Boolean onRent) {
        isOnRent = onRent;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public Switch getaSwitch() {
        return aSwitch;
    }

    public void setaSwitch(Switch aSwitch) {
        this.aSwitch = aSwitch;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public SwitchOnDialogBox() {
    }


    public Dialog onCreateDialog(Bundle savedInstanceState) {


        SwitchOnDialogBox switchDialog = new SwitchOnDialogBox();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Location may delete.")
                .setCancelable(false)
                .setMessage("This action may result in loosing last location associated with the ad")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //my check on code
                        aSwitch.setChecked(true);
                        FirebaseDatabase.getInstance().getReference("Current Location").child(adId).removeValue();
                        isOnRent=true;
                        credentials.setIsActive("active");
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        aSwitch.setChecked(false);
                        isOnRent=false;
                        credentials.setIsActive("In active");
                    }
                });

        return builder.create();
    }
}
