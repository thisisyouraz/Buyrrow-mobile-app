package com.project.mycompany.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BidAd_detail_admin extends AppCompatActivity {

    Toolbar toolbar;
    ImageSlider slider;
    TextView tvTitle, tvStartingPrice, tvMaxOffer, tvBidderContact, tvDesc, totalBidsTv, sellerContact;
    Button callBidderBtn, delBtn;
    private static final int REQUEST_PHONE_CALL = 1;
    String callFrom,str;
    int totalBids=0;
    String[]bidderContact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_ad_detail_admin);
        toolbar = findViewById(R.id.toolbar_bid_detail);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Ad detail");
        Intent intent = getIntent();
        final AdCredentials credentials = intent.getParcelableExtra("adInfo");
        callFrom = intent.getStringExtra("call from");


        sellerContact = findViewById(R.id.id_seller_number);
        totalBidsTv = findViewById(R.id.totalBids);
        slider = findViewById(R.id.id_slider_my_ads);
        callBidderBtn = findViewById(R.id.id_contactBidder_btn);
        delBtn = findViewById(R.id.id_deleteAd_btn);
        tvTitle = findViewById(R.id.id_title_for_admin);
        tvStartingPrice = findViewById(R.id.id_startingPrice_value);
        tvMaxOffer = findViewById(R.id.id_MaxOfferTv);
        tvBidderContact = findViewById(R.id.id_bider_number);
        tvDesc = findViewById(R.id.id_bidDetail_desc_admin);

        setImages(credentials);

        if (credentials.getMax_offer().equals("dummy")) {
            callBidderBtn.setVisibility(View.INVISIBLE);
            tvMaxOffer.setText("Max offer: No offer yet");
            tvBidderContact.setText("Winner contact: No bidder yet");
        } else {
            str="Maximum Offer: "+credentials.getMax_offer();
            tvMaxOffer.setText(str);
            setBidderContact(credentials.getBidder_id());
        }

        tvTitle.setText(credentials.getTitle().toString());
        str="Rs "+credentials.getPrice();
        tvStartingPrice.setText(str);

        setOwnerContact(credentials.getSellerId());
        tvDesc.setText(credentials.getAdDesSpec());

        setTotalBids(credentials.getParentID());
        Log.d("test", "onCreate: "+totalBids);

        totalBidsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                Bundle bundle=new Bundle();
//                bundle.putSerializable("list", (Serializable) myOfferList);
//

                Intent intent =new Intent(BidAd_detail_admin.this,BiddersDetail.class);
                intent.putExtra("adId",credentials.getParentID());
                startActivity(intent);
            }
        });

        callBidderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent callIntent = new Intent(Intent.ACTION_CALL);

                bidderContact = tvBidderContact.getText().toString().split(":");

                callIntent.setData(Uri.parse("tel:" + bidderContact[1]));
                if (ContextCompat.checkSelfPermission(BidAd_detail_admin.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(BidAd_detail_admin.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                } else {
                    startActivity(callIntent);
                }
            }
        });

        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String removeAdiD = credentials.getParentID();

                Log.d("removeAdiD", " " + removeAdiD);

                FirebaseDatabase.getInstance().getReference("Data of posted ads")
                        .child(removeAdiD).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            FirebaseDatabase.getInstance().getReference("biding info")
                                    .child(removeAdiD).removeValue();

                            if (callFrom.equals("myAds")) {
                                Intent intent = new Intent(BidAd_detail_admin.this, My_ads.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Intent intent = new Intent(BidAd_detail_admin.this, AdminHome.class);
                                startActivity(intent);
                                finish();
                            }

                        } else {
                            Toast.makeText(BidAd_detail_admin.this, "ad not deleted sucessfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


    List<OffersDetail> myOfferList=new ArrayList<>();
    private void setTotalBids(String parentID) {

        FirebaseDatabase.getInstance().getReference("biding info").child(parentID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();


                        for (DataSnapshot item:children) {
                            totalBids++;
                            OffersDetail offer=item.getValue(OffersDetail.class);
                            Log.d("test", "onDataChange: "+offer.amount);
                            myOfferList.add(offer);
                        }
                        str="total Bids: "+totalBids;
                        totalBidsTv.setText(str);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void setImages(AdCredentials credentials) {

        List<SlideModel> slideModels = new ArrayList<>();

        for (int i = 0; i < credentials.getImageLinks().size(); i++) {
            slideModels.add(new SlideModel(credentials.getImageLinks().get(i), ScaleTypes.FIT));
        }

        slider.setImageList(slideModels, ScaleTypes.FIT);
    }

    private void setOwnerContact(final String id) {
        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("users");
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> usersList = dataSnapshot.getChildren();

                for (DataSnapshot user : usersList) {
                    if (user.getKey().equals(id)) {
                        User owner = user.getValue(User.class);
                        str="Seller Contact: "+owner.getContact();
                        sellerContact.setText(str);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setBidderContact(final String bidder_id) {
        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("users");
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> usersList = dataSnapshot.getChildren();


                for (DataSnapshot user : usersList) {
                    if (user.getKey().equals(bidder_id)) {
                        User bidder = user.getValue(User.class);
                        str="Winner Contact: "+bidder.getContact();
                        tvBidderContact.setText(str);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onBackPressed() {

        if (callFrom.equals("myAds")) {

            Intent intent = new Intent(BidAd_detail_admin.this, My_ads.class);
            startActivity(intent);
            finish();
        } else if (callFrom.equals("adminHome")) {
            Intent intent = new Intent(BidAd_detail_admin.this, AdminHome.class);
            startActivity(intent);
            finish();
        }
        super.onBackPressed();
    }
}
