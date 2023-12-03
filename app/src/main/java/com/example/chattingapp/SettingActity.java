package com.example.chattingapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.chattingapp.Model.UserProfile;
import com.example.chattingapp.databinding.ActivitySettingActityBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class SettingActity extends AppCompatActivity {
    ActivitySettingActityBinding binding;
    FirebaseDatabase database;
    FirebaseStorage storage;
    FirebaseAuth firebaseAuth;
    int check=0;
    Uri imgUri;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySettingActityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dialog=new Dialog(this);
        firebaseAuth=FirebaseAuth.getInstance();
        storage=FirebaseStorage.getInstance();
        database=FirebaseDatabase.getInstance();

        //getting pic name from database of current user
        database.getReference("Users").child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserProfile userProfile=snapshot.getValue(UserProfile.class);
                Picasso.get().load(userProfile.getPROFILEIMAGE()).placeholder(R.drawable.blank_profile_pic).into(binding.settingImgFilterView);
                binding.settingUserNAme.setText(userProfile.getNAME());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    ///creating alert dialog box for sign out
        AlertDialog.Builder dialog=new AlertDialog.Builder(SettingActity.this);
        dialog.setTitle("Sign Out");
        dialog.setMessage("Are You Sure To Sign Out ?");
        dialog.setPositiveButton("Yes,SignOut", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                firebaseAuth.signOut();
                Toast.makeText(SettingActity.this, "Sign Out Sucessfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SettingActity.this,SplashScreenActivity.class));
                finishAffinity();
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(SettingActity.this, "Sign Out Canceled", Toast.LENGTH_SHORT).show();
            }
        });

        // making edit text visible on button click
        binding.settingBtnNameedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check==0){
                    binding.nameeditviewsetting.setText("");
                    binding.nameeditviewsetting.setVisibility(View.VISIBLE);
                    check=1;
                }else {
                    binding.nameeditviewsetting.setVisibility(View.GONE);
                    check=0;
                }

            }
        });


        //Change Image
        binding.settingImgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent opengallery_intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                open_gallery.launch(opengallery_intent);
            }
        });

        //updating Name and image in firebase
        binding.settingUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //updating Name

                if(!binding.nameeditviewsetting.getText().toString().trim().equals("")){
                    DialogVisible_meth(true);
                    database.getReference("Users").child(firebaseAuth.getUid()).child("name").setValue(binding.nameeditviewsetting.getText().toString());
                    binding.nameeditviewsetting.setText("");
                    binding.nameeditviewsetting.setVisibility(View.INVISIBLE);
                    check=0;
                    DialogVisible_meth(false);

                }
                //updating profile pic
                if(imgUri!=null){
                    DialogVisible_meth(true);
                    StorageReference reference=storage.getReference().child("ProfilePics").child(firebaseAuth.getUid());
                    reference.putFile(imgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        database.getReference().child("Users").child(firebaseAuth.getUid()).child("profileimage").setValue(uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                DialogVisible_meth(false);
                                                Toast.makeText(SettingActity.this, "Profile Updated", Toast.LENGTH_SHORT).show();

                                            }
                                        });

                                    }
                                });
                            }else {
                                Toast.makeText(SettingActity.this, "Failed", Toast.LENGTH_SHORT).show();
                                DialogVisible_meth(false);
                            }
                        }
                    });

                }





            }
        });




        //sign out
        binding.settingSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
    }
    ActivityResultLauncher<Intent> open_gallery=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getData()!=null){
                imgUri=result.getData().getData();
                binding.settingImgFilterView.setImageURI(imgUri);
            }


        }
    });

    private void  DialogVisible_meth(Boolean visible){

        dialog.setCancelable(false);
        dialog.setContentView(R.layout.progress_dialog);
        if(visible==true){
            dialog.show();
        }else {
            dialog.dismiss();
        }


    }
}