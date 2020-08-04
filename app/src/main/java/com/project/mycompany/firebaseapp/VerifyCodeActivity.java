package com.project.mycompany.firebaseapp;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyCodeActivity extends AppCompatActivity {


    private String verificationId;
    private EditText enteredCode;
    private Button signup;
    private ProgressBar progress;
    private FirebaseAuth mAuth;
    private String code;
    private TextView autoTv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.code_verification_layout);
        enteredCode = findViewById(R.id.code);
        signup = findViewById(R.id.signupBtn);
        progress = findViewById(R.id.progressBar);
        mAuth=FirebaseAuth.getInstance();
        autoTv=findViewById(R.id.autoDetetiontv);

        String phoneNumber = getIntent().getStringExtra("phoneNumber");
        sendCode(phoneNumber);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enteredCode == null) {
                    enteredCode.setError("please enter the code.");
                    enteredCode.requestFocus();
                }
                progress.setVisibility(View.VISIBLE);
                autoTv.setText("auto detecting code...");
                verifyCode(enteredCode.getText().toString().trim());
            }
        });
    }


    private void sendCode(String phoneNumber) {


   //     progress.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;

            Log.d("code sent", "onCodeSent: "+s);

            enteredCode.setVisibility(View.VISIBLE);
            progress.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            code = phoneAuthCredential.getSmsCode();

            Log.d("code", "onVerificationCompleted: "+code);
            if (code != null) {
                progress.setVisibility(View.VISIBLE);
                autoTv.setText("detecting code...");
                enteredCode.setText(code);
                verifyCode(enteredCode.getText().toString().trim());
            }

            // in case of instant verification.
            if(verificationId== null){

                Toast.makeText(VerifyCodeActivity.this, "account created", Toast.LENGTH_LONG).show();

                return;
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(VerifyCodeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
        }

    private void signInWithCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progress.setVisibility(View.INVISIBLE);
                            autoTv.setText("");

                            Toast.makeText(VerifyCodeActivity.this, "account created", Toast.LENGTH_LONG).show();
                        }
                        else{
                            enteredCode.setError("incorrect code");
                            enteredCode.requestFocus();
                           progress.setVisibility(View.INVISIBLE);
                           autoTv.setText("");
                        }
                    }
                });
    }

}
