package com.project.mycompany.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Bid_credential_AdType extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    Toolbar toolbar;
    EditText endTimeEt, endDateEt, buyItnowEt;
    String callFrom, adType, uid, title, minPrice, specs, category, endTime, endDate, buyItNow, maxBid = "dummy", bidderName = "dummy";

    TextView startDateTv;
    DatabaseReference mref = FirebaseDatabase.getInstance().getReference("Admin approve ads");
    int temp, sucessCount = 0;
    StorageReference storageReference;
    Uri[] imageUri;
    Button uploadBtn;

    Calendar endDateValue, startDate;
    ArrayList<String> downloadAbleLink = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_credential__ad_type);
        initialize();

        Intent intent = getIntent();
        final Credentials credentials = intent.getParcelableExtra("credential");
        callFrom = intent.getStringExtra("callFrom");

        if (callFrom.equals("createAd")) {
            minPrice = credentials.getPrice();
        }
        startDate = Calendar.getInstance();
        String date = DateFormat.getDateInstance(DateFormat.FULL).format(startDate.getTime());
        startDateTv.setText(date);


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create ad");


        endDateEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

//        endTimeEt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DialogFragment timePicker = new TimePickerFragment();
//                timePicker.show(getSupportFragmentManager(), "time picker");
//            }
//        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                String date1 = DateFormat.getDateInstance(DateFormat.FULL).format(startDate.getTime());
                String date2 = DateFormat.getDateInstance(DateFormat.FULL).format(endDateValue.getTime());

                if (TextUtils.isEmpty(endDateEt.getText()) || endDateValue.before(startDate) || date1.compareTo(date2)==0) {
                    endDateEt.setError("Valid End Date is required");
                    endDateEt.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(buyItnowEt.getText()) || Integer.parseInt(buyItnowEt.getText().toString()) < Integer.parseInt(minPrice)) {
                    buyItnowEt.setError("valid offer is required");
                    buyItnowEt.requestFocus();
                    return;
                } else
                    uploadAd(credentials);
            }
        });
    }

    private void uploadAd(Credentials credentials) {

        adType = credentials.getAdType();
        title = credentials.getTitle();
        specs = credentials.getSpecs();
        category = credentials.getCategory();
        endDate = endDateEt.getText().toString();
        buyItNow = buyItnowEt.getText().toString();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        imageUri = credentials.getUri();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Ad Uploading...");
        progressDialog.show();

        for (int i = 0; i < imageUri.length; i++) {
            if (imageUri[i] != null) {
                temp++;
                storageReference.child("data of posted ads").child(imageUri[i].getLastPathSegment()).
                        putFile(imageUri[i]).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        sucessCount++;

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete()) ;
                        Uri urlImage = uriTask.getResult();
                        String link = urlImage.toString();
                        downloadAbleLink.add(link);
                        String isActive="active";

                        if (sucessCount == temp) {
                            String credentialKey = mref.push().getKey();

                            AdCredentials credentials = new AdCredentials(credentialKey, title, minPrice, adType, specs
                                    , uid, maxBid, bidderName
                                    , category, downloadAbleLink
                                    , endDate, buyItNow,isActive);

//                            credentials.setCalendarEndDate(endDateValue);

                            mref.child(credentialKey).setValue(credentials)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {
                                                Toast.makeText(Bid_credential_AdType.this, "ad sent to admin for approval", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(Bid_credential_AdType.this, Create_Ad.class);
                                                // set the new task and clear flags
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);

//                                                finish();

                                                progressDialog.dismiss();
                                            } else {
                                                Toast.makeText(Bid_credential_AdType.this, "something went wrong plz try again later.", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Bid_credential_AdType.this, "something went wrong plz try again later.", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();

                                }
                            });
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        }

    }

    private void initialize() {

        startDateTv = findViewById(R.id.start_DateTv);
        uploadBtn = findViewById(R.id.id_upload_btn_bidding_type);
        toolbar = findViewById(R.id.toolbar_bid_credentials);
        endTimeEt = findViewById(R.id.id_end_time);
        endDateEt = findViewById(R.id.id_end_Date);
        buyItnowEt = findViewById(R.id.id_buy_it_now);
        storageReference = FirebaseStorage.getInstance().getReference();

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DATE, dayOfMonth);

        endDateValue = c;
        String date = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        endDateEt.setText(date);
    }

//    @Override
//    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//
//        view.setIs24HourView(false);
//
//        endTimeEt.setText(hourOfDay + ":" + minute+"");
//    }
}
