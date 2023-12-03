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

import com.example.chattingapp.Model.GroupModel;
import com.example.chattingapp.databinding.ActivityGroupNameBinding;
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

import java.util.ArrayList;
import java.util.Date;

public class Group_Name_Activity extends AppCompatActivity {
    ActivityGroupNameBinding binding;
    FirebaseDatabase database;
    FirebaseStorage storage;
    FirebaseAuth firebaseAuth;
    Dialog dialog;
    String groupName,key;
    Uri imgUri;
    ArrayList<String > groupmemberModelArrayList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityGroupNameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dialog=new Dialog(this);
        dialog.setContentView(R.layout.progress_dialog);
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        groupmemberModelArrayList=new ArrayList<>();



        binding.imgGroupAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imgIntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                showImg.launch(imgIntent);
            }
        });

        binding.btnCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                DialogVisible_meth(true);

                //showing error on empty name
                groupName=binding.edtGroupName.getText().toString();
                if(groupName.trim().isEmpty()){
                    binding.edtGroupName.setError("Name Not Valid");
                    DialogVisible_meth(false);
                }else{



                //adding image to storage
                    if(imgUri!=null) {
                        DialogVisible_meth(true);
                        GroupModel groupModel=new GroupModel();
                        key=database.getReference().child("Groups").push().getKey();


                        groupModel.setGroupName(groupName);
                        groupModel.setCreateTime(String.valueOf(new Date().getTime()));
                        groupModel.setGroupUID(key);
                        groupmemberModelArrayList.add(firebaseAuth.getUid());
                        groupModel.setGroupMemebersList(groupmemberModelArrayList);

                        StorageReference reference=storage.getReference().child("GroupProfileImage").child(key);
                        reference.putFile(imgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if ((task.isSuccessful())){
                                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            groupModel.setGroupImg(String.valueOf(uri));
                                            database.getReference().child("Groups").child(key).setValue(groupModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    database.getReference().child("Users").child(firebaseAuth.getUid()).child("Joined_Groups").push().setValue(key);
                                                    DialogVisible_meth(false);
                                                    Toast.makeText(Group_Name_Activity.this, "Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                                                    Intent intent=new Intent(Group_Name_Activity.this,MainActivity.class);
                                                    startActivity(intent);
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
                    GroupModel groupModel=new GroupModel();
                    key=database.getReference().child("Groups").push().getKey();
                    groupModel.setGroupName(groupName);
                    groupModel.setCreateTime(String.valueOf(new Date().getTime()));
                    groupModel.setGroupUID(key);
                    groupmemberModelArrayList.add(firebaseAuth.getUid());
                    groupModel.setGroupMemebersList(groupmemberModelArrayList);


                    database.getReference().child("Groups").child(key).setValue(groupModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            database.getReference().child("Users").child(firebaseAuth.getUid()).child("Joined_Groups").child(key).setValue(key);
                            DialogVisible_meth(false);
                            Toast.makeText(Group_Name_Activity.this, "Group Created Sucessfully", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(Group_Name_Activity.this,MainActivity.class);
                            startActivity(intent);
                            finishAffinity();

                        }
                    });





                }

            }}
        });

            }












    ActivityResultLauncher<Intent>showImg=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode()==RESULT_OK){
                if (result.getData()!=null){
                    imgUri=result.getData().getData();
                    binding.imgGroupCreateImageView.setImageURI(imgUri);
                }else
                    Toast.makeText(Group_Name_Activity.this, "Error Occured", Toast.LENGTH_SHORT).show();

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