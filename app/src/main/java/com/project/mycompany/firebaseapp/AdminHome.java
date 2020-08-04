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
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    DrawerLayout mNavDrawer;
    Toolbar toolbar;

    private RecyclerView myRecyclerView;
    private GridLayoutManager gridLayoutManager;
    private List<AdCredentials> myCredentialList;
    private ValueEventListener eventListener;
    private AdapterRecyclerViewAdminHome adminHomeAdapter;
    private DatabaseReference mref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_drawer_home);
        initialize();
        navIcon();
        getSupportActionBar().setTitle("Admin Home");
        navigationView.setNavigationItemSelectedListener(this);
        myRecyclerView.setLayoutManager(gridLayoutManager);

        eventListener=mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                /// myCredentialList.clear();

                final Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot credential : children) {

                    Log.d("key", " "+credential.getKey());

                    AdCredentials value = credential.getValue(AdCredentials.class);
                    myCredentialList.add(value);
                }
                adminHomeAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(AdminHome.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        myRecyclerView.setAdapter(adminHomeAdapter);
    }

    public void navIcon() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mNavDrawer, toolbar, R.string.nav_app_bar_open_drawer_description,
                R.string.navigation_drawer_close
        );
        mNavDrawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initialize() {

        myRecyclerView=findViewById(R.id.recyclerView_AdminHome);
        gridLayoutManager = new GridLayoutManager(AdminHome.this, 1);
        myCredentialList=new ArrayList<>();
        adminHomeAdapter=new AdapterRecyclerViewAdminHome(AdminHome.this,myCredentialList);
        mref = FirebaseDatabase.getInstance().getReference().child("Data of posted ads");
        mNavDrawer = findViewById(R.id.drawer_layout_AdminHome);
        navigationView = findViewById(R.id.navigation_view_adminHome);
        toolbar = findViewById(R.id.toolbar_adminHome);
        setSupportActionBar(toolbar);
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.idApproveAds){
            Intent intent=new Intent(AdminHome.this,Admin_approve_ad.class);
            intent.putExtra("callFrom","adminHome");
            startActivity(intent);
            finish();

        } else if (item.getItemId() == R.id.id_SignOut_admin) {
            Intent intent=new Intent(AdminHome.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else if(item.getItemId()==R.id.idHomeAdmin){
            mNavDrawer.closeDrawer(GravityCompat.START);
        }

        return true;
    }
    public void onBackPressed() {
        if (mNavDrawer.isDrawerOpen(GravityCompat.START)) {
            mNavDrawer.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }


}
