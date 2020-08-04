package com.project.mycompany.firebaseapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.hotspot2.pps.Credential;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class Create_Ad extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_CODE_IMAGE = 101;
    private ImageView imageView1, imageView2, imageView3, imageView4;

    RadioButton bid, cell, rent;
    String adType, id, title, price, specs, category, debug = "debug", endTime, endDate, buyItNow;
    EditText titleEt, priceEt, descriptionEt;
    Button uploadBtn;
    Uri[] imageUri = new Uri[4];


    Spinner categorylist;
    Boolean isImageAdded = false;
    private DatabaseReference mref;
    private StorageReference storageReference;
    Toolbar toolbar;
    DrawerLayout mNavDrawer;
    NavigationView navigationView;
    Boolean isUploaded = true;
    private String maxBid = "dummy", bidderName = "dummy";

    private AdCredentials credentials;


    private ArrayList<String> downloadAbleLink = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer_create_ad);


        initialize();
        setSupportActionBar(toolbar);
        categorylist.setAdapter(new ArrayAdapter<String>
                (this, R.layout.support_simple_spinner_dropdown_item, AdCategoriesList.categoryList));

        navigationView.getMenu().getItem(1).setChecked(true);
        navIcon();
        setUserCredentials();
        getSupportActionBar().setTitle("Create ad");

        navigationView.setNavigationItemSelectedListener(this);




        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 2);
            }
        });


        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 3);
            }
        });

        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 4);
            }
        });

        final int temp = 1;
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                    Intent intent = new Intent(getApplicationContext(), Bid_credential_AdType.class);
//                    startActivity(intent);
//                    return;


                if (!isImageAdded) {
                    Toast.makeText(Create_Ad.this, "Atleast one image should be selected", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isRadioButtonSelected()) {
                    Toast.makeText(Create_Ad.this, "Ad type is not selected", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(titleEt.getText().toString())) {
                    titleEt.setError("Title is required");
                    titleEt.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(priceEt.getText().toString())) {
                    priceEt.setError("price is required");
                    priceEt.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(descriptionEt.getText().toString())) {
                    descriptionEt.setError("specifications required");
                    descriptionEt.requestFocus();
                    return;
                } else {
                    uploadAd();

                    Log.d("debug", "ad upload finish");
                    Log.d("debug", " " + downloadAbleLink.size());
                }
            }
        });

    }

    private void initialize() {

        categorylist = findViewById(R.id.category_spinner);
        toolbar = findViewById(R.id.toolbar_create_ad);
        mNavDrawer = findViewById(R.id.drawer_layout_create_ad);
        navigationView = findViewById(R.id.navigation_view);


        imageView1 = findViewById(R.id.id_imageView1);
        imageView2 = findViewById(R.id.id_imageView2);
        imageView3 = findViewById(R.id.id_imageView3);
        imageView4 = findViewById(R.id.id_imageView4);


        titleEt = findViewById(R.id.id_title);
        priceEt = findViewById(R.id.id_price);
        descriptionEt = findViewById(R.id.id_desc_spec);
        bid = findViewById(R.id.id_bid);
        cell = findViewById(R.id.id_cell);
        rent = findViewById(R.id.id_rent);
        uploadBtn = findViewById(R.id.id_upload_btn);
        mref = FirebaseDatabase.getInstance().getReference("Admin approve ads");
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    int sucessCount = 0, temp = 0;



    public void uploadAd() {

        id = FirebaseAuth.getInstance().getUid();
        title = titleEt.getText().toString();
        price = priceEt.getText().toString();
        specs = descriptionEt.getText().toString();

        endDate = "dummy";
        endTime = "dummy";
        buyItNow ="dummy";
        category = categorylist.getSelectedItem().toString();


        checkAdType();

        if (!adType.equals("Bid")) {

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

                            if (sucessCount == temp) {

//                                checkAdType();
//                                    id = FirebaseAuth.getInstance().getUid();
//                                    title = titleEt.getText().toString();
//                                    price = priceEt.getText().toString();
//                                    specs = descriptionEt.getText().toString();
//
//                                    endDate = "dummy";
//                                    endTime = "dummy";
//                                    buyItNow = "dummy";


                                    String credentialKey = mref.push().getKey();
                                    String isActive="active";
                                    AdCredentials credentials = new AdCredentials(credentialKey, title, price, adType, specs
                                            , id, maxBid, bidderName
                                            , category, downloadAbleLink
                                            , endDate, buyItNow,isActive);


                                    mref.child(credentialKey).setValue(credentials)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    Log.d(debug, "on complete ");

                                                    if (task.isSuccessful()) {
                                                        Log.d(debug, "onComplete: ad uploaded sucessfully");

                                                        Toast.makeText(Create_Ad.this, "ad sent to admin for approval", Toast.LENGTH_SHORT).show();

                                                        Intent intent = new Intent(Create_Ad.this, Create_Ad.class);
                                                        startActivity(intent);
                                                        finish();

                                                        progressDialog.dismiss();
                                                    } else {
                                                        Toast.makeText(Create_Ad.this, "something went wrong plz try again later.", Toast.LENGTH_SHORT).show();
                                                        progressDialog.dismiss();
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Create_Ad.this, "something went wrong plz try again later.", Toast.LENGTH_SHORT).show();
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
        else {
            Intent intent = new Intent(getApplicationContext(), Bid_credential_AdType.class);

            Credentials credentials=new Credentials(title,imageUri,adType,specs,price,category);

            intent.putExtra("callFrom","createAd");
            intent.putExtra("credential",credentials);

            startActivity(intent);
            return;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null) {
            Log.d("testing", "in 1st image: ");
            imageUri[0] = data.getData();
            isImageAdded = true;
            imageView1.setImageURI(imageUri[0]);

            Log.d("uri value", " " + imageUri[0]);
        } else if (requestCode == 2 && data != null) {
            Log.d("testing", "in 2nd image: ");
            imageUri[1] = data.getData();
            isImageAdded = true;
            imageView2.setImageURI(imageUri[1]);
            Log.d("uri value", " " + imageUri[1]);

        } else if (requestCode == 3 && data != null) {
            Log.d("testing", "in 3rd image: ");
            imageUri[2] = data.getData();
            isImageAdded = true;
            imageView3.setImageURI(imageUri[2]);
            Log.d("uri value", " " + imageUri[2]);
        } else if (requestCode == 4 && data != null) {
            Log.d("testing", "in 4th image: ");
            imageUri[3] = data.getData();
            isImageAdded = true;
            imageView4.setImageURI(imageUri[3]);
            Log.d("uri value", " " + imageUri[3]);
        }
    }

    public void checkAdType() {

        if (bid.isChecked()) {
            adType = "Bid";
        } else if (cell.isChecked()) {
            adType = "Sell";
        } else if (rent.isChecked()) {
            adType = "Rent";
        }
    }

    public Boolean isRadioButtonSelected() {
        if (!bid.isChecked() && !cell.isChecked() && !rent.isChecked()) {
            return false;
        } else return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.idPostAd) {
            Intent intent = new Intent(getApplicationContext(), Create_Ad.class);
            startActivity(intent);
            finish();

        } else if (item.getItemId() == R.id.idAboutAs) {
            Intent intent = new Intent(getApplicationContext(), About_us.class);
            startActivity(intent);
            finish();
        } else if (item.getItemId() == R.id.idMyAds) {
            Intent intent = new Intent(getApplicationContext(), My_ads.class);
            startActivity(intent);
            finish();
        } else if (item.getItemId() == R.id.idHome) {
            Intent intent = new Intent(getApplicationContext(), Home.class);
            startActivity(intent);
            finish();
        } else if (item.getItemId() == R.id.idAdminHomeActivity) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            FirebaseAuth.getInstance().signOut();
            startActivity(intent);
            finish();
        } else if (item.getItemId() == R.id.idSignOut) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }


        return true;
    }

    public void navIcon() {

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mNavDrawer, toolbar, R.string.nav_app_bar_open_drawer_description,
                R.string.navigation_drawer_close
        );
        mNavDrawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Create_Ad.this, Home.class);
        startActivity(intent);
        finish();
    }

    private void setUserCredentials() {


        FirebaseAuth currentUser = FirebaseAuth.getInstance();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.headerName);
        TextView navUserMail = headerView.findViewById(R.id.header_email);
        navUserMail.setText(currentUser.getCurrentUser().getEmail());


        String email = currentUser.getCurrentUser().getEmail();
        String[] name = email.split("@");
        navUsername.setText(name[0]);
        name = null;
    }

}
