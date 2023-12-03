package com.example.chattingapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.chattingapp.Model.UserProfile;
import com.example.chattingapp.R;
import com.example.chattingapp.databinding.ActivityNameBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class NameActivity extends AppCompatActivity {
    ActivityNameBinding binding;
    Uri imgUri;
    String name;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage storage;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityNameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        dialog=new Dialog(this) ;

        binding.imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent img_picker_intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                showPicture.launch(img_picker_intent);

            }
        });

        //on click of create profile button
        binding.btnCreateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogVisible_meth(true);

                //showing error on empty name
                name=binding.edtName.getText().toString();
                if(name.trim().isEmpty()){
                    binding.edtName.setError("Name Not Valid");
                    DialogVisible_meth(false);
                }else{

                //addig image to storage
                if(imgUri!=null) {

                    StorageReference reference = storage.getReference().child("ProfilePics").child(firebaseAuth.getUid());
                    reference.putFile(imgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        //adding to firebase database
                                        UserProfile user = new UserProfile();
                                        user.setNAME(name);
                                        user.setUID(firebaseAuth.getUid());
                                        user.setPROFILEIMAGE(String.valueOf(uri));
                                        user.setPHONENUMBER(firebaseAuth.getCurrentUser().getPhoneNumber());

                                        firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                DialogVisible_meth(false);
                                                startActivity(new Intent(NameActivity.this, MainActivity.class));
                                                finishAffinity();
                                            }
                                        });
                                    }
                                });


                            }

                        }


                    });


                }else{

                    DialogVisible_meth(true);

                    //adding to firebase database
                    UserProfile user = new UserProfile();
                    user.setNAME(name);
                    user.setUID(firebaseAuth.getUid());
                    user.setPROFILEIMAGE("No Image");
                    user.setPHONENUMBER(firebaseAuth.getCurrentUser().getPhoneNumber());

                    firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            DialogVisible_meth(false);
                            startActivity(new Intent(NameActivity.this, MainActivity.class));
                            finishAffinity();
                        }
                    });




                }

            }}
        });


    }
    ActivityResultLauncher<Intent> showPicture=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode()==RESULT_OK){
                if (result.getData()!=null){
                     imgUri=result.getData().getData();
                    binding.imgFilterView.setImageURI(imgUri);
                }else
                    Toast.makeText(NameActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();

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