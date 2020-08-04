package com.project.mycompany.firebaseapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class About_us extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
    }


    public void onBackPressed() {
        Intent intent=new Intent(About_us.this,Home.class);
        startActivity(intent);
        finish();
    }
}
