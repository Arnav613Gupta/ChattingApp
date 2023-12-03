package com.example.chattingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.chattingapp.databinding.ActivityPhoneSignupBinding;
import com.google.firebase.auth.FirebaseAuth;

public class phone_signup_activity extends AppCompatActivity {
ActivityPhoneSignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPhoneSignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //auto LogIn
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            startActivity(new Intent(phone_signup_activity.this, MainActivity.class));
            finishAffinity();
        }

        //for getting keyBoard automatically
        binding.edtPhoneNo.requestFocus();


        //on click of continue button
        binding.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phone_num=binding.edtPhoneNo.getText().toString();
                if(phone_num.length()>9){
                    //changing Sign in activity to otp activity
                    Intent signup_to_otp=new Intent(phone_signup_activity.this, OtpVerificationActivity.class);
                    signup_to_otp.putExtra("phone_num",phone_num);
                    startActivity(signup_to_otp);
                }else{
                    //showing error for wrong number
                    binding.edtPhoneNo.setError("Enter Valid number");
                }


            }
        });

    }



}