package com.example.chattingapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.chattingapp.Adapter.Chat_message_recyclesAdapter;
import com.example.chattingapp.Model.GroupModel;
import com.example.chattingapp.Model.MessageModel;
import com.example.chattingapp.databinding.ActivityGroupChatDetailAcctivityBinding;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class group_chat_detail_acctivity extends AppCompatActivity {
    ActivityGroupChatDetailAcctivityBinding binding;
    String groupName,groupImg,GroupId;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseStorage firebaseStorage;
    ArrayList<MessageModel> groupMessages;
    Chat_message_recyclesAdapter chatMessageRecyclesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityGroupChatDetailAcctivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent getdataIntent=getIntent();
        groupMessages=new ArrayList<>();
        firebaseStorage=FirebaseStorage.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        chatMessageRecyclesAdapter=new Chat_message_recyclesAdapter(this,groupMessages);
        binding.chatDetailGroupRecycleView.setAdapter(chatMessageRecyclesAdapter);
        groupName=getdataIntent.getStringExtra("GroupName");
        groupImg=getdataIntent.getStringExtra("GroupImg");
        GroupId=getdataIntent.getStringExtra("GroupId");
        binding.nameGroupChatDetailText.setText(groupName);
        Picasso.get().load(groupImg).placeholder(R.drawable.blank_profile_pic).into(binding.picGroupChatDetailImg);
        binding.backArrowGroupChatDetailImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(group_chat_detail_acctivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        firebaseDatabase.getReference().child("Groups").child(GroupId).child("Messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupMessages.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);
                    groupMessages.add(messageModel);
                    chatMessageRecyclesAdapter.notifyDataSetChanged();



                }
                binding.chatDetailGroupRecycleView.scrollToPosition(groupMessages.size()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(group_chat_detail_acctivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        binding.imgPlusGroupAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent addUserintent=new Intent(group_chat_detail_acctivity.this,SelectPeopleForGroup.class);
                addUserintent.putExtra("GroupId",GroupId);
                startActivity(addUserintent);

            }
        });


        //send message on click
        binding.imageViewsendGroupChatdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date();
                String messageTxt = binding.edtMsgGroupChatdetail.getText().toString().trim();
                if (messageTxt.trim().isEmpty()) {


                } else {

                    MessageModel message = new MessageModel(firebaseAuth.getUid(), messageTxt, date.getTime());
                    binding.edtMsgGroupChatdetail.setText("");
                    firebaseDatabase.getReference().child("Groups").child(GroupId)
                            .child("Messages").push().addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String keyId=snapshot.getKey();
                                    message.setMessageNodeId(keyId);
                                    firebaseDatabase.getReference().child("Groups").child(GroupId)
                                            .child("Messages").child(keyId).setValue(message);


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                }


            }
        });



        ActivityResultLauncher<Intent>opengallery=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode()==RESULT_OK){
                    if(result.getData()!=null){

                        ClipData clipData=result.getData().getClipData();
                        int imgcount =clipData.getItemCount();
                        for (int i=0;i<imgcount;i++){
                            uploadImageToStorage(clipData.getItemAt(i).getUri());

                        }




                    }
                }

            }
        });
        //link image Button
        binding.imgLinkGroupChatDeatail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                opengallery.launch(intent);
            }
        });








        ActivityResultLauncher<Intent> camerapic = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (result.getData() != null) {

                        try {
                            Bitmap captureImage = (Bitmap) result.getData().getExtras().get("data");

                            // Save the captured image to a temporary file
                            File imageFile = createImageFile();
                            if (imageFile != null) {
                                FileOutputStream outputStream = new FileOutputStream(imageFile);
                                captureImage.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                                outputStream.close();

                                // Get the URI of the saved image file
                                Uri imgUri = FileProvider.getUriForFile(group_chat_detail_acctivity.this, "com.example.chattingapp.provider", imageFile);

                                // Now, you can upload or use 'imgUri' as needed
                                uploadImageToStorage(imgUri);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }


            }
        });



        binding.imgCamGroupChatDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent camintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                camerapic.launch(camintent);

            }
        });






    }
    private File createImageFile() throws IOException {
        String imageFileName = "captured_image";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        return imageFile;
    }
    private void uploadImageToStorage(Uri imgUri) {
        Date date=new Date();
        StorageReference reference = firebaseStorage.getReference().child("Sendpic").child(String.valueOf(date.getTime()));
        reference.putFile(imgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //Updating database for image
                            MessageModel message=new MessageModel(firebaseAuth.getUid(),"",uri.toString(),date.getTime());
                            firebaseDatabase.getReference().child("Groups").child(GroupId)
                                    .child("Messages").push().addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String keyId=snapshot.getKey();
                                            message.setMessageNodeId(keyId);
                                            firebaseDatabase.getReference().child("Groups").child(GroupId)
                                                    .child("Messages").child(keyId).setValue(message);


                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
















                        }
                    });
                } else {
                    Toast.makeText(group_chat_detail_acctivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}