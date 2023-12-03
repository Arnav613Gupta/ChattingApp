package com.example.chattingapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.chattingapp.Adapter.Chat_message_recyclesAdapter;
import com.example.chattingapp.Model.MessageModel;
import com.example.chattingapp.databinding.ActivityChatDetailBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
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
import java.util.HashSet;

public class Chat_detail_Activity extends AppCompatActivity  {
    ActivityChatDetailBinding binding;
    ArrayList<MessageModel> messagelist;
    HashSet<String> deleteMessagelist;


    Chat_message_recyclesAdapter chatMessageRecyclesAdapter;

    String receiverName, receiverId, receiverPic;
    String senderRoom, receiverRoom;
    String senderId;
    FirebaseDatabase database;
    FirebaseStorage storage;
    FirebaseAuth firebaseAuth;
    Toolbar deleteToolbar,normalToolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        deleteToolbar=binding.chatDetailToolbarChatDelete;
        normalToolbar=binding.chatDetailToolbar;
        deleteMessagelist=new HashSet<>();

        messagelist = new ArrayList<>();

        chatMessageRecyclesAdapter = new Chat_message_recyclesAdapter(this, messagelist);


        senderId = FirebaseAuth.getInstance().getUid();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();


        init();
        binding.chatDetailRecycleView.setAdapter(chatMessageRecyclesAdapter);
        senderRoom = senderId + receiverId;
        receiverRoom = receiverId + senderId;


        //showing messages
        database.getReference().child("Chats").child(senderRoom).child("Messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagelist.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);
                    messagelist.add(messageModel);


                }
                chatMessageRecyclesAdapter.notifyDataSetChanged();
                binding.chatDetailRecycleView.scrollToPosition(messagelist.size()-1);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Chat_detail_Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });









        //clear chat menu
        binding.imageViewClearChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenuMeth(view);
            }
        });
























        //send message on click
        binding.imageViewsendChatdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date();
                String messageTxt = binding.edtMsgChatdetail.getText().toString().trim();
                if (messageTxt.trim().isEmpty()) {


                } else {

                    MessageModel message = new MessageModel(senderId, messageTxt, date.getTime());
                    binding.edtMsgChatdetail.setText("");

                    database.getReference().child("Chats").child(senderRoom)
                            .child("Messages").push().addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    String messageKeyId=snapshot.getKey();
                                    message.setMessageNodeId(messageKeyId);
                                    database.getReference().child("Chats").child(senderRoom)
                                            .child("Messages").child(messageKeyId).setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    database.getReference().child("Chats").child(receiverRoom)
                                                            .child("Messages").child(messageKeyId).setValue(message);


                                                }
                                            });

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });



                }


            }
        });

        binding.imgCamChatDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent camintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                camerapic.launch(camintent);

            }
        });
        binding.imgLinkChatDeatail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                opengallery.launch(intent);
            }
        });







    }

    private void showPopupMenuMeth(View view) {
        PopupMenu popupMenu=new PopupMenu(this,view);
        popupMenu.getMenuInflater().inflate(R.menu.chat_detale_menu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId()==R.id.chatDetailClearChat){
                    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Chats").child(senderRoom);
                    databaseReference.removeValue();
                    chatMessageRecyclesAdapter.notifyDataSetChanged();


                    }


                return true;
            }
        });
        popupMenu.show();
    }

    private void init() {

        //on click Of Back Arrow
        binding.backArrowChatDetailImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Chat_detail_Activity.this, MainActivity.class));
                finishAffinity();


            }
        });
        binding.backArrowChatDetailImgDeleteToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Chat_detail_Activity.this, MainActivity.class));
                finishAffinity();


            }
        });

        receiverName = getIntent().getStringExtra("receiverName");
        receiverId = getIntent().getStringExtra("receiverUID");
        receiverPic = getIntent().getStringExtra("receiverPic");

        binding.receiverNameChatDetailText.setText(receiverName);
        Picasso.get().load(receiverPic).placeholder(R.drawable.blank_profile_pic).into(binding.receiverPicChatDetailImg);

    }

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
                            Uri imgUri = FileProvider.getUriForFile(Chat_detail_Activity.this, "com.example.chattingapp.provider", imageFile);

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


    //for choosing image from gallery
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


    private File createImageFile() throws IOException {
        String imageFileName = "captured_image";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        return imageFile;
    }

    private void uploadImageToStorage(Uri imgUri) {
        Date date=new Date();
        StorageReference reference = storage.getReference().child("Sendpic").child(String.valueOf(date.getTime()));
        reference.putFile(imgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                        @Override
                        public void onSuccess(Uri uri) {
                            //Updating database for image
                            MessageModel message=new MessageModel(senderId,"",uri.toString(),date.getTime());
                            database.getReference().child("Chats").child(senderRoom)
                                    .child("Messages").push().addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String messageKeyId=snapshot.getKey();
                                            message.setMessageNodeId(messageKeyId);
                                            database.getReference().child("Chats").child(senderRoom)
                                                    .child("Messages").child(messageKeyId).setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            database.getReference().child("Chats").child(receiverRoom)
                                                                    .child("Messages").child(messageKeyId).setValue(message);

                                                        }
                                                    });

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                        }
                    });
                } else {
                    Toast.makeText(Chat_detail_Activity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    }












