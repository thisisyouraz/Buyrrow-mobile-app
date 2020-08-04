package com.project.mycompany.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.wifi.hotspot2.pps.Credential;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BidDetail extends AppCompatActivity {

    Toolbar toolbar;
    DatabaseReference mref;
    private EditText myOffer;
    private Button sendOfferBtn;
    Double upto;
    Double tenPercent;
    String isDateExpire = "notExpire";
    LinearLayoutCompat offerLayout;
    private TextView addUpTo, releasedPrice, titletv, contacttv, desctv, highestOffer, buyItNow, endDate, tvCategory;
    ImageSlider slider;

    int myLastOffer=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bid_detail);
        initialize();
        getSupportActionBar().setTitle("AD Details");

        final Intent intent = getIntent();
        final AdCredentials credentials = intent.getParcelableExtra("adInfo");
        myLastOffer=checkLastOffer(credentials);

        tenPercent = (Double.parseDouble(credentials.getPrice().toString())) * 0.10;

        setData(credentials);

        sendOfferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String winnerId = credentials.getBidder_id();
                String maxOffer;
//                AdCredentials credentials = new AdCredentials(credentialKey, title, minPrice, adType, specs
//                        , uid, maxBid, bidderName
//                        , category, downloadAbleLink
//                        , endDate, buyItNow);
//

                if (TextUtils.isEmpty(myOffer.getText())) {
                    myOffer.setError("offer should not be empty");
                    myOffer.requestFocus();
                } else if (isOfferValid(credentials)) {

                    if (!credentials.getMax_offer().equals("dummy")) {

                        int enteredOffer = Integer.parseInt(myOffer.getText().toString());
                        int currentMaxOffer = Integer.parseInt(credentials.getMax_offer());
                        if (enteredOffer > currentMaxOffer) {
                            winnerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            maxOffer = myOffer.getText().toString();
                        } else
                            maxOffer = credentials.getMax_offer().toString();
                    } else {
                        winnerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        maxOffer = myOffer.getText().toString();
                    }

                    final AdCredentials newCredential = new AdCredentials(credentials.getParentID(),
                            credentials.getTitle(),
                            credentials.getPrice(),
                            credentials.getAdType(),
                            credentials.getAdDesSpec(),
                            credentials.getSellerId(),
                            maxOffer,
                            winnerId,
                            credentials.getCategory(),
                            credentials.getImageLinks(),
                            credentials.getEndDate(),
                            credentials.getBuyItNow(),
                            credentials.getIsActive()
                    );

//                    newCredential.setParentID(credentials.getParentID());

                    Log.d("parentId", "id: " + credentials.getParentID());

                    FirebaseDatabase.getInstance().getReference("Data of posted ads").child(credentials.getParentID())
                            .setValue(newCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("biding info");
                    Double offeredAmount = Double.parseDouble(myOffer.getText().toString());
                    OffersDetail offerIns = new OffersDetail(offeredAmount);

                    reference.child(credentials.getParentID()).child(FirebaseAuth.getInstance()
                            .getCurrentUser().getUid())
                            .setValue(offerIns)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(BidDetail.this, "Offer send", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(BidDetail.this, BidDetail.class);
                                    intent.putExtra("adInfo", newCredential);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                } else {
                    myOffer.setError("Sorry, this offer is not valid.");
                    myOffer.requestFocus();
                }
            }
        });

    }

    String contact;

    private String getContactOfBidder(final String bidder_id) {


        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("users");
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> usersList = dataSnapshot.getChildren();

                for (DataSnapshot user : usersList) {

                    if (user.getKey().equals(bidder_id)) {
                        User bidder = user.getValue(User.class);
                        Log.d("test", "onDataChange: " + bidder.getContact());

                        contact = bidder.getContact();

                    }

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return contact;

    }


    int temp = 0;
    Boolean status = true;

    private Boolean isOfferValid(AdCredentials credentials) {

        final int yourEnteredAmount = Integer.parseInt(myOffer.getText().toString());
        final int startPrice = Integer.parseInt(credentials.getPrice().toString());
        final int maxOfferExpected = Integer.parseInt(credentials.getBuyItNow());
        final Integer integer = Integer.valueOf(upto.intValue());
        if (!credentials.getMax_offer().toString().equals("dummy")) {
            int highest = Integer.parseInt(credentials.getMax_offer().toString());
        }
        final int highest = Integer.parseInt(credentials.getPrice().toString());


        //

//        while (temp != 1) {
//            checkLastOffer(credentials);
//        }
        if (yourEnteredAmount >= startPrice && yourEnteredAmount <= maxOfferExpected
                && yourEnteredAmount <= integer && yourEnteredAmount > highest
                && yourEnteredAmount > myLastOffer) {
            Log.d("test", "isOfferValid: in lower  if statement");

            return true;
        } else {
            return false;
        }
    }

    private int checkLastOffer(AdCredentials credentials) {

        Log.d("test", "checkLastOffer: check last offer");

            FirebaseDatabase.getInstance().getReference("biding info")
                    .child(credentials.getParentID()).addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Log.d("test", "onDataChange: on data change");
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();

//                    while (temp != 1) {
                        for (DataSnapshot item : children) {
                            Log.d("test", "onDataChange: in for");

                            if (item.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                Log.d("test", "onDataChange: " + FirebaseAuth.getInstance().getCurrentUser().getUid());
                                Log.d("test", "onDataChange: " + item.getKey());


                                OffersDetail offer = item.getValue(OffersDetail.class);
                                myLastOffer = (int) Math.round(offer.getAmount());
                                temp = 1;
                                Log.d("test", "onDataChange: " + myLastOffer);

                            }
                        }
                        temp = 1;
                    }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }

            });

        return myLastOffer;
    }


    private void setData(AdCredentials credentials) {

        tvCategory.setText(credentials.getCategory());

//        Glide.with(this).load(credentials.getDownloadAbleLink()).into(imageView);
        titletv.setText(credentials.getTitle());
        setImages(credentials);
        String startingPrice = "Released price: " + credentials.getPrice();
        releasedPrice.setText(startingPrice);
        setContact(credentials);
        desctv.setText(credentials.getAdDesSpec());

        if (credentials.getMax_offer().equals("dummy")) {

            upto = Double.parseDouble(credentials.getPrice().toString()) + tenPercent;
            if (upto > Double.parseDouble(credentials.getBuyItNow())) {
                upto = Double.parseDouble(credentials.getBuyItNow());
            }
            String str = "offer upto " + Integer.valueOf(upto.intValue());
            addUpTo.setText(str);

            highestOffer.setText("NO offer yet");

        } else if (!credentials.getMax_offer().equals("dummy")) {
            upto = Double.parseDouble(credentials.getMax_offer().toString()) + tenPercent;
            if (upto > Double.parseDouble(credentials.getBuyItNow())) {
                upto = Double.parseDouble(credentials.getBuyItNow());
            }
            String str = "offer upto " + Integer.valueOf(upto.intValue());
            addUpTo.setText(str);

            String offer = "highest Offer :" + credentials.getMax_offer();
            highestOffer.setText(offer);
        }
        String maxExpectedAmount = "Buy it Now : " + credentials.getBuyItNow();
        buyItNow.setText(maxExpectedAmount);

        isDateExpired(credentials.getEndDate());
        if (!isDateExpire.equals("expire")) {
            String endOn = "will end on " + credentials.getEndDate().toString();
            endDate.setText(endOn);
        } else {
            String endOn = "bidding time expire.";
            endDate.setText(endOn);
            offerLayout.setVisibility(View.INVISIBLE);
            addUpTo.setVisibility(View.INVISIBLE);
            sendOfferBtn.setVisibility(View.INVISIBLE);

        }
    }

    private void isDateExpired(String endDate) {

        Log.d("date", "isDateExpired:" + endDate);

        Calendar startDate = Calendar.getInstance();
        String date = DateFormat.getDateInstance(DateFormat.FULL).format(startDate.getTime());

        Log.d("testing", "isDateExpired: "+date.compareTo(endDate));

        if (isDateExpire.equals("expire")) {
            return;
        }
        //new code...
        else if (date.compareTo(endDate) == 0 || date.compareTo(endDate)<0) {
            Log.d("compare", "dates equal expired");
            isDateExpire = "expire";
        }
    }

    private void setContact(final AdCredentials credentials) {

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();


                for (DataSnapshot data : children) {
                    if (data.getKey().equals(credentials.getSellerId())) {
                        User user = data.getValue(User.class);
//                        String number = "Seller contact :" + user.getContact();
                        contacttv.setText(user.getContact().toString());
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void initialize() {

        tvCategory = findViewById(R.id.id_category_bid);
        offerLayout = findViewById(R.id.offer_option);
        sendOfferBtn = findViewById(R.id.id_sendOffer_btn);
        mref = FirebaseDatabase.getInstance().getReference("users");
        titletv = findViewById(R.id.id_title_bidDetail);
        endDate = findViewById(R.id.id_end_bidding_date);
        addUpTo = findViewById(R.id.id_add_upto);
        slider = findViewById(R.id.id_slider_bid_detail);
        contacttv = findViewById(R.id.id_seller_number);
        desctv = findViewById(R.id.id_bidDetail_desc);
        toolbar = findViewById(R.id.toolbar_bid_detail);
        setSupportActionBar(toolbar);
        releasedPrice = findViewById(R.id.id_released_price);
        myOffer = findViewById(R.id.id_myOfferEt);
        highestOffer = findViewById(R.id.id_highest_offer);
        buyItNow = findViewById(R.id.id_buy_it_now);
    }


    private void setImages(AdCredentials credentials) {

        List<SlideModel> slideModels = new ArrayList<>();

        for (int i = 0; i < credentials.getImageLinks().size(); i++) {
            slideModels.add(new SlideModel(credentials.getImageLinks().get(i)));
        }

        slider.setImageList(slideModels, true);
    }

    public void onBackPressed() {
        Intent intent = new Intent(BidDetail.this, Home.class);
        startActivity(intent);
        finish();
    }

}
