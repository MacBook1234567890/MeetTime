package com.example.meettime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class RegisterationActivity extends AppCompatActivity {

    private EditText phoneText;
    private EditText codeText;
    private Button continueAndNextBtn;
    private String checker = "", phoneNumber = "";
    private RelativeLayout relativeLayout;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);


        mAuth=FirebaseAuth.getInstance();
        loadingBar=new ProgressDialog(this);



        phoneText = findViewById(R.id.phoneText);
        codeText = findViewById(R.id.codeText);
        continueAndNextBtn = findViewById(R.id.continueNextButton);
        relativeLayout = findViewById(R.id.phoneAuth);



        continueAndNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (continueAndNextBtn.getText().equals("Submit") || checker.equals("Code Sent")) {

                        String verificationCode =codeText.getText().toString();

                    if (verificationCode.equals("")){
                            Toast.makeText(RegisterationActivity.this,"Please enter the code",Toast.LENGTH_SHORT).show();

                        }
                        else {
                            loadingBar.setTitle("Code Verfication. . .");
                            loadingBar.setMessage("Please wait, we are verifying your code.");
                            loadingBar.setCanceledOnTouchOutside(false);
                            loadingBar.show();
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);
                            signInWithPhoneAuthCredential(credential);
                        }
                } else {
                    phoneNumber = phoneText.getText().toString(); // Get phone number directly from the EditText
                    if (!phoneNumber.equals("")) {
                        loadingBar.setTitle("Phone Number Verfication");
                        loadingBar.setMessage("Please wait, we are verifying your phone number.");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();
                        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                                .setPhoneNumber(phoneNumber)  // Your phone number here
                                .setTimeout(60L, TimeUnit.SECONDS)
                                .setActivity(RegisterationActivity.this)
                                .setCallbacks(mCallbacks)
                                .build();
                        PhoneAuthProvider.verifyPhoneNumber(options);


                    } else {
                        Toast.makeText(RegisterationActivity.this, "Please write valid phone number.",Toast.LENGTH_SHORT ).show();
                    }

                }
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(RegisterationActivity.this,"Invalid Phone Number...",Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
                relativeLayout.setVisibility(View.VISIBLE);
                continueAndNextBtn.setText("Continue");
                codeText.setVisibility(View.GONE);

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                mVerificationId=s;
                mResendToken   =forceResendingToken;
                relativeLayout.setVisibility(View.GONE);
                checker ="Code Sent";
                continueAndNextBtn.setText("Submit");
                codeText.setVisibility(View.VISIBLE);
                loadingBar.dismiss();
                Toast.makeText(RegisterationActivity.this,"Code has been sent",Toast.LENGTH_SHORT).show();
            }
        };
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser!=null){
            Intent homeIntent=new Intent(RegisterationActivity.this, ContactsActivity.class);
            startActivity(homeIntent);
            finish();
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loadingBar.dismiss();
                            Toast.makeText(RegisterationActivity.this,"Congratulations ",Toast.LENGTH_SHORT).show();
                            sendUserToMainActivity();
                        } else {
                            loadingBar.dismiss();
                            String e=task.getException().toString();
                        Toast.makeText(RegisterationActivity.this,"Error:"+e,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void sendUserToMainActivity(){
        Intent intent=new Intent(RegisterationActivity.this, ContactsActivity.class);
        startActivity(intent);
        finish();

    }
}