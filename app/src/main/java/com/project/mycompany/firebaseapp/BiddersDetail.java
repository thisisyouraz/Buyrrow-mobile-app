package com.project.mycompany.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BiddersDetail extends AppCompatActivity {



    private RecyclerView myRecyclerView;
    private GridLayoutManager gridLayoutManager;
    private List<OffersDetail> myOfferList=new ArrayList<>();
    private AdapterRecyclerViewBidDetail bidDetailAdapter;
    private DatabaseReference mref;
    private int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidders_detail);
        getSupportActionBar().setTitle("Bidder's detail");

        String adId =getIntent().getStringExtra("adId");

        myRecyclerView=findViewById(R.id.recyclerViewMyAdsBid);
        gridLayoutManager=new GridLayoutManager(BiddersDetail.this,1);
        bidDetailAdapter=new AdapterRecyclerViewBidDetail(BiddersDetail.this,myOfferList);
        myRecyclerView.setLayoutManager(gridLayoutManager);
        mref= FirebaseDatabase.getInstance().getReference("biding info");

        mref.child(adId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot item:children) {
                    Log.d("test", "onDataChange1: "+item.getKey());
                    OffersDetail offer=item.getValue(OffersDetail.class);
                    setContact(item.getKey(),offer);

                    myOfferList.add(offer);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(BiddersDetail.this, "there is some issue , please try again.", Toast.LENGTH_SHORT).show();
            }
        });
        myRecyclerView.setAdapter(bidDetailAdapter);

    }

    private void setContact(final String key, final OffersDetail offer) {

        FirebaseDatabase.getInstance().getReference("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();


                for (DataSnapshot data : children) {
                    if (data.getKey().equals(key)) {
                        i++;
                        User user = data.getValue(User.class);
                        offer.setContact(user.getContact());
                        Log.d("test", "onDataChange1: "+offer.getContact());
                        if(myOfferList.size()==i){
                            Log.d("test", "notify change");
                            bidDetailAdapter.notifyDataSetChanged();
                        }

                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }
}
