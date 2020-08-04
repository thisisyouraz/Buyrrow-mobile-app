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
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Admin_approve_ad extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    NavigationView navigationView;
    DrawerLayout mNavDrawer;
    Toolbar toolbar;

    RecyclerView mRecyclerView;
    GridLayoutManager gridLayoutManager;
    List<AdCredentials> myListCredential;
    Admin_approveAd_adapter approveAdapter;
    DatabaseReference mref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_admin_approve_ad);
        initialize();
        navIcon();

        getSupportActionBar().setTitle("Admin approval");

        Intent intent=getIntent();
        String callFrom=intent.getStringExtra("callFrom");
//        if(callFrom.equals("adminApprove")){
//            finish();
//        }



        navigationView.setNavigationItemSelectedListener(this);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        approveAdapter= new Admin_approveAd_adapter(Admin_approve_ad.this,myListCredential);

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot adId:children) {
                    AdCredentials credentialObject=adId.getValue(AdCredentials.class);
                    credentialObject.setParentID(adId.getKey());
                    myListCredential.add(credentialObject);
                }
                approveAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Admin_approve_ad.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerView.setAdapter(approveAdapter);

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
        mref= FirebaseDatabase.getInstance().getReference("Admin approve ads");
        mRecyclerView=findViewById(R.id.recyclerView_Admin_Approve);
        gridLayoutManager=new GridLayoutManager(this,1);
        myListCredential=new ArrayList<>();
        mNavDrawer = findViewById(R.id.drawer_layout_AdminApproveAd);
        navigationView = findViewById(R.id.navigation_view_admin_approveAd);
        toolbar = findViewById(R.id.toolbar_adminApproveAd);
        setSupportActionBar(toolbar);
        navigationView.getMenu().getItem(1).setChecked(true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.idApproveAds) {
            mNavDrawer.closeDrawer(GravityCompat.START);
        }
        else if (item.getItemId() == R.id.id_SignOut_admin) {
            Intent intent=new Intent(Admin_approve_ad.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else if(item.getItemId()==R.id.idHomeAdmin){

            Intent intent=new Intent(Admin_approve_ad.this,AdminHome.class);
            startActivity(intent);
            finish();
        }
        return true;
    }

    public void onBackPressed() {
        Intent intent=new Intent(Admin_approve_ad.this,AdminHome.class);
        startActivity(intent);
        finish();
    }

}
