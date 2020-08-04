package com.project.mycompany.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signUp extends AppCompatActivity{

    private String  emailDuplicationError= "The email address is already in use by another account";
    private TextView loginLink;
    private EditText phnNumber;
    private Spinner spinner;
    private Button btnContinue;
    private EditText email;
    private EditText password;
    private EditText confirmPass;
    private String phoneNumber;
    private FirebaseAuth mAuth;
    DatabaseReference mref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Buyrrow signup");


        mAuth=FirebaseAuth.getInstance();

        email = findViewById(R.id.emailEt);
        password = findViewById(R.id.passwordEt);
        confirmPass = findViewById(R.id.confirmPasswordEt);
        phnNumber = findViewById(R.id.number);
        spinner = findViewById(R.id.countryCode);
        btnContinue = findViewById(R.id.btnContinue);
        mref=FirebaseDatabase.getInstance().getReference("users");
        loginLink=findViewById(R.id.loginTv);

        spinner.setAdapter(new ArrayAdapter<String>
                (this, R.layout.support_simple_spinner_dropdown_item, PhoneNumberCodes.Codes));


        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(signUp.this,LoginActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String enteredEmail = email.getText().toString();
                if (!(isEmailValid(enteredEmail))) {
                    email.setError("valid email is required.");
                    email.requestFocus();
                    return;
                }
                String number = phnNumber.getText().toString().trim();
               // String selectedCountryCode = CountryData.countryCodes[spinner.getSelectedItemPosition()];
                phoneNumber = " " +spinner.getSelectedItem().toString() + number;

                if (!isValidPhoneNumber(phoneNumber)) {
                    phnNumber.setError("valid number is required.");
                    phnNumber.requestFocus();
                    return;
                }
                String enteredPass = password.getText().toString();
                if (enteredPass.length() < 6) {
                    password.setError("min 6 characters requires");
                    password.requestFocus();
                    return;
                }
                String enteredConfirmPass = confirmPass.getText().toString();
                if (!isPassConfirmed(enteredPass, enteredConfirmPass)) {
                    confirmPass.setError("password miss match");
                    confirmPass.requestFocus();
                    return;
                }


               createUser(enteredEmail,enteredPass);
                return;

            }
        });
    }

    private void createUser(final String enteredEmail, final String enteredPass) {
        mAuth.createUserWithEmailAndPassword(enteredEmail, enteredPass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                           String id= FirebaseAuth.getInstance().getCurrentUser().getUid();
                           
                           User user=new User(id,enteredEmail,phoneNumber,enteredPass);
                           mref.child(id).setValue(user);
                           Toast.makeText(signUp.this, "Account created", Toast.LENGTH_SHORT).show();

                           Intent intent=new Intent(signUp.this,Home.class);
                           startActivity(intent);
                           finish();

                        }
                        else
                        {
                            Exception exp=task.getException();

                            if(exp.getMessage().equals("The email address is already in use by another account.")){
                                email.setError("Email already exits.");
                                email.requestFocus();
                                return;
                            }
                            else
                            Toast.makeText(signUp.this,exp.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private Boolean isPassConfirmed(String password, String confirmPass) {
        if (password.equals(confirmPass)) {
            return true;
        } else {
            return false;
        }
    }


    private Boolean isEmailValid(String email) {
        if (email.isEmpty()) {
            return false;
        }
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public final static boolean isValidPhoneNumber(String target) {
        if (target == null || target.length() !=12) {

            Log.d("phone number", "isValidPhoneNumber: "+target);
            Log.d("length", "isValidPhoneNumber: "+target.length());
            return false;
        } else {
            return true;
          // return android.util.Patterns.PHONE.matcher(target).matches();
        }
    }

}
