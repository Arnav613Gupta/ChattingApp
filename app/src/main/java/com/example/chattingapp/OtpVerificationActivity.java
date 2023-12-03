package com.example.chattingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.chattingapp.databinding.ActivityOtpVerificationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class OtpVerificationActivity extends AppCompatActivity {
    ActivityOtpVerificationBinding binding;
    String phone_num,verificationCode;
    PhoneAuthProvider.ForceResendingToken resendToken;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

    String country_code="+91";

    Long timeOut=60L;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        getphone_num_setScreen();
        sendOtp(phone_num,false);
        //On click of Verify button
        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationCode,binding.edtOtp.getText().toString());
                signIn(credential);
            }
        });

        //on click of resend button
        binding.btnSendAgainOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.progressBarSignup.setVisibility(View.VISIBLE);
                sendOtp(phone_num,true);
            }
        });







    }
    //sending otp
    private void sendOtp(String  phone_num,Boolean isResend){

        startResendTimer();

        PhoneAuthOptions.Builder options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(country_code+phone_num)
                        .setTimeout(timeOut, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signIn(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(OtpVerificationActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                //changing Activity on failure
                                startActivity(new Intent(OtpVerificationActivity.this, phone_signup_activity.class));
                                finish();
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                Toast.makeText(OtpVerificationActivity.this, "OTP Send sucesslully", Toast.LENGTH_SHORT).show();
                                binding.edtOtp.requestFocus();//for opening keyboard automatically
                                verificationCode=s;
                                resendToken=forceResendingToken;
                                //making otp visible
                                binding.llyoutOtp.setVisibility(View.VISIBLE);
                                binding.progressBarSignup.setVisibility(View.INVISIBLE);
                                binding.txtSendingOtp.setText(R.string.otp_send);
                            }
                        });
        if(isResend){
            PhoneAuthProvider.verifyPhoneNumber(options.setForceResendingToken(resendToken).build());
        }else{
            PhoneAuthProvider.verifyPhoneNumber(options.build());
        }


    }




    


    //getting phoneNumber from signup
    private void getphone_num_setScreen(){
        Intent getNum=getIntent();
        phone_num=getNum.getStringExtra("phone_num");
        binding.txtPhoneNum.setText(country_code+" "+phone_num);

    }
    //check number and sign up to firebase
    private void   signIn(PhoneAuthCredential phoneAuthCredential){
        firebaseAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(OtpVerificationActivity.this, "Sucessfully Signed In", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(OtpVerificationActivity.this, NameActivity.class));
                    finishAffinity();
                }else{
                    Toast.makeText(OtpVerificationActivity.this, "Sign In Failes", Toast.LENGTH_SHORT).show();






                    //changing Activity on failure
                    startActivity(new Intent(OtpVerificationActivity.this, phone_signup_activity.class));
                    finish();
                }
            }

        });
    }
    //setting resend timer
    private void startResendTimer(){

        binding.btnSendAgainOtp.setEnabled(false);
        Timer timer=new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeOut--;
                binding.btnSendAgainOtp.setText("Resend OTP in "+timeOut+"seconds");
                if(timeOut<=0){
                    timeOut=60L;
                    timer.cancel();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.btnSendAgainOtp.setEnabled(true);
                            binding.btnSendAgainOtp.setText("Resend Otp");
                        }
                    });

                }

            }
        },0,1000);
    }


}