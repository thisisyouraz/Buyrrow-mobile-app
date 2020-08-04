package com.project.mycompany.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.wifi.hotspot2.pps.Credential;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    DrawerLayout mNavDrawer;
    Toolbar toolbar;

    private RecyclerView myRecyclerView;
    private GridLayoutManager gridLayoutManager;
    private List<AdCredentials> myCredentialList=new ArrayList<>();
    private ValueEventListener eventListener;
    private AdapterRecyclerViewHome homeAdapter;

    private DatabaseReference mref;
    Date currentTime = Calendar.getInstance().getTime();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer_home);

        initialize();
        navIcon();
        setUserCredentials();

        getSupportActionBar().setTitle("Home");
        navigationView.setNavigationItemSelectedListener(this);
        myRecyclerView.setLayoutManager(gridLayoutManager);

        final ProgressDialog progressDialog = new ProgressDialog(Home.this);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();

        eventListener = mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                /// myCredentialList.clear();
                final Iterable<DataSnapshot> children = dataSnapshot.getChildren();


                for (DataSnapshot credential : children) {
//                    Log.d("key", " " + credential.getKey());
                    AdCredentials value = credential.getValue(AdCredentials.class);

                    myCredentialList.add(value);
                }
                homeAdapter.notifyDataSetChanged();
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Home.this, "Error", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });
        myRecyclerView.setAdapter(homeAdapter);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);


        inflater.inflate(R.menu.search_item,menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search Ads");
        //  searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(final String newText) {
                ArrayList<AdCredentials> filterList = new ArrayList<>();
                for (AdCredentials item : myCredentialList) {
                    if (item.getTitle().toLowerCase().contains(newText.toLowerCase()) ||
                            item.getCategory().toLowerCase().contains(newText.toLowerCase()) ||
                            item.getAdType().toLowerCase().contains(newText.toLowerCase()))  {
                        filterList.add(item);
                    }
                }
                homeAdapter.filteredList(filterList);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.cat_automobile) {

            Query query = mref.orderByChild("category").equalTo("Automobile");
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    final Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    myCredentialList.clear();
                    for (DataSnapshot credential : children) {
                        AdCredentials value = credential.getValue(AdCredentials.class);
                        myCredentialList.add(value);
                    }
                    homeAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Home.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
            myRecyclerView.setAdapter(homeAdapter);
        }
        if (item.getItemId() == R.id.cat_mobile) {


            Query query = mref.orderByChild("category").equalTo("Mobile");
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    final Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    myCredentialList.clear();
                    for (DataSnapshot credential : children) {
                        AdCredentials value = credential.getValue(AdCredentials.class);
                        myCredentialList.add(value);
                    }
                    homeAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Home.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
            myRecyclerView.setAdapter(homeAdapter);


        }
        if (item.getItemId() == R.id.cat_electronics) {


            Query query = mref.orderByChild("category").equalTo("Electronics");
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    final Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    myCredentialList.clear();
                    for (DataSnapshot credential : children) {
                        AdCredentials value = credential.getValue(AdCredentials.class);
                        myCredentialList.add(value);
                    }
                    homeAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Home.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
            myRecyclerView.setAdapter(homeAdapter);

        }
        if (item.getItemId() == R.id.cat_property) {

            Query query = mref.orderByChild("category").equalTo("Property");
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    final Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    myCredentialList.clear();
                    for (DataSnapshot credential : children) {
                        AdCredentials value = credential.getValue(AdCredentials.class);
                        myCredentialList.add(value);
                    }
                    homeAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Home.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
            myRecyclerView.setAdapter(homeAdapter);

        }
        if (item.getItemId() == R.id.cat_stationary) {


            Query query = mref.orderByChild("category").equalTo("Stationary");
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    final Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    myCredentialList.clear();
                    for (DataSnapshot credential : children) {
                        AdCredentials value = credential.getValue(AdCredentials.class);
                        myCredentialList.add(value);
                    }
                    homeAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Home.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
            myRecyclerView.setAdapter(homeAdapter);
        }


        if (item.getItemId() == R.id.cat_all) {

            mref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    final Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    myCredentialList.clear();
                    for (DataSnapshot credential : children) {
                        AdCredentials value = credential.getValue(AdCredentials.class);
                        myCredentialList.add(value);
                    }
                    homeAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Home.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
            myRecyclerView.setAdapter(homeAdapter);


        }



        // new code
        if (item.getItemId() == R.id.price_5k){

            mref.addValueEventListener(new ValueEventListener(){
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    final Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    myCredentialList.clear();
                    for (DataSnapshot credential : children) {
                        AdCredentials value = credential.getValue(AdCredentials.class);
                        if (Integer.parseInt(value.getPrice())>= 0 && (Integer.parseInt(value.getPrice())) < 5000 ){
                            myCredentialList.add(value);
                        }
                    }
                    homeAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Home.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
            myRecyclerView.setAdapter(homeAdapter);
        }


        if (item.getItemId() == R.id.price_15k){

            mref.addValueEventListener(new ValueEventListener(){
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    final Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    myCredentialList.clear();
                    for (DataSnapshot credential : children) {
                        AdCredentials value = credential.getValue(AdCredentials.class);
                        if (Integer.parseInt(value.getPrice())>= 5000 && (Integer.parseInt(value.getPrice())) < 15000 ){
                            myCredentialList.add(value);
                        }
                    }
                    homeAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Home.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
            myRecyclerView.setAdapter(homeAdapter);
        }


        if (item.getItemId() == R.id.price_25k){

            mref.addValueEventListener(new ValueEventListener(){
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    final Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    myCredentialList.clear();
                    for (DataSnapshot credential : children) {
                        AdCredentials value = credential.getValue(AdCredentials.class);
                        if (Integer.parseInt(value.getPrice())>= 15000 && (Integer.parseInt(value.getPrice())) < 25000 ){
                            myCredentialList.add(value);
                        }
                    }
                    homeAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Home.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
            myRecyclerView.setAdapter(homeAdapter);
        }


        if (item.getItemId() == R.id.price_35k){

            mref.addValueEventListener(new ValueEventListener(){
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    final Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    myCredentialList.clear();
                    for (DataSnapshot credential : children) {
                        AdCredentials value = credential.getValue(AdCredentials.class);
                        if (Integer.parseInt(value.getPrice())>= 25000 && (Integer.parseInt(value.getPrice())) < 35000 ){
                            myCredentialList.add(value);
                        }
                    }
                    homeAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Home.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
            myRecyclerView.setAdapter(homeAdapter);
        }


        if (item.getItemId() == R.id.price_45k){

            mref.addValueEventListener(new ValueEventListener(){
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    final Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    myCredentialList.clear();
                    for (DataSnapshot credential : children) {
                        AdCredentials value = credential.getValue(AdCredentials.class);
                        if (Integer.parseInt(value.getPrice())>= 35000 && (Integer.parseInt(value.getPrice())) < 45000 ){
                            myCredentialList.add(value);
                        }
                    }
                    homeAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Home.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
            myRecyclerView.setAdapter(homeAdapter);
        }


        if (item.getItemId() == R.id.price_all){

            mref.addValueEventListener(new ValueEventListener(){
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    final Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    myCredentialList.clear();
                    for (DataSnapshot credential : children) {
                        AdCredentials value = credential.getValue(AdCredentials.class);
                        if (Integer.parseInt(value.getPrice())>= 45000 ){
                            myCredentialList.add(value);
                        }
                    }
                    homeAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Home.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
            myRecyclerView.setAdapter(homeAdapter);
        }


        if (item.getItemId() == R.id.sell_type){

            mref.addValueEventListener(new ValueEventListener(){
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    final Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    myCredentialList.clear();
                    for (DataSnapshot credential : children) {
                        AdCredentials value = credential.getValue(AdCredentials.class);
                        if (value.getAdType().equals("Sell")){
                            myCredentialList.add(value);
                        }
                    }
                    homeAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Home.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
            myRecyclerView.setAdapter(homeAdapter);
        }


        if (item.getItemId() == R.id.rent_type){

            mref.addValueEventListener(new ValueEventListener(){
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    final Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    myCredentialList.clear();
                    for (DataSnapshot credential : children) {
                        AdCredentials value = credential.getValue(AdCredentials.class);
                        if (value.getAdType().equals("Rent")){
                            myCredentialList.add(value);
                        }
                    }
                    homeAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Home.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
            myRecyclerView.setAdapter(homeAdapter);
        }


        if (item.getItemId() == R.id.bid_type){

            mref.addValueEventListener(new ValueEventListener(){
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    final Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    myCredentialList.clear();
                    for (DataSnapshot credential : children) {
                        AdCredentials value = credential.getValue(AdCredentials.class);
                        if (value.getAdType().equals("Bid")){
                            myCredentialList.add(value);
                        }
                    }
                    homeAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Home.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
            myRecyclerView.setAdapter(homeAdapter);
        }


        return super.onOptionsItemSelected(item);


//        return super.onOptionsItemSelected(item);
    }


    private void initialize() {
        mref = FirebaseDatabase.getInstance().getReference().child("Data of posted ads");
//        myCredentialList = new ArrayList<>();
        gridLayoutManager = new GridLayoutManager(Home.this, 1);
        myRecyclerView = findViewById(R.id.recyclerView_home);
        mNavDrawer = findViewById(R.id.drawer_layout_home);
        navigationView = findViewById(R.id.navigation_view);
        toolbar = findViewById(R.id.toolbar_home);
        homeAdapter = new AdapterRecyclerViewHome(Home.this, myCredentialList);
        setSupportActionBar(toolbar);
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.idPostAd){
            Intent intent = new Intent(getApplicationContext(), Create_Ad.class);
//            mNavDrawer.closeDrawer(GravityCompat.START);
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

//            intent.putExtra("callFrom","Home");

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
        if (mNavDrawer.isDrawerOpen(GravityCompat.START)) {
            mNavDrawer.closeDrawer(GravityCompat.START);
        } else

            super.onBackPressed();
    }

}
