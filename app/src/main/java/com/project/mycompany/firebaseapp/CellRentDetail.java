package com.project.mycompany.firebaseapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CellRentDetail extends AppCompatActivity {

    Toolbar toolbar;
    DatabaseReference mref;
    private TextView title, adType, price, contact, desc;
    private Button callBtn, trackBtn;

    TextView tvCategory;
    private String adTypeForBtn, adId;
    private static final int REQUEST_PHONE_CALL = 1;
    ImageSlider slider;
    private String contactNumber, callFrom, str;
    Boolean isAvailable = true;
    String[] sellerContact;
    View view;
    Switch aSwitch;
    int i;
    Boolean isProductOnRent = true, inUse = false;
    AdCredentials credentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cell_rent_detail);
        initialize();
        getSupportActionBar().setTitle("Ad detail");
        Intent intent = getIntent();
        credentials = intent.getParcelableExtra("adInfo");
        callFrom = intent.getStringExtra("call from");

        if (credentials.getAdType().equals("Rent") && (callFrom.equals("myAds") || callFrom.equals("adminHome"))) {
            trackBtn.setVisibility(View.VISIBLE);
        }


//        Glide.with(this).load(credentials.getDownloadAbleLink()).into(imageView);
        setImages(credentials);

        title.setText(credentials.getTitle());
        adType.setText(credentials.getAdType());

        tvCategory.setText(credentials.getCategory());
        str = "Rs: " + credentials.getPrice();
        price.setText(str);
        desc.setText(credentials.getAdDesSpec());
        adTypeForBtn = credentials.getAdType();
        adId = credentials.getParentID();

        if (!(callFrom.equals("home") || callFrom.equals("MapActivity"))) {
            if (credentials.getAdType().equals("Rent")) {

                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.switch_layout, null);
                ViewGroup insertPoint = (ViewGroup) findViewById(R.id.insertion_point);
                insertPoint.addView(view, 0);


                Log.d("switch", "before checkAvalability: " + credentials.getIsActive());
                availabilityForRent(credentials.getParentID(), credentials);
                aSwitch = (Switch) findViewById(R.id.switch_on_off);
                if (inUse == false) {
                    if (credentials.getIsActive().equals("active")) {
                        i = 2;
                        aSwitch.setChecked(true);

                    } else if (!credentials.getIsActive().equals("active")) {
                        aSwitch.setChecked(false);
                    }
                }

                aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked == true) {
                            if (i == 0) {
                                SwitchOnDialogBox switchDialog = new SwitchOnDialogBox(aSwitch, credentials, getApplicationContext(), isProductOnRent);
                                switchDialog.show(getSupportFragmentManager(), "switchDialog");
                                Log.d("active", "onCheckedChanged: " + isProductOnRent);

                                i = 0;
                                return;
                            }
                            else {
                                credentials.setIsActive("active");
                                isProductOnRent = true;
                                i = 0;
                                return;
                            }

                        } else if (isChecked == false) {
                            isProductOnRent = false;
                            credentials.setIsActive("InActive");
                            i = 0;
                        }
                    }
                });
            }
        }

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot data : children) {
                    if (data.getKey().equals(credentials.getSellerId())) {
                        User user = data.getValue(User.class);
//                        contactNumber = "Seller contact: " + user.getContact();
                        contact.setText(user.getContact());

                        if (callFrom.equals("home") || callFrom.equals("MapActivity")) {
                            if (adTypeForBtn.equals("Sell")) {
//                                sellerContact = contactNumber.split(":");

                                callBtn.setText(contact.getText().toString());
                            } else if (adTypeForBtn.equals("Rent")) {
                                checkAvailability(credentials.getParentID());
                                aSwitch = (Switch) findViewById(R.id.switch_on_off);

                                if (isAvailable == true && credentials.getIsActive().equals("active")) {
                                    callBtn.setText("Apply for Rent");
                                } else {
                                    callBtn.setText("Not avaialable");
                                    callBtn.setBackgroundColor(Color.parseColor("#ADD8E6"));
                                }
                            }
                        } else if (callFrom.equals("myAds") || callFrom.equals("adminHome")) {
                            callBtn.setText("Delete ad");
                        }
                        return;
                    }
                }
            }

            private void checkAvailability(final String adId) {
                FirebaseDatabase.getInstance().getReference("Current Location")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                                for (DataSnapshot item : children) {
                                    if (item.getKey().equals(adId)) {
                                        isAvailable = false;
//                                        callBtn.setEnabled(false)
                                        callBtn.setText("Not avaialable");
                                        callBtn.setBackgroundColor(Color.parseColor("#ADD8E6"));

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        trackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(CellRentDetail.this, "track location clicked", Toast.LENGTH_SHORT).show();

                final String currentAdId = credentials.getParentID();
                final List<LocationHelper> adLocation = new ArrayList<>();

                DatabaseReference mref = FirebaseDatabase.getInstance().getReference("Current Location");

                mref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> adIdsOfRentAds = dataSnapshot.getChildren();
                        adLocation.clear();
                        for (DataSnapshot item : adIdsOfRentAds) {
                            if (item.getKey().equals(currentAdId)) {
                                LocationHelper locationOfAd = item.getValue(LocationHelper.class);
                                adLocation.add(locationOfAd);
                            }
                        }
                        if (adLocation.size() == 0) {
//                            Toast.makeText(CellRentDetail.this, "location not fount", Toast.LENGTH_SHORT).show();

                            LocationNotFoundDialog dialog = new LocationNotFoundDialog();
//                            dialog.commitAllowingStateLoss();

                            try {
                                dialog.show(getSupportFragmentManager(), "example dialog");
                            }catch (Exception e){

                            }
                        } else {
                            LocationHelper location = adLocation.get(0);
                            Intent intent = new Intent(CellRentDetail.this, RetrieveMapsActivity.class);
                            intent.putExtra("locationOfAd", location);
                            startActivity(intent);

                            //    Toast.makeText(CellRentDetail.this, "location found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
//
//                Intent intent= new Intent(CellRentDetail.this,RetrieveMapsActivity.class);
//                startActivity(intent);
            }
        });


        callBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (callFrom.equals("home") || callFrom.equals("MapActivity")) {
                    if (adTypeForBtn.equals("Sell")) {

                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + contact.getText().toString()));
                        if (ContextCompat.checkSelfPermission(CellRentDetail.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CellRentDetail.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                        } else {
                            startActivity(callIntent);
                        }
                    } else if (adTypeForBtn.equals("Rent")) {

                        if (isAvailable == true && credentials.getIsActive().equals("active")) {
                            Intent intent = new Intent(CellRentDetail.this, MapsActivity.class);
                            intent.putExtra("ad_id", adId);
                            intent.putExtra("credential", credentials);
                            startActivity(intent);
                            finish();
                        }
                    }
                } else {

                    if (credentials.getAdType().equals("Rent")) {
                        Log.d("rent", "in rent ");
                        removeFromLocation(credentials.getParentID());
                    }
                    String removeAdiD = credentials.getParentID();

                    Log.d("removeAdiD", " " + removeAdiD);
                    FirebaseDatabase.getInstance().getReference("Data of posted ads")
                            .child(removeAdiD).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                if (callFrom.equals("adminHome")) {
                                    Intent intent = new Intent(CellRentDetail.this, AdminHome.class);
                                    startActivity(intent);
                                    finish();


                                } else {
                                    Intent intent = new Intent(CellRentDetail.this, My_ads.class);
                                    startActivity(intent);
                                    finish();
                                }

                            } else {
                                Toast.makeText(CellRentDetail.this, "ad not deleted sucessfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }

    private void availabilityForRent(String parentID, final AdCredentials credentials) {

        FirebaseDatabase.getInstance().getReference("Current Location")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        Log.d("switch", "before looping through locations node: " + credentials.getIsActive());

                        for (DataSnapshot item : children) {
                            if (item.getKey().equals(adId)) {
//                                isAvailable = false;
                                aSwitch = (Switch) view.findViewById(R.id.switch_on_off);
                                aSwitch.setChecked(false);
                                inUse = true;
                                credentials.setIsActive("inActive");
                                Log.d("switch", "already on rent: " + credentials.getIsActive());


                                return;
                            }
                        }
//                        i = 0;
//                        aSwitch.setChecked(true);

//                        i = 1;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void removeFromLocation(String parentID) {

        FirebaseDatabase.getInstance().getReference("Current Location").child(parentID).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CellRentDetail.this, "Ad deleted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CellRentDetail.this, "Ad not deleted from location", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void setImages(AdCredentials credentials) {

        List<SlideModel> slideModels = new ArrayList<>();

        for (int i = 0; i < credentials.getImageLinks().size(); i++) {
            slideModels.add(new SlideModel(credentials.getImageLinks().get(i)));
        }

        slider.setImageList(slideModels, true);
    }

    private void initialize() {

        tvCategory=findViewById(R.id.id_category);
        toolbar = findViewById(R.id.toolbar_cellRent_detail);
        setSupportActionBar(toolbar);
        callBtn = findViewById(R.id.id_call_btn);

        slider = findViewById(R.id.image_slider_cellRent_Detail);
//        imageView = findViewById(R.id.id_imageView_cellDetail);
        mref = FirebaseDatabase.getInstance().getReference().child("users");
        title = findViewById(R.id.id_title_cellRent);
        adType = findViewById(R.id.id_cellRent_adType);
        price = findViewById(R.id.id_cellRent_price);
        contact = findViewById(R.id.id_cellRent_number);
        desc = findViewById(R.id.id_cellRent_desc);
        trackBtn = findViewById(R.id.id_track_btn);

    }

    public void onBackPressed() {

        FirebaseDatabase.getInstance().getReference("Data of posted ads")
                .child(credentials.getParentID()).setValue(new AdCredentials(credentials.getParentID(),
                credentials.getTitle(),
                credentials.getPrice(),
                credentials.getAdType(),
                credentials.getAdDesSpec(),
                credentials.getSellerId(),
                credentials.getMax_offer(),
                credentials.getBidder_id(),
                credentials.getCategory(),
                credentials.getImageLinks(),
                credentials.getEndDate(),
                credentials.getBuyItNow(),
                credentials.getIsActive())
        );


        if (callFrom.equals("myAds")) {

            Intent intent = new Intent(CellRentDetail.this, My_ads.class);
            startActivity(intent);
            finish();
        } else if (callFrom.equals("adminHome")) {
            Intent intent = new Intent(CellRentDetail.this, AdminHome.class);
            startActivity(intent);
            finish();
        }
        super.onBackPressed();
    }


}
