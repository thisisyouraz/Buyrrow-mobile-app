package com.project.mycompany.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class My_ads extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout mNavDrawer;
    Toolbar toolbar;
    NavigationView navigationView;

    private RecyclerView myRecyclerView;
    private GridLayoutManager gridLayoutManager;
    private List<AdCredentials> myCredentialList;
    private ValueEventListener eventListener;
    private My_ad_view_Adapter myAdsAdapter;
    private DatabaseReference mref;
    private String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer_my_ads);
        initiate();
        getSupportActionBar().setTitle("My Ads");
        navIcon();
        navigationView.getMenu().getItem(2).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);
        setUserCredentials();

        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query query = mref.orderByChild("sellerId").equalTo(currentUser);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myCredentialList.clear();

                final Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot credential : children) {

                    Log.d("key", " " + credential.getKey());
                    AdCredentials value = credential.getValue(AdCredentials.class);

                    myCredentialList.add(value);
                }
                myAdsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        myRecyclerView.setAdapter(myAdsAdapter);

    }

    private void initiate() {
        mref = FirebaseDatabase.getInstance().getReference("Data of posted ads");
        myRecyclerView = findViewById(R.id.recyclerView_MyAds);
        myCredentialList = new ArrayList<>();
        myAdsAdapter = new My_ad_view_Adapter(My_ads.this, myCredentialList);
        gridLayoutManager = new GridLayoutManager(My_ads.this, 1);
        myRecyclerView.setLayoutManager(gridLayoutManager);
        mNavDrawer = findViewById(R.id.my_ads_drawerLayout);
        toolbar = findViewById(R.id.toolbar_my_ads);
        navigationView = findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);
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
        Intent intent = new Intent(My_ads.this, Home.class);
        startActivity(intent);
        finish();
    }


    private void setUserCredentials() {


        FirebaseAuth currentUser = FirebaseAuth.getInstance();

        NavigationView navigationView = findViewById(R.id.nav_view);
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
