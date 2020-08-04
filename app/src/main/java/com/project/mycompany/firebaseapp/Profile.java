package com.project.mycompany.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class Profile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    DrawerLayout mNavDrawer;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_nav_drawer);

        navigationView = findViewById(R.id.nav_view);
        mNavDrawer = findViewById(R.id.profile_nav_drawer);
        toolbar = findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);


        navigationView.getMenu().getItem(0).setChecked(true);

        navigationView.setNavigationItemSelectedListener(this);
        navIcon();
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.idPostAd) {
            Intent intent = new Intent(getApplicationContext(), Create_Ad.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.idAboutAs) {
            Intent intent = new Intent(getApplicationContext(), About_us.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.idMyAds) {
            Intent intent = new Intent(getApplicationContext(), My_ads.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.idHome) {
            Intent intent = new Intent(getApplicationContext(), Home.class);
            startActivity(intent);
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
