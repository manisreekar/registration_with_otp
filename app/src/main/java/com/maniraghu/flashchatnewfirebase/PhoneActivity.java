package com.maniraghu.flashchatnewfirebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneActivity extends AppCompatActivity {

    private AutoCompleteTextView mPhoneView;
    private AutoCompleteTextView mCodeView;
    Button codeVerify,codeLogin;
    FirebaseAuth mAuth;
    String codeSent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        mAuth=FirebaseAuth.getInstance();
        mPhoneView = (AutoCompleteTextView) findViewById(R.id.phno);
        mCodeView = (AutoCompleteTextView) findViewById(R.id.code);
        codeVerify=(Button)findViewById(R.id.codebutton);
        codeLogin=(Button)findViewById(R.id.codelogin);

        codeVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerificationCode();
            }
        });

        codeLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifySignInCode();
            }
        });
    }

    private void verifySignInCode(){
        String code=mCodeView.getText().toString();

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Logging in.....",Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(PhoneActivity.this,MainActivity.class);
                            startActivity(intent);
                        } else {

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(getApplicationContext(),"The verification code entered was invalid",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }


    private void sendVerificationCode(){
        String phone=mPhoneView.getText().toString();

        if(phone.isEmpty()){
            mPhoneView.setError("Phone number is required");
            mPhoneView.requestFocus();
            return;
        }
        if(phone.length()!=10){
            mPhoneView.setError("Please check your phone number");
            mPhoneView.requestFocus();
            return;
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeSent=s;
        }
    };
}
