package com.example.chattingapp.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.chattingapp.Adapter.StatusAdapter;
import com.example.chattingapp.MainActivity;
import com.example.chattingapp.Model.StatusImg;
import com.example.chattingapp.Model.UserProfile;
import com.example.chattingapp.Model.UserStatusModel;
import com.example.chattingapp.R;
import com.example.chattingapp.databinding.FragmentStatusBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;

import javax.net.ssl.SSLEngineResult;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;


public class Status_Fragment extends Fragment {
    StatusAdapter statusAdapter;
    FragmentStatusBinding binding;
    ArrayList<UserStatusModel> userStatusModelArrayList;
    FirebaseStorage storage;
    Dialog dialog;
    FirebaseDatabase database;
    UserProfile currentUser;


    public Status_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        binding=FragmentStatusBinding.inflate(inflater,container,false);
        storage=FirebaseStorage.getInstance();
        database=FirebaseDatabase.getInstance();
        dialog=new Dialog(getContext());
        currentUser=new UserProfile();
        userStatusModelArrayList=new ArrayList<>();


        //setting ProfileImage in empty status
        DatabaseReference databaseReferenc=FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid());
        databaseReferenc.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserProfile currentUser=snapshot.getValue(UserProfile.class);
                try {
                    Picasso.get().load(currentUser.getPROFILEIMAGE()).placeholder(R.drawable.blank_profile_pic).into(binding.circularImageViewUser);
                }catch (Exception e){
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //show Status
        showStatus();
        //deleting Status

        binding.deleteStatusImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder AlertDialog=new AlertDialog.Builder(getContext());
                AlertDialog.setTitle("Detelte Status");
                AlertDialog.setMessage("Are you sure to delete your Status?");
                AlertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog.setPositiveButton("Yes sure,Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Stories").child(FirebaseAuth.getInstance().getUid());
                        databaseReference.removeValue();
                        startActivity(new Intent(getActivity(), MainActivity.class));
                    }
                });
                AlertDialog.create();
                AlertDialog.show();







            }
        });

























        //

        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUser=snapshot.getValue(UserProfile.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        statusAdapter=new StatusAdapter(getContext(),userStatusModelArrayList);
        binding.statusRecycleView.setAdapter(statusAdapter);













    //img picking
        ActivityResultLauncher<Intent> showImg=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new  ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode()== Activity.RESULT_OK){
                    if(result.getData()!=null){

                        ClipData clipData=result.getData().getClipData();
                        int imgcount =clipData.getItemCount();


                        DialogVisible_meth(true);
                        for(int i=0;i<imgcount;i++){
                            Date date=new Date();
                            StorageReference reference=storage.getReference().child("Status")
                                    .child(String.valueOf(date.getTime()));
                            reference.putFile(clipData.getItemAt(i).getUri()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                    if(task.isSuccessful()){
                                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {

                                                UserStatusModel userStatus=new UserStatusModel();
                                                userStatus.setName(currentUser.getNAME());
                                                userStatus.setProfileImage(currentUser.getPROFILEIMAGE());
                                                userStatus.setLastUpdated(date.getTime());
                                                userStatus.setUserUid(currentUser.getUID());


                                                HashMap<String,Object> obj=new HashMap<>();
                                                obj.put("name",userStatus.getName());
                                                obj.put("profileImage",userStatus.getProfileImage());
                                                obj.put("lastUpdated",userStatus.getLastUpdated());
                                                obj.put("userUid",userStatus.getUserUid());

                                                String imgUri=uri.toString();
                                                StatusImg statusImg=new StatusImg(imgUri,userStatus.getLastUpdated());

                                                database.getReference().child("Stories")
                                                        .child(FirebaseAuth.getInstance().getUid()).updateChildren(obj);

                                                database.getReference().child("Stories").child(FirebaseAuth.getInstance().getUid())
                                                        .child("Status").push().setValue(statusImg);











                                            }
                                        });
                                    }

                                }
                            });

                        }
                        DialogVisible_meth(false);



                    }
                }

            }
        });
        //use status write button
        binding.floatbtnStatusWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK ,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                showImg.launch(intent);
               // DialogVisible_meth(true);
            }
        });






        return binding.getRoot();

    }
    private void  DialogVisible_meth(Boolean visible){

        dialog.setCancelable(false);
        dialog.setContentView(R.layout.progress_dialog);
        if(visible==true){
            dialog.show();
        }else {
            dialog.dismiss();
        }


    }
    private UserStatusModel manageStatusInfo(DataSnapshot snapshotStories){
        UserStatusModel userStatusModel = new UserStatusModel();

        userStatusModel.setName(snapshotStories.child("name").getValue(String.class));
        userStatusModel.setProfileImage(snapshotStories.child("profileImage").getValue(String.class));
        userStatusModel.setLastUpdated(snapshotStories.child("lastUpdated").getValue(Long.class));
        userStatusModel.setUserUid(snapshotStories.child("userUid").getValue(String.class));

        ArrayList<StatusImg> statusImgArrayList = new ArrayList<>();
        for (DataSnapshot statusSnapshot : snapshotStories.child("Status").getChildren()) {
            statusImgArrayList.add(statusSnapshot.getValue(StatusImg.class));
        }
        userStatusModel.setStatusImgArrayList(statusImgArrayList);
        return userStatusModel;


    }

    private void  showStatus(){
        database.getReference().child("Stories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    userStatusModelArrayList.clear();
                    for (DataSnapshot snapshotStories:snapshot.getChildren()) {
                        if(!FirebaseAuth.getInstance().getCurrentUser().getUid().equals(snapshotStories.child("userUid").getValue())){




                            userStatusModelArrayList.add(manageStatusInfo(snapshotStories));
                        }else {
                            //for Current User

                            UserStatusModel userStatus=manageStatusInfo(snapshotStories);

                            //setting status of user
                            try{


                                StatusImg lastImgedata=userStatus.getStatusImgArrayList().get(userStatus.getStatusImgArrayList().size()-1);
                                Picasso.get().load(lastImgedata.getImgUrl()).placeholder(R.drawable.blank_profile_pic).into(binding.circularImageViewUser);
                                binding.circularStatusViewUser.setPortionsCount(userStatus.getStatusImgArrayList().size());


                                binding.circularStatusViewUser.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        ArrayList<MyStory> myStories = new ArrayList<>();
                                        for (StatusImg statusImg:userStatus.getStatusImgArrayList()){
                                            myStories.add(new MyStory(statusImg.getImgUrl()));
                                        }

                                        new StoryView.Builder(getActivity().getSupportFragmentManager())
                                                .setStoriesList(myStories) // Required
                                                .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                                                .setTitleText(userStatus.getName()) // Default is Hidden
                                                .setSubtitleText("") // Default is Hidden
                                                .setTitleLogoUrl(userStatus.getProfileImage()) // Default is Hidden
                                                .setStoryClickListeners(new StoryClickListeners() {
                                                    @Override
                                                    public void onDescriptionClickListener(int position) {
                                                        //your action
                                                    }

                                                    @Override
                                                    public void onTitleIconClickListener(int position) {
                                                        //your action
                                                    }
                                                }) // Optional Listeners
                                                .build() // Must be called before calling show method
                                                .show();

                                    }
                                });}catch (Exception e){

                            }




                        }
                    }
                    statusAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });



    }

}