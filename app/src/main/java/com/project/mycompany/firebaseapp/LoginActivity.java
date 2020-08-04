package com.project.mycompany.firebaseapp;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEt,passwordEt;
    private Button signInbtn;
    private TextView signUpLink;
    private FirebaseAuth mAuth;

    RadioButton radioButtonBuyerSeller,radioButtonAdmin;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        getSupportActionBar().setTitle("Buyrrow login");

//        ActionBar bar = getActionBar();
//        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0000FF")));

        radioButtonBuyerSeller=findViewById(R.id.id_user_buyerSeller);
        radioButtonAdmin=findViewById(R.id.id_user_admin);
        emailEt=findViewById(R.id.emailSignIn);
        passwordEt=findViewById(R.id.passwordSignIn);
        signInbtn=findViewById(R.id.signIn);
        signUpLink=findViewById(R.id.signupTv);
        mAuth=FirebaseAuth.getInstance();

        signInbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (radioButtonBuyerSeller.isChecked()) {
                    String enteredEmail = emailEt.getText().toString();
                    String enteredPassword = passwordEt.getText().toString();

                    if (TextUtils.isEmpty(enteredEmail)) {
                        emailEt.setError("valid Email is required.");
                        emailEt.requestFocus();
                        return;
                    }
                    if (TextUtils.isEmpty(enteredPassword)) {
                        passwordEt.setError("password is required.");
                        passwordEt.requestFocus();
                        return;
                    }
                    signInWithEmailAndPassword(enteredEmail, enteredPassword);

                }
                else if (radioButtonAdmin.isChecked()){
                    String enteredEmail =emailEt.getText().toString();
                    String enteredPass=passwordEt.getText().toString();

                    final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setMessage("checking credentials...");
                    progressDialog.show();

                    if(!enteredEmail.equals("admin@gmail.com") ) {
                        emailEt.setError("Incorrect email");
                    }
                    else if(!enteredPass.equals("admin123")){
                        passwordEt.setError("Incorrect password");
                    }
                    else {

                        Intent intent = new Intent(LoginActivity.this, AdminHome.class);
                        intent.putExtra("email", enteredEmail);
                        startActivity(intent);
                        progressDialog.dismiss();
                        finish();
                    }
                        progressDialog.dismiss();
                }
            }
            private void signInWithEmailAndPassword(String enteredEmail, String enteredPassword) {
                final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("checking credentials...");
                progressDialog.show();

                mAuth.signInWithEmailAndPassword(enteredEmail,enteredPassword).
                        addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Intent intent=new Intent(LoginActivity.this,Home.class);
                                    startActivity(intent);

                                    progressDialog.dismiss();
                                    finish();
                                }
                                else{
                                    Toast.makeText(LoginActivity.this,"check email and password and try again",Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();

                                }
                            }
                        });
            }
        });

        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this, signUp.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser =FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser!=null){
            Intent intent=new Intent(LoginActivity.this,Home.class);
            startActivity(intent);

            finish();
        }
    }
}
